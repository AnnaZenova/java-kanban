package main.service;

import main.model.Epic;
import main.model.SubTask;
import main.model.Task;
import main.status.Status;

import java.util.HashMap;
import java.util.ArrayList;


public class InMemoryTaskManager implements TaskManager {

   // ArrayList<Task> viewHistory = new ArrayList<>();
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    int taskId = 0;
    private final  HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();



   /* @Override
    public ArrayList<Task> getHistory() {
        if (viewHistory.isEmpty()) {
            System.out.println("No view history");
        }
        return viewHistory;
    }
*/
    @Override
    public int createNewId() {//создаем новый id
        return taskId++;
    }

    @Override
    public Task createTask(Task task) {//создаем новый main.model.Task
        task.setTaskId(createNewId());
        tasks.put(task.getTaskId(), task);
        return task;
    }


    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setTaskId(createNewId());
        subTasks.put(subTask.getTaskId(), subTask);
        int subTaskId = subTask.getTaskId();
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskIdToLisT(subTaskId);
        countStatusByEpicId(subTask.getEpicId());
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
        tasks.clear();
    }


    @Override
    public void deleteEpicList() {
        subTasks.clear();
        epics.clear();
    }


    @Override
    public void deleteSubTaskList() {
        for (Epic epic : epics.values()) {
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
        //if (viewHistory.size()>=10) {
          //  viewHistory.remove(0);
       // }
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
    public ArrayList<SubTask> getSubTasksByEpicId(int epicId) {
        Epic epic = getEpicById(epicId);
        epic.getSubTaskIdList();
        ArrayList<SubTask> subTasksInEpic = new ArrayList<>();
        for (Integer subTaskId : epic.subTaskIdList) {
            subTasksInEpic.add(subTasks.get(subTaskId));
        }
        return subTasksInEpic;
    }

    private void countStatusByEpicId(int epicId) {
        ArrayList<SubTask> subTasksInEpic = getSubTasksByEpicId(epicId);
        if (subTasksInEpic.size() == 0) {
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
        }
        if (isAllStatusDone) {
            Epic epic = getEpicById(epicId);
            epic.setStatus(Status.DONE);
        }
        Epic epic = getEpicById(epicId);
        epic.setStatus(Status.IN_PROGRESS);
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}













