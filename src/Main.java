import service.Managers;
import status.Status;
import model.Epic;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {


    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();

        taskManager.deleteEpicList();
        taskManager.deleteTaskList();
        taskManager.deleteSubTaskList();
        //System.out.println(taskManager.getAllTaskList());
        System.out.println("====================================");
        //System.out.println(taskManager.getAllEpicList());
        System.out.println("====================================");
        //System.out.println(taskManager.getAllSubTaskList());
        System.out.println("====================================");
        Task task1 = taskManager.createTask(new Task("Задача1", "Описание1", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Задача1", "Описание1", Status.NEW));

        for (int i=1; i<=11;i++) {
            taskManager.getTaskById(task1.getTaskId());
            taskManager.getTaskById(task2.getTaskId());
        }

        //System.out.println(InMemoryTaskManager.getAllTaskList());

        /*Epic epic1 = taskManager.createEpic(new Epic("Эпик1", "Описание эпика1", Status.IN_PROGRESS));//я тут проверяла
        //что при любом переданном мной статусе, если я указала в конструкторе NEW, то так дальше и будет
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", Status.DONE, epic1.getTaskId()));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("Подзадача2", "Описание подзадачи2", Status.DONE, epic1.getTaskId()));
        System.out.println(subTask1);
        taskManager.updateSubTask(new SubTask("Задача update", "Описание update", Status.NEW, epic1.getTaskId()));
        Epic epic2 = taskManager.createEpic(new Epic("Эпик2", "Описание эпика2", Status.DONE));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("Подзадача", "Описание подзадачи", Status.NEW, epic2.getTaskId()));
        taskManager.getSubTasksByEpicId(epic1.getTaskId());
        System.out.println(taskManager.getAllTaskList());
        System.out.println("====================================");
        System.out.println(taskManager.getAllEpicList());
        System.out.println("====================================");
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println("====================================");
        System.out.println( taskManager.getSubTasksByEpicId(epic1.getTaskId()));
        System.out.println("====================================");
        taskManager.removeEpicById(epic2.getTaskId());
        System.out.println("====================================");
        taskManager.removeTaskById(task2.getTaskId());
        System.out.println(taskManager.getAllTaskList());
        System.out.println("====================================");
        System.out.println(taskManager.getAllEpicList());
        System.out.println("====================================");
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println("====================================");*/
        System.out.println(taskManager.getHistory());

    }
}
