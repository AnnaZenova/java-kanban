package service;

import exceptions.ManagerValidateException;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends InMemoryTaskManager> {

    protected T managerTask;
    protected Task task;
    protected Task task1;
    protected Epic epic;
    protected Epic epic1;
    protected SubTask subTask;
    protected SubTask subTask1;
    protected Task task2;

    @BeforeEach
    @DisplayName("создать объекты/экземпляры")
    void shouldCreateObjects() {
        this.task = new Task("Новая таска", "для проверки", Status.NEW, LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
        this.epic = new Epic("Новый епик", "для проверки", Status.NEW);
        epic1 = new Epic("Новый епик", "для проверки", Status.NEW);
        subTask = new SubTask("Новая cабтаска", "для проверки", Status.NEW, epic.getTaskId(), LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
        task1 = new Task("Новая таска", "для проверки", Status.NEW, LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
        task2 = new Task("Имя", "Фамилия", Status.NEW, LocalDateTime.of(2021, 11, 3, 17, 55), Duration.ofHours(10));
        subTask1 = new SubTask("Новая cабтаска", "для проверки", Status.NEW, epic.getTaskId(), LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
    }

    @Test
    @DisplayName("Проверка создания тикета task")
    void shouldCreateNewTask() {
        Task task3 = managerTask.createTask(this.task);
        final Task createdTask = managerTask.getTaskById(task3.getTaskId());
        assertEquals(task3, createdTask, "Не удалось создать задачу");
    }

    @Test
    @DisplayName("Проверка создания тикета subtask")
    void shouldCreateNewSubTask() {
        Epic epic0 = managerTask.createEpic(this.epic);
        SubTask subTask0 = managerTask.createSubTask(this.subTask);
        SubTask createdSubTask = managerTask.getSubTaskById(subTask0.getTaskId());
        assertEquals(this.subTask, createdSubTask, "Не удалось создать задачу");
    }

    @Test
    @DisplayName("Проверка создания тикета epic")
    void shouldCreateNewEpic() {
        Epic epic5 = managerTask.createEpic(this.epic);
        final Epic createdEpic = managerTask.getEpicById(epic5.getTaskId());
        assertEquals(epic, createdEpic, "Не удалось создать задачу");
    }

    @Test
    @DisplayName("Проверка удаления тикета task")
    void shouldRemoveTask() {
        Task task3 = managerTask.createTask(this.task);
        Map<Integer, Task> tasks = new HashMap<>();
        tasks.put(0, task3);
        assertEquals(1, tasks.size());
        tasks.remove(task3.getTaskId());
        assertEquals(0, tasks.size(), "Ошибка удаления");
    }

    @Test
    @DisplayName("Проверка обновления тикета Task")
    void shouldUpdateTasks() {
        managerTask.createTask(this.task);
        this.task.setStatus(Status.DONE);
        managerTask.updateTask(this.task);
        assertEquals(Status.DONE, this.task.getStatus(), "Не удалось обновить задачу");
    }

    @Test
    @DisplayName("Проверка обновления тикета SubTask")
    void shouldUpdateSubtasks() {
        managerTask.createEpic(this.epic);
        managerTask.createSubTask(this.subTask);
        this.subTask.setStatus(Status.DONE);
        managerTask.updateTask(this.subTask);
        assertEquals(Status.DONE, this.subTask.getStatus(), "Не удалось обновить задачу");
    }

    @Test
    @DisplayName("Проверка наложения интервалов времени")
    void shouldReturnFalseIfOverlap() {
        managerTask.createEpic(this.epic);
        managerTask.createSubTask(this.subTask);
        assertThrows(ManagerValidateException.class, () -> managerTask.createEpic(this.epic1), "Добавляемая задача не прошла валидацию");
        assertThrows(ManagerValidateException.class, () -> managerTask.createSubTask(this.subTask1), "Добавляемая задача не прошла валидацию");
        assertThrows(ManagerValidateException.class, () -> managerTask.createTask(this.task1), "Добавляемая задача не прошла валидацию");
        assertFalse(managerTask.checkOverlaps(this.task1));
        assertFalse(managerTask.checkOverlaps(this.epic1));
        assertFalse(managerTask.checkOverlaps(this.subTask1));
    }
}
