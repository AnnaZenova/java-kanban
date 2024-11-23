package service;
import exceptions.ManagerValidateException;
import model.Epic;
import model.SubTask;
import model.Task;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
    }

    int taskId = 0;
    protected static final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();

    final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int createNewId() { //создаем новый id
        return taskId++;
    }

    @Override
    public Task createTask(Task task) { //создаем новый main.model.Task
        task.setTaskId(createNewId());
        tasks.put(task.getTaskId(), task);
        Optional<Task> result = Optional.ofNullable(tasks.values().stream()
                .filter(this::checkOverlaps)
                .findFirst()
                .orElseThrow(() -> new ManagerValidateException("Задача пересекается во времени с другими")));
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setTaskId(createNewId());
        subTasks.put(subTask.getTaskId(), subTask);
        Optional<Task> result = Optional.of(subTasks.values().stream()
                .filter(this::checkOverlaps)
                .findFirst().orElseThrow(() -> new ManagerValidateException("Задача пересекается во времени с другими")));
        prioritizedTasks.add(subTask);
        int subTaskId = subTask.getTaskId();
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskIdToList(subTaskId);
        countStatusByEpicId(subTask.getEpicId());
        calculateEpicStartTimeAndDurationAndEndTime(epic);
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setTaskId(createNewId());
        epics.put(epic.getTaskId(), epic);
        return epic;
    }

    @Override
    public ArrayList<Task> getAllTaskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskList() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteTaskList() {
        tasks.values().stream()
                .map(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public void deleteEpicList() {
        subTasks.clear();
        epics.values().stream()
                .map(prioritizedTasks::remove);
        epics.clear();
    }

    @Override
    public void deleteSubTaskList() {
        for (Epic epic : epics.values()) {
            prioritizedTasks.remove(getSubTasksByEpicId(epic.getTaskId()));
            epic.subTaskIdList.clear();
            countStatusByEpicId(epic.getTaskId());
        }
        subTasks.clear();
    }

    @Override
    public Task getTaskById(int taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("No task list found with id " + taskId);
        }
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);

    }

    @Override
    public Epic getEpicById(int taskId) {
        if (!epics.containsKey(taskId)) {
            System.out.println("No epic list found with id " + taskId);
        }
        historyManager.add(epics.get(taskId));
        return epics.get(taskId);
    }

    @Override
    public SubTask getSubTaskById(int taskId) {
        if (!subTasks.containsKey(taskId)) {
            System.out.println("No subTask found with id " + taskId);
        }
        historyManager.add(subTasks.get(taskId));
        return subTasks.get(taskId);
    }

    @Override
    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int taskId) {
        Epic epic = getEpicById(taskId);
        for (Integer idToDelete : epic.subTaskIdList) {
            subTasks.remove(idToDelete);
        }
        epics.remove(taskId);
    }

    @Override
    public void removeSubTaskById(int taskId) {
        SubTask subTask = getSubTaskById(taskId);
        Epic epic = getEpicById(subTask.getEpicId());
        epic.subTaskIdList.remove(taskId);
        subTasks.remove(taskId);
        countStatusByEpicId(subTask.getEpicId());
        calculateEpicStartTimeAndDurationAndEndTime(epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic createdEpic = epics.get(epic.getTaskId());
        if (createdEpic != null) {
            return;
        }
        createdEpic.setName(epic.getName());
        createdEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getTaskId(), subTask);
        countStatusByEpicId(subTask.getEpicId());
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int epicId) {
        return epics.get(epicId).getSubTaskIdList().stream()
                .map(this::getSubTaskById)
                .collect(Collectors.toList());
    }

    public void countStatusByEpicId(int epicId) {
        List<SubTask> subTasksInEpic = getSubTasksByEpicId(epicId);
        if (subTasksInEpic.isEmpty()) {
            Epic epic = getEpicById(epicId);
            epic.setStatus(Status.NEW);
        }
        boolean isAllStatusNew = true;
        boolean isAllStatusDone = true;
        for (SubTask subTask : subTasksInEpic) {
            if (subTask.getStatus() != Status.DONE) {
                isAllStatusDone = false;
            }
            if (subTask.getStatus() != Status.NEW) {
                isAllStatusNew = false;
            }
        }
        if (isAllStatusNew) {
            Epic epic = getEpicById(epicId);
            epic.setStatus(Status.NEW);
        } else if (isAllStatusDone) {
            Epic epic = getEpicById(epicId);
            epic.setStatus(Status.DONE);
        } else {
            Epic epic = getEpicById(epicId);
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void calculateEpicStartTimeAndDurationAndEndTime(Epic epic) {
        List<Integer> subTaskIds = epic.getSubTaskIdList();
        LocalDateTime startTime = null;
        Duration duration = Duration.ofHours(0);
        LocalDateTime subTaskEndTime = null;
        for (int id : subTaskIds) {
            SubTask subTask = subTasks.get(id);
            if (subTask.getStartTime() != null) {
                if (startTime == null || startTime.isAfter(subTask.getStartTime())) {
                    startTime = subTask.getStartTime();
                }
            } else {
                System.out.println("Subtask start time equals null.");
            }
            Duration subTaskDuration = subTask.getDuration();
            duration = duration.plus(subTaskDuration);
            subTaskEndTime = subTask.getCalculatedEndTime(startTime, duration);
            if (subTaskEndTime != null) {
                if (subTaskEndTime == null || subTaskEndTime.isBefore(subTask.getCalculatedEndTime(subTask.getStartTime(), duration))) {
                    subTaskEndTime = subTask.getCalculatedEndTime(subTask.getStartTime(), duration);
                }
            }

        }
        epic.setDuration(duration);
        epic.setStartTime(startTime);
        epic.setEndTime(subTaskEndTime);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return new HashSet<>(prioritizedTasks);
    }

    public boolean checkOverlaps(Task task) {
        Set<Task> tasks = getPrioritizedTasks();
        for (Task task1 : tasks) {
            if (task1.getStartTime() == null && task1.getCalculatedEndTime(task1.getStartTime(), task1.getDuration()) == null) {
                break;
            } else if (
                    (task1.getStartTime().equals(task.getStartTime())
                            && task1.getCalculatedEndTime(task1.getStartTime(), task1.getDuration()).equals(task.getCalculatedEndTime(task.getStartTime(), task.getDuration())))
                            || (task1.getStartTime().isBefore(task.getStartTime())
                            && (task1.getCalculatedEndTime(task1.getStartTime(), task1.getDuration()).isAfter(task.getStartTime()))
                            || (task1.getStartTime().isAfter(task.getStartTime()))
                            && (task1.getStartTime().isBefore(task.getCalculatedEndTime(task.getStartTime(), task.getDuration()))))
            ) {
                return false;
            }
        }
        return true;
    }
}














