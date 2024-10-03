package service;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.List;

import java.util.ArrayList;

public interface TaskManager {

    List<Task> getHistory();

    Task createTask(Task task);

    SubTask createSubTask(SubTask subTask);

    Epic createEpic(Epic epic);

    ArrayList<Task> getAllTaskList();

    ArrayList<Epic> getAllEpicList();

    ArrayList<SubTask> getAllSubTaskList();

    void deleteTaskList();

    void deleteEpicList();

    void deleteSubTaskList();

    Task getTaskById(int taskId);

    Epic getEpicById(int taskId);

    SubTask getSubTaskById(int taskId);

    void removeTaskById(int taskId);

    void removeEpicById(int taskId);

    void removeSubTaskById(int taskId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    ArrayList<SubTask> getSubTasksByEpicId(int epicId);
}
