
public class Main {


    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager a = new TaskManager();
        a.deleteEpicList();
        a.deleteTaskList();
        a.deleteSubTaskList();
        Task task1 = a.createTask(new Task("Задача1", "Описание1", Status.NEW));
        Task task2 = a.createTask(new Task("Задача2", "Описание2", Status.NEW));
        Epic epic1 = a.createEpic(new Epic("Эпик1", "Описание эпика1", Status.IN_PROGRESS));
        System.out.println(epic1.getStatus());
        SubTask subTask1 = a.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", Status.DONE, epic1.getTaskId()));
        SubTask subTask2 = a.createSubTask(new SubTask("Подзадача2", "Описание подзадачи2", Status.DONE, epic1.getTaskId()));
        System.out.println(a.getStatus());
        System.out.println(a.getSubTasks());
        a.updateSubTask(new SubTask("Задача update", "Описание update", Status.NEW, epic1.getTaskId()));
        System.out.println(a.getStatus());
        System.out.println(a.getSubTasks());
        Epic epic2 = a.createEpic(new Epic("Эпик2", "Описание эпика2", Status.DONE));
        SubTask subTask3 = a.createSubTask(new SubTask("Подзадача", "Описание подзадачи", Status.NEW, epic2.getTaskId()));
        System.out.println(a.getStatus());
        System.out.println(a.getAllEpicList());
        System.out.println(a.getAllTaskList());
        System.out.println(a.getAllSubTaskList());
    }
}
