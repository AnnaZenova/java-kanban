import service.FileBackedTaskManager;
import service.Managers;
import status.Status;
import model.Epic;
import model.SubTask;
import model.Task;
import service.TaskManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Поехали!");
        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager();
        System.out.println(backedTaskManager.loadFromFile(backedTaskManager.getFileForSavings()).getAllTaskList());
        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.createTask(new Task("Задача1", "Описание1", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Задача1", "Описание1", Status.NEW));
        backedTaskManager.createTask(task1);
        backedTaskManager.createTask(task2);
        Epic epic1 = taskManager.createEpic(new Epic("Эпик1", "Описание эпика1", Status.IN_PROGRESS));//я тут проверяла
        backedTaskManager.createEpic(epic1);

        SubTask subTask1 = taskManager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", Status.DONE, epic1.getTaskId()));
        backedTaskManager.createSubTask(subTask1);

        taskManager.updateSubTask(new SubTask("Задача update", "Описание update", Status.NEW, epic1.getTaskId()));
        Epic epic2 = taskManager.createEpic(new Epic("Эпик2", "Описание эпика2", Status.DONE));
        backedTaskManager.createEpic(epic2);
        //проверки по файлам:
        File file = new File("C:\\Users\\fored\\first-project\\tests.txt");
        File file1 = new File("A:\\Users\\fored\\first-project\\tests.txt");

        System.out.println(backedTaskManager.loadFromFile(backedTaskManager.getFileForSavings()));//восстановление из файла (вернет пустой массив)
        System.out.println(Files.readString(backedTaskManager.getFileForSavings().toPath()));
        System.out.println(backedTaskManager.loadFromFile(backedTaskManager.getFileForSavings()).getAllTaskList());//вернет список задач в нужном формате (проверяем и сохранение и восстановление)
        System.out.println(backedTaskManager.loadFromFile(backedTaskManager.getFileForSavings()));

        backedTaskManager.loadFromFile(file);//Exception
        System.out.println(backedTaskManager.loadFromFile(file1));//Exception

    }
}

