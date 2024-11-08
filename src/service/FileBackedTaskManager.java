package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import status.TaskType;
import status.Status;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

   // public static File fileForSavings = new File ("C:\\Users\\fored\\first-project\\test1.txt");
    //так проверяю сама, просьба не учитывать при проверке
public static File fileForSavings;

    static {
        try {
            fileForSavings = File.createTempFile("text", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() throws ManagerSaveException {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic,startTime,duration").append("\n");
        try (BufferedWriter fileWriter = new BufferedWriter(
                new FileWriter(fileForSavings, StandardCharsets.UTF_8))) {
            if (fileForSavings.exists()) {
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
            } else {
                System.out.println("Файла не существует");
            }
        } catch (IOException e) {
            System.out.println("Ошибка: данные не сохранены в файл");
        }
    }

    public File getFileForSavings() {
        return fileForSavings;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        try (BufferedReader fileReader = new BufferedReader(
                new FileReader(file, StandardCharsets.UTF_8))) {
            if (file.exists()) {
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
            } else {
                System.out.println("Файла не существует");
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
        sb.append(",").append(task.getStartTime());
        sb.append(",").append(task.getDuration().toMinutes());
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
        String description = line[4];
        Status status = Status.valueOf(line[3]);
        LocalDateTime startTime = LocalDateTime.parse(line[5]);
        Duration duration = Duration.ofMinutes(Long.parseLong(line[6]));
        Task task = null;
        switch (type) {
            case TaskType.SUBTASK:
                int epicId = Integer.parseInt(line[7]);
                task = new SubTask(name, description, status, epicId,startTime,duration);
                break;
            case TaskType.EPIC:
                task = new Epic(name, description, status, startTime, duration);
                break;
            case TaskType.TASK:
                task = new Task(name, description, status,startTime, duration);
                break;
            default:
                System.out.println("Такого типа не существует");
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
        return task;
    }

    @Override
    public Epic getEpicById(int taskId) {
        Epic epic = super.getEpicById(taskId);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int taskId) {
        SubTask subTask = super.getSubTaskById(taskId);
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
