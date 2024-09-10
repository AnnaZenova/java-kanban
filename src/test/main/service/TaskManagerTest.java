package main.service;


import main.model.Task;
import main.service.InMemoryTaskManager;
import main.model.SubTask;
import main.model.Epic;
import main.service.InMemoryHistoryManager;
import  java.util.ArrayList;
import main.service.HistoryManager;
import main.service.TaskManager;


import main.status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest extends InMemoryHistoryManager {

    InMemoryTaskManager manager = new InMemoryTaskManager();
    Task task = new Task("Новая таска","для проверки", Status.NEW);
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();



    @BeforeEach
    void deleteTasksLists() { //обнуляем все листы, чтоб id начиналось с 0.
        manager.deleteTaskList();
        manager.deleteSubTaskList();
        manager.deleteEpicList();
    }



    @Test
    void shouldCreateEqualTasksById() {
        manager.createTask(task);
        Task task1 = manager.getTaskById(task.getTaskId());
        Task task2 = manager.getTaskById(task.getTaskId());
        assertEquals(task1,task2);
    }

    @Test
    void shouldCreateEqualSubTasksById() {
        Epic epic  = new Epic ("Новый епик","для проверки",Status.NEW);
        manager.createEpic(epic);
        SubTask subTask =new SubTask("Новая cабтаска","для проверки",Status.NEW,epic.getTaskId());
        manager.createSubTask(subTask);
        SubTask subTask1 = manager.getSubTaskById(subTask.getTaskId());
        SubTask subTask2 = manager.getSubTaskById(subTask.getTaskId());
        assertEquals(subTask1,subTask2);
    }

    @Test
    void shouldCreateEqualEpicsById() {
        Epic epic  = new Epic ("Новый епик","для проверки",Status.NEW);
        manager.createEpic(epic);
        Epic epic1 = manager.getEpicById(epic.getTaskId());
        Epic epic2 = manager.getEpicById(epic.getTaskId());
        assertEquals(epic1,epic2);
    }

    @Test
    void historyShouldNotBeNull() {
        historyManager.viewHistory.add(task);
        final ArrayList<Task> viewHistory1 = historyManager.getHistory();
        assertNotNull(viewHistory1, "История не пустая.");
        assertEquals(1, viewHistory1.size(), "История не пустая.");
    }
    @Test
void epicShouldNotBeAddedAsASubTask() {
        Epic epic  = new Epic ("Новый епик","для проверки",Status.NEW);
        manager.createEpic(epic);
        SubTask subTask =new SubTask("Новая cабтаска","для проверки",Status.NEW,epic.getTaskId());
        subTask.setTaskId(epic.getTaskId());
        manager.updateSubTask(subTask);
        assertEquals(0,epic.subTaskIdList.size(),"В список id сабтасок не попал новый id");
    }

    @Test
    void subTaskShouldNotBeAddedAsAnEpic() {
        Epic epic  = new Epic ("Новый епик","для проверки",Status.NEW);
        SubTask subTask = new SubTask("Новая cабтаска","для проверки",Status.NEW,epic.getTaskId());
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        subTask.setEpicId(subTask.getTaskId());
        manager.getEpicById(subTask.getTaskId());
        assertNull(manager.getEpicById(subTask.getTaskId()),"Такого епика с таким ID больше нет");
    }


@Test //getHistory возвращает 10 задач (ни больше ни меньше)
    void shoulBeNotMoreThen10Tasks(){
    manager.createTask(task);
    Epic epic  = new Epic ("Новый епик","для проверки",Status.NEW);
    manager.createEpic(epic);
    SubTask subTask =new SubTask("Новая cабтаска","для проверки",Status.NEW,epic.getTaskId());
    manager.createSubTask(subTask);

    for (int i =1; i<=15; i++) {
        manager.getTaskById(task.getTaskId());
        manager.getSubTaskById(subTask.getTaskId());
        manager.getEpicById(epic.getTaskId());
    }
    assertEquals(10,viewHistory.size(),"Не больше 10 тасок");
    }

    @Test //просмотренные задачи должны добавляться в конец.
    void taskShoulBeAddedInTheEnd()  {
        manager.createTask(task);
        Epic epic  = new Epic ("Новый епик","для проверки",Status.NEW);
        manager.createEpic(epic);
        manager.getEpicById(epic.getTaskId());
        manager.getTaskById(task.getTaskId());
        assertEquals(task.getTaskId(),viewHistory.get(1).getTaskId(),"ID не совпали");
    }

    @Test//задачи с заданным id и сгенерированным id не конфликтуют;
    void TasksShouldBeEqualsNoMetterHowIdCreated() {
        Task task1 = new Task("Имя","Фамилия", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Имя","Фамилия", Status.NEW);
        task2.setTaskId(task1.getTaskId());
        assertEquals(task1,task2,"Объекты не равны");
    }

}