package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import status.TaskType;
import status.Status;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    //public static File fileForSavings = new File ("C:\\Users\\fored\\first-project\\test.txt");
    //так проверяю сама, просьба не учитывать при проверке

    public static File fileForSavings;

    static {
        try {
            fileForSavings = File.createTempFile("text", ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() throws ManagerSaveException {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic").append("\n");
        try (BufferedWriter fileWriter = new BufferedWriter(
                new FileWriter(fileForSavings, StandardCharsets.UTF_8))) {
            fileWriter.write(sb.toString());
            for (Task task : getAllTaskList()) {
                fileWriter.write(taskToString(task) + "\n");
            }
            for (Epic epic : getAllEpicList()) {
                fileWriter.write(taskToString(epic) + "\n");
                sb.append("\n");
            }
            for (SubTask subTask : getAllSubTaskList()) {
                fileWriter.write(taskToString(subTask) + "\n");

            }
        } catch (IOException e) {
            System.out.println("Ошибка: данне не сохранены в файл");
        }
    }

    public File getFileForSavings() {
        return fileForSavings;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        try (BufferedReader fileReader = new BufferedReader(
                new FileReader(fileForSavings, StandardCharsets.UTF_8))) {
            fileReader.readLine();
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                if (!line.isEmpty()) {
                    Task task = taskManager.fromString(line);
                    tasks.put(task.getTaskId(), task);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: данные из файла не восстановлены");
        }
        return taskManager;
    }

    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        TaskType type = task.getType();
        sb.append(task.getTaskId());
        sb.append(",").append(type);
        sb.append(",").append(task.getName());
        sb.append(",").append(task.getStatus());
        sb.append(",").append(task.getDescription());
        if (type.equals(TaskType.SUBTASK)) {
            sb.append(",").append(((SubTask) task).getEpicId());
        }
        return sb.toString();
    }

    private Task fromString(String value) {
        String[] line = value.split(",");
        int taskId = Integer.parseInt(line[0]);
        TaskType type = TaskType.valueOf(line[1]);
        String name = line[2];
        Status status = Status.valueOf(line[3]);
        String description = line[4];
        Task task;
        if (type == TaskType.SUBTASK) {
            int epicId = Integer.parseInt(line[5]);
            task = new SubTask(name, description, status, epicId);
        } else if (type == TaskType.EPIC) {
            task = new Epic(name, description, status);
        } else {
            task = new Task(name, description, status);
        }
        task.setTaskId(taskId);
        task.setStatus(status);
        return task;
    }

    @Override
    public Task createTask(Task task) {
        Task nTask = super.createTask(task);
        save();
        return nTask;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask nSubTask = super.createSubTask(subTask);
        save();
        return nSubTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic nEpic = super.createEpic(epic);
        save();
        return nEpic;
    }

    @Override
    public ArrayList<Task> getAllTaskList() {
        return super.getAllTaskList();
    }

    @Override
    public ArrayList<Epic> getAllEpicList() {
        return super.getAllEpicList();
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskList() {
        return super.getAllSubTaskList();
    }

    @Override
    public void deleteTaskList() {
        super.deleteTaskList();
        save();
    }

    @Override
    public void deleteEpicList() {
        super.deleteEpicList();
        save();
    }

    @Override
    public void deleteSubTaskList() {
        super.deleteSubTaskList();
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int taskId) {
        Epic epic = super.getEpicById(taskId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int taskId) {
        SubTask subTask = super.getSubTaskById(taskId);
        save();
        return subTask;
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(int taskId) {
        super.removeEpicById(taskId);
        save();
    }

    @Override
    public void removeSubTaskById(int taskId) {
        super.removeSubTaskById(taskId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }
}