package service;
import exceptions.NotFoundException;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.List;

import java.util.ArrayList;
import java.util.Set;

public interface TaskManager {

    List<Task> getHistory()throws NotFoundException;

    Task createTask(Task task);

    SubTask createSubTask(SubTask subTask);

    Epic createEpic(Epic epic) throws NotFoundException;

    ArrayList<Task> getAllTaskList() throws NotFoundException;

    ArrayList<Epic> getAllEpicList() throws NotFoundException;

    ArrayList<SubTask> getAllSubTaskList() throws NotFoundException;

    void deleteTaskList();

    void deleteEpicList();

    void deleteSubTaskList();

    Task getTaskById(int taskId) throws NotFoundException;

    Epic getEpicById(int taskId) throws NotFoundException;

    SubTask getSubTaskById(int taskId) throws NotFoundException;

    void removeTaskById(int taskId) throws NotFoundException;

    void removeEpicById(int taskId) throws NotFoundException;

    void removeSubTaskById(int taskId) throws NotFoundException;

    void updateTask(Task task) throws NotFoundException;

    void updateEpic(Epic epic) throws NotFoundException;

    void updateSubTask(SubTask subTask) throws NotFoundException;

    List<SubTask> getSubTasksByEpicId(int epicId) throws NotFoundException;

    Set<Task> getPrioritizedTasks() throws NotFoundException;

    void calculateEpicStartTimeAndDurationAndEndTime(Epic epic);
}
