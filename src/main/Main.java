package main;
import main.status.Status;
import main.model.Epic;
import main.model.SubTask;
import main.model.Task;
import main.service.InMemoryTaskManager;
import main.service.InMemoryHistoryManager;

public class Main {


    public static void main(String[] args) {
        System.out.println("Поехали!");
        InMemoryTaskManager InMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        InMemoryTaskManager.deleteEpicList();
        InMemoryTaskManager.deleteTaskList();
        InMemoryTaskManager.deleteSubTaskList();
        System.out.println(InMemoryTaskManager.getAllTaskList());
        System.out.println("====================================");
        System.out.println(InMemoryTaskManager.getAllEpicList());
        System.out.println("====================================");
        System.out.println(InMemoryTaskManager.getAllSubTaskList());
        System.out.println("====================================");
        Task task1 = InMemoryTaskManager.createTask(new Task("Задача1", "Описание1", Status.NEW));
        Task task2 = InMemoryTaskManager.createTask(new Task("Задача1", "Описание1", Status.NEW));

        for (int i=1; i<=11;i++) {
            InMemoryTaskManager.getTaskById(task1.getTaskId());
            InMemoryTaskManager.getTaskById(task2.getTaskId());
        }
        System.out.println(historyManager.viewHistory.size());
        //System.out.println(InMemoryTaskManager.getAllTaskList());

        Epic epic1 = InMemoryTaskManager.createEpic(new Epic("Эпик1", "Описание эпика1", Status.IN_PROGRESS));//я тут проверяла
        //что при любом переданном мной статусе, если я указала в конструкторе NEW, то так дальше и будет
        SubTask subTask1 = InMemoryTaskManager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", Status.DONE, epic1.getTaskId()));
        SubTask subTask2 = InMemoryTaskManager.createSubTask(new SubTask("Подзадача2", "Описание подзадачи2", Status.DONE, epic1.getTaskId()));
        System.out.println(subTask1);
        InMemoryTaskManager.updateSubTask(new SubTask("Задача update", "Описание update", Status.NEW, epic1.getTaskId()));
        Epic epic2 = InMemoryTaskManager.createEpic(new Epic("Эпик2", "Описание эпика2", Status.DONE));
        SubTask subTask3 = InMemoryTaskManager.createSubTask(new SubTask("Подзадача", "Описание подзадачи", Status.NEW, epic2.getTaskId()));
        InMemoryTaskManager.getSubTasksByEpicId(epic1.getTaskId());
        System.out.println(InMemoryTaskManager.getAllTaskList());
        System.out.println("====================================");
        System.out.println(InMemoryTaskManager.getAllEpicList());
        System.out.println("====================================");
        System.out.println(InMemoryTaskManager.getAllSubTaskList());
        System.out.println("====================================");
        System.out.println( InMemoryTaskManager.getSubTasksByEpicId(epic1.getTaskId()));
        System.out.println("====================================");
        InMemoryTaskManager.removeEpicById(epic2.getTaskId());
        System.out.println("====================================");
        InMemoryTaskManager.removeTaskById(task2.getTaskId());
        System.out.println(InMemoryTaskManager.getAllTaskList());
        System.out.println("====================================");
        System.out.println(InMemoryTaskManager.getAllEpicList());
        System.out.println("====================================");
        System.out.println(InMemoryTaskManager.getAllSubTaskList());
        System.out.println("====================================");

    }
}
