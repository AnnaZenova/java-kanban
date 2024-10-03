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
        subTask = new SubTask("Новая cабтаска", "для проверки", Status.NEW, 1);
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
    @DisplayName("в историю не попадают повторы")
    void shouldBeNoDublicates() {
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        for (int i = 1; i <= 15; i++) {
            manager.getTaskById(task.getTaskId());
            manager.getSubTaskById(subTask.getTaskId());
            manager.getEpicById(epic.getTaskId());
        }
        assertEquals(3, manager.getHistory().size(), "Дубликаты есть");
    }

    @Test
    @DisplayName("история содержит больше задач")
    void shouldContainMoreTheniTasks() {

        for (int i = 1; i <= 15; i++) {
            Task a = new Task("Новая таска" + i, "Описание таски" + i, Status.NEW);
            manager.createTask(a);
            manager.getTaskById(a.getTaskId());
        }
        assertEquals(15, manager.getHistory().size(), "История содержит задач больше/меньше заданного i");
    }

    @Test
    @DisplayName("просмотренные задачи должны добавляться в конец")
    void shouldTaskBeAddedInTheEnd() {
        manager.createTask(task);
        manager.createEpic(epic);
        manager.getEpicById(epic.getTaskId());
        manager.getTaskById(task.getTaskId());
        assertEquals(task.getTaskId(), manager.getHistory().get(1).getTaskId(), "ID не совпали");
    }

    @Test
    @DisplayName("операция добавления работает")
    void ShouldAddTicketToHistory() {
        manager.createTask(task);
        manager.getTaskById(task.getTaskId());
        manager.createEpic(epic);
        manager.getEpicById(epic.getTaskId());
        System.out.println(manager.getHistory());
        assertEquals(epic, manager.getHistory().get(1), "Объекты должны совпасть");
        assertEquals(task, manager.getHistory().get(0), "Объекты должны совпасть");
    }

    @Test
//задачи с заданным id и сгенерированным id не конфликтуют;
    void shouldTasksBeEqualsNoMetterHowIdCreated() {
        manager.createTask(task1);
        task2.setTaskId(task1.getTaskId());
        assertEquals(task1, task2, "Объекты не равны");
    }
}

