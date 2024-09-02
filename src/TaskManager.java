import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    int taskId = 0;
    static final private HashMap<Integer, Task> tasks = new HashMap<>();
    static final private HashMap<Integer, Epic> epics = new HashMap<>();
    static final private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int createNewId() {//создаем новый id
        return taskId++;
    }

    public Task createTask(Task task) {//создаем новый Task
        task.setTaskId(createNewId());
        tasks.put(task.getTaskId(), task);
        return task;
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setTaskId(createNewId());
        subTasks.put(subTask.getTaskId(), subTask);
        int subTaskId = subTask.getTaskId();
        Epic epic1 = epics.get(subTask.getEpicId());
        epic1.addSubTaskIdToLisT(subTaskId);
        countStatusByEpicId(subTask.getEpicId());
        return subTask;
    }


    public Epic createEpic(Epic epic) {
        epic.setTaskId(createNewId());
        epics.put(epic.getTaskId(), epic);
        return epic;
    }

    public ArrayList<Task> getAllTaskList() {
        ArrayList<Task> allTaskList = new ArrayList<>();
        for (Task taskList : tasks.values()) {//для каждого клчюча объекта нужно
            allTaskList.add(taskList);
        }
        return allTaskList;
    }

    public ArrayList<Epic> getAllEpicList() {
        ArrayList<Epic> allEpicList = new ArrayList<>();
        for (Epic epicList : epics.values()) {
            allEpicList.add(epicList);
        }
        return allEpicList;
    }

    static public ArrayList<SubTask> getAllSubTaskList() {
        ArrayList<SubTask> allSubTaskList = new ArrayList<>();
        for (SubTask tasksList : subTasks.values()) {
            allSubTaskList.add(tasksList);
        }
        return allSubTaskList;
    }

    public void deleteTaskList() {
        tasks.clear();
    }

    public void deleteEpicList() {
        for (Integer key : epics.keySet()) {//для каждого объекта получаем ключ
            Epic epicList = epics.get(key);//получаем епик по ключу
            for (Integer subTaskid : epicList.subTaskIdList) {//для каждого id из списка idсабтасков конкретного эпика
                subTasks.remove(subTaskid);//удаляем объект сабтаск по id
            }
        }
        epics.clear();
    }

    public void deleteSubTaskList() {
        for (Integer key : epics.keySet()) {//для каждого объекта получаем ключ
            Epic epic = epics.get(key);//получаем объект по ключу
            ArrayList<Integer> epicId = new ArrayList<>();
            for (Integer key1 : epic.subTaskIdList) {
                SubTask subTask1 = getSubTaskById(key1);
                epicId.add(subTask1.getEpicId());
            }
            epic.subTaskIdList.clear();
            for (Integer key2 : epicId) {
                countStatusByEpicId(key2);
            }
        }
        subTasks.clear();
    }

    public Task getTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        } else {
            System.out.println("No task list found with id " + taskId);
        }
        return tasks.get(taskId);
    }

    public Epic getEpicById(int taskId) {
        epics.get(taskId);
        if (epics.containsKey(taskId)) {
            return epics.get(taskId);
        } else {
            System.out.println("No epic list found with id " + taskId);
        }
        return epics.get(taskId);
    }

    public SubTask getSubTaskById(int taskId) {
        if (subTasks.containsKey(taskId)) {
            return subTasks.get(taskId);
        } else {
            System.out.println("No subTask found with id " + taskId);
        }
        return subTasks.get(taskId);
    }

    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpicById(int taskId) {
        Epic epic1 = getEpicById(taskId);
        for (Integer idToDelete : epic1.subTaskIdList) {
            subTasks.remove(idToDelete);
        }
        epics.remove(taskId);
    }

    public void removeSubTaskById(int taskId) {
        SubTask subTask = getSubTaskById(taskId);
        Epic epic1 = getEpicById(subTask.getEpicId());
        epic1.subTaskIdList.remove(taskId);
        subTasks.remove(taskId);
        countStatusByEpicId(subTask.getEpicId());
    }

    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic createdEpic = epics.get(epic.getTaskId());
        if (createdEpic != null) {
            return;
        }
        createdEpic.setName(epic.getName());
        createdEpic.setDescription(epic.getDescription());
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getTaskId(), subTask);
        countStatusByEpicId(subTask.getEpicId());
    }

    public ArrayList<SubTask> getSubTasksByEpicId(int epicId) {
        Epic epic1 = getEpicById(epicId);
        ArrayList<SubTask> subTasksInEpic = new ArrayList<>();
        for (Integer subTaskId : epic1.subTaskIdList) {
            subTasksInEpic.add(subTasks.get(subTaskId));
        }
        return subTasksInEpic;
    }

    private Status countStatusByEpicId(int epicId) {
        ArrayList<SubTask> subTasksInEpic = getSubTasksByEpicId(epicId);
        if (subTasksInEpic.size() == 0) {
            Epic epic1 = getEpicById(epicId);
            epic1.status = Status.NEW;
            return Status.NEW;
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
            Epic epic1 = getEpicById(epicId);
            epic1.status = Status.NEW;
            return Status.NEW;
        }
        if (isAllStatusDone) {
            Epic epic1 = getEpicById(epicId);
            epic1.status = Status.DONE;
            return Status.DONE;
        }
        Epic epic1 = getEpicById(epicId);
        epic1.status = Status.IN_PROGRESS;
        return Status.IN_PROGRESS;
    }
}













