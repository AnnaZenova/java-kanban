import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    static int taskId = 0;

    static private HashMap<Integer, Task> tasks = new HashMap<>();
    static private HashMap<Integer, Epic> epics = new HashMap<>();
    static private HashMap<Integer, SubTask> subTasks = new HashMap<>();


    public int createNewId() {//создаем новый id
        taskId++;
        return taskId;
    }

    public Task createTask(Task task) {//создаем новый Task
        task.setTaskId(createNewId());
        tasks.put(task.getTaskId(), task);
        return task;
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setTaskId(createNewId());
        subTasks.put(subTask.getTaskId(), subTask);
        return subTask;
    }


    public Epic createEpic(Epic epic) {
        epic.setTaskId(createNewId());
        epics.put(epic.getTaskId(), epic);
        return epic;
    }

    public ArrayList getAllTaskList() {
        ArrayList<Task> allTaskList = new ArrayList<>();
        for (Integer key : tasks.keySet()) {
            Task taskList = tasks.get(key);
            allTaskList.add(taskList);
        }
        return allTaskList;
    }

    public ArrayList getAllEpicList() {
        ArrayList<Epic> allEpicList = new ArrayList<>();
        for (Integer key : epics.keySet()) {
            Epic epicList = epics.get(key);
            allEpicList.add(epicList);
        }
        return allEpicList;
    }

    static public ArrayList<SubTask> getAllSubTaskList() {
        ArrayList<SubTask> allSubTaskList = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            SubTask subTaskList = subTasks.get(key);
            allSubTaskList.add(subTaskList);
        }
        return allSubTaskList;
    }

    public void deleteTaskList() {
        tasks.clear();
    }

    public void deleteEpicList() {
        epics.clear();
    }

    public void deleteSubTaskList() {
        subTasks.clear();
    }

    public Task getTaskById(int taskId) {
        Task taskbyId = tasks.get(taskId);
        if (taskbyId != null) {
            return taskbyId;
        } else {
            System.out.println("No task list found with id " + taskId);
        }
        return tasks.get(taskId);


    }

    public Epic getEpicById(int taskId) {
        Epic epicbyId = epics.get(taskId);
        if (epicbyId != null) {
            return epicbyId;
        } else {
            System.out.println("No epic list found with id " + taskId);
        }
        return epics.get(taskId);

    }

    public SubTask getSubTaskById(int taskId) {
        SubTask subTaskbyId = subTasks.get(taskId);
        if (subTaskbyId != null) {
            return subTaskbyId;
        } else {
            System.out.println("No subTask found with id " + taskId);
        }
        return subTaskbyId;

    }

    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpicById(int taskId) {
        epics.remove(taskId);
    }

    public void removeSubTaskById(int taskId) {
        subTasks.remove(taskId);
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
        Status status = getStatus();
        Epic epic = getEpicById(subTask.getEpicId());
        epic.setStatus(status);
    }


   public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasksInEpic = new ArrayList<>();
        for (SubTask subTask : getAllSubTaskList()) {
            if (subTask.getEpicId() != subTask.getTaskId()) {
                subTasksInEpic.add(subTask);
            }
        }
       System.out.println(subTasksInEpic);
        return subTasksInEpic;

    }

    public Status getStatus() {
        ArrayList<SubTask> subTasksInEpic = getSubTasks();
        if (subTasksInEpic.size() == 0) {
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
            return Status.NEW;
        }
        if (isAllStatusDone) {
            return Status.DONE;
        }

        return Status.IN_PROGRESS;
    }
}













