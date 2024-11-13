package service;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import  java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        managerTask = new InMemoryTaskManager();
    }

    TaskManager manager = Managers.getDefault();
    Task task;
    Task task1;
    Epic epic;
    SubTask subTask;
    SubTask subTask1;
    HistoryManager historyManager = Managers.getDefaultHistory();
    Task task2;

    @BeforeEach
    @DisplayName("создать объекты/экземпляры")
    void CreateObjects() {
        task = new Task("Новая таска", "для проверки", Status.NEW, LocalDateTime.of(2024, 11, 3, 17,55), Duration.ofHours(10));
        epic = new Epic("Новый епик", "для проверки", Status.NEW);
        subTask = new SubTask("Новая cабтаска", "для проверки", Status.NEW,1, LocalDateTime.of(2024, 11, 4, 17,55), Duration.ofHours(10));
        task1 = new Task("Имя", "Фамилия", Status.NEW,LocalDateTime.of(2022, 11, 3, 17,55), Duration.ofHours(10));
        task2 = new Task("Имя", "Фамилия", Status.NEW,LocalDateTime.of(2021, 11, 3, 17,55), Duration.ofHours(10));
        subTask1 = new SubTask("Новая cабтаска", "для проверки", Status.NEW,1, LocalDateTime.of(2029, 11, 3, 17,55), Duration.ofHours(10));
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

    @Test
    @DisplayName("корректный расчет статуса Epic, если все подзадачи в NEW")
    void shouldBeCorrectEpicStatusNEW() {
    Epic epic0 = new Epic("Epic", "status check", Status.IN_PROGRESS);
    manager.createEpic(epic0);
    SubTask subtask3 = new SubTask("SubTAsk", "status check", Status.NEW,epic0.getTaskId(), LocalDateTime.of(2025,11,3,17,55), Duration.ofHours(10));
    SubTask subtask4 = new SubTask("SubTask", "status check", Status.NEW,epic0.getTaskId(), LocalDateTime.of(2020,11,3,17,55), Duration.ofHours(10));
    manager.createSubTask(subtask3);
    manager.createSubTask(subtask4);
    assertEquals(Status.NEW,epic0.getStatus(),"У епика рассчитан некорректный статус");
    }

    @Test
    @DisplayName("корректный расчет статуса Epic, если все подзадачи в DONE")
    void shouldBeCorrectEpicStatusDONE() {
        Epic epic = new Epic("Epic", "status check", Status.NEW);
        manager.createEpic(epic);
        SubTask subtask5 = new SubTask("Epic", "status check", Status.DONE, epic.getTaskId(), LocalDateTime.of(1099,11,3,17,55), Duration.ofHours(10));
        SubTask subtask6 = new SubTask("Epic", "status check", Status.DONE,epic.getTaskId(), LocalDateTime.of(2000,11,3,17,55), Duration.ofHours(10));
        manager.createSubTask(subtask5);
        manager.createSubTask(subtask6);
        assertEquals(Status.DONE,epic.getStatus(),"У епика рассчитан некорректный статус");
    }

    @Test
    @DisplayName("корректный расчет статуса Epic, если есть подзадачи в DONE и NEW")
    void shouldBeCorrectEpicStatusINPROGRESS() {
        Epic epic = new Epic("Epic", "status check", Status.NEW);
        manager.createEpic(epic);
        SubTask subtask5 = new SubTask("Epic", "status check", Status.NEW, epic.getTaskId(), LocalDateTime.of(1099,11,3,17,55), Duration.ofHours(10));
        SubTask subtask6 = new SubTask("Epic", "status check", Status.DONE,epic.getTaskId(), LocalDateTime.of(2000,11,3,17,55), Duration.ofHours(10));
        manager.createSubTask(subtask5);
        manager.createSubTask(subtask6);
        assertEquals(Status.IN_PROGRESS,epic.getStatus(),"У епика рассчитан некорректный статус");
    }

    @Test
    @DisplayName("корректный расчет статуса Epic, если подзадачи в статусе IN_PROGRESS")
    void shouldBeCorrectEpicStatusIN_PROGRESS() {
        Epic epic = new Epic("Epic", "status check", Status.NEW);
        manager.createEpic(epic);
        SubTask subtask5 = new SubTask("Epic", "status check", Status.IN_PROGRESS, epic.getTaskId(), LocalDateTime.of(1099,11,3,17,55), Duration.ofHours(10));
        SubTask subtask6 = new SubTask("Epic", "status check", Status.IN_PROGRESS,epic.getTaskId(), LocalDateTime.of(2000,11,3,17,55), Duration.ofHours(10));
        manager.createSubTask(subtask5);
        manager.createSubTask(subtask6);
        assertEquals(Status.IN_PROGRESS,epic.getStatus(),"У епика рассчитан некорректный статус");
    }
}

