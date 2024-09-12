package service;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import status.Status;
import  java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager manager = Managers.getDefault();
    Task task;
    Task task1;
    Epic epic;
    SubTask subTask;
    HistoryManager historyManager = Managers.getDefaultHistory();
    Task task2;

    @BeforeEach
    @DisplayName("создать объекты/экземпляры")
    void shouldCreateObjects() {
        task = new Task("Новая таска", "для проверки", Status.NEW);
        epic = new Epic("Новый епик", "для проверки", Status.NEW);
        subTask = new SubTask("Новая cабтаска", "для проверки", Status.NEW, epic.getTaskId());
        task1 = new Task("Имя", "Фамилия", Status.NEW);
        task2 = new Task("Имя", "Фамилия", Status.NEW);
    }

    @Test
    @DisplayName("что экземпляры класса равны друг другу, если равен ID")
    void shouldCreateEqualTasksById() {
        manager.createTask(task);
        Task task1 = manager.getTaskById(task.getTaskId());
        Task task2 = manager.getTaskById(task.getTaskId());
        assertEquals(task1, task2);
    }

    @Test
    @DisplayName("что экземпляры класса равны друг другу, если равен ID")
    void shouldCreateEqualSubTasksById() {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        SubTask subTask1 = manager.getSubTaskById(subTask.getTaskId());
        SubTask subTask2 = manager.getSubTaskById(subTask.getTaskId());
        assertEquals(subTask1, subTask2);
    }

    @Test
    @DisplayName("что экземпляры класса равны друг другу, если равен ID")
    void shouldCreateEqualEpicsById() {
        manager.createEpic(epic);
        Epic epic1 = manager.getEpicById(epic.getTaskId());
        Epic epic2 = manager.getEpicById(epic.getTaskId());
        assertEquals(epic1, epic2);
    }

    @Test
    @DisplayName("что в историю добавляются значения")
    void historyShouldNotBeNull() {
        historyManager.add(task);
        final List<Task> viewHistory1 = historyManager.getHistory();
        assertNotNull(viewHistory1, "История не пустая.");
        assertEquals(1, viewHistory1.size(), "История не пустая.");
    }

    @Test
    @DisplayName("епик не добавится в себя как сабтаск")
    void epicShouldNotBeAddedAsASubTask() {
        manager.createEpic(epic);
        subTask.setTaskId(epic.getTaskId());
        manager.updateSubTask(subTask);
        assertEquals(0, epic.subTaskIdList.size(), "В список id сабтасок не попал новый id");
    }

    @Test
    @DisplayName("сабтаск не станет своим же епиком")
    void subTaskShouldNotBeAddedAsAnEpic() {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        subTask.setEpicId(subTask.getTaskId());
        manager.getEpicById(subTask.getTaskId());
        assertNull(manager.getEpicById(subTask.getTaskId()), "Такого епика с таким ID больше нет");
    }


    @Test //getHistory возвращает 10 задач (ни больше ни меньше)
    @DisplayName("getHistory возвращает 10 задач (ни больше ни меньше)")
    void shouldBeNotMoreThen10Tasks() {
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubTask(subTask);

        for (int i = 1; i <= 15; i++) {
            manager.getTaskById(task.getTaskId());
            manager.getSubTaskById(subTask.getTaskId());
            manager.getEpicById(epic.getTaskId());
        }
        assertEquals(10, historyManager.getHistory().size(), "Не больше 10 тасок");
    }

    @Test
    @DisplayName("просмотренные задачи должны добавляться в конец")
    void shouldTaskBeAddedInTheEnd() {
        manager.createTask(task);
        manager.createEpic(epic);
        manager.getEpicById(epic.getTaskId());
        manager.getTaskById(task.getTaskId());
        assertEquals(task.getTaskId(), historyManager.getHistory().get(1).getTaskId(), "ID не совпали");
    }

    @Test
//задачи с заданным id и сгенерированным id не конфликтуют;
    void shouldTasksBeEqualsNoMetterHowIdCreated() {
        manager.createTask(task1);
        task2.setTaskId(task1.getTaskId());
        assertEquals(task1, task2, "Объекты не равны");
    }
}

