package main.service;

import main.model.Epic;
import main.model.SubTask;
import main.model.Task;

import java.util.ArrayList;

public interface TaskManager {

    int createNewId();//создаем новый id

    //ArrayList<Task> getHistory();

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
