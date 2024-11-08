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

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    Task task;
    Task task1;
    Epic epic;
    Epic epic1;
    SubTask subTask;
    SubTask subTask1;
    HistoryManager historyManager = Managers.getDefaultHistory();
    Task task2;

    @BeforeEach
    @DisplayName("создать объекты/экземпляры")
    void shouldCreateObjects() {
        task = new Task("Новая таска", "для проверки", Status.NEW, LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
        epic = new Epic("Новый епик", "для проверки", Status.NEW, LocalDateTime.of(2023, 11, 3, 17, 55), Duration.ofHours(10));
        epic1 = new Epic("Новый епик", "для проверки", Status.NEW, LocalDateTime.of(2023, 11, 3, 17, 55), Duration.ofHours(10));
        subTask = new SubTask("Новая cабтаска", "для проверки", Status.NEW, epic.getTaskId(), LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
        task1 = new Task("Новая таска", "для проверки", Status.NEW, LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
        task2 = new Task("Имя", "Фамилия", Status.NEW, LocalDateTime.of(2021, 11, 3, 17, 55), Duration.ofHours(10));
        subTask1 = new SubTask("Новая cабтаска", "для проверки", Status.NEW, epic.getTaskId(), LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
    }

    @Test
    @DisplayName("Проверка создания тикета task")
    void shouldCreateNewTask() {
        manager.createTask(task);
        final Task createdTask = manager.getTaskById(task.getTaskId());
        assertEquals(task, createdTask, "Не удалось создать задачу");
    }

    @Test
    @DisplayName("Проверка создания тикета subtask")
    void shouldCreateNewSubTask() {
        manager.createTask(subTask);
        final SubTask createdSubTask = manager.getSubTaskById(subTask.getTaskId());
        assertEquals(subTask, createdSubTask, "Не удалось создать задачу");
    }

    @Test
    @DisplayName("Проверка создания тикета epic")
    void shouldCreateNewEpic() {
        manager.createTask(epic);
        final Epic createdEpic = manager.getEpicById(epic.getTaskId());
        assertEquals(epic, createdEpic, "Не удалось создать задачу");
    }

    @Test
    @DisplayName("Проверка удаления тикета task")
    void shouldRemoveTask() {
        manager.createTask(task);
        Map<Integer, Task> tasks = new HashMap<>();
        tasks.put(0, task);
        assertEquals(1, tasks.size());
        manager.removeTaskById(task.getTaskId());
        assertEquals(0, tasks.size(), "Ошибка удаления");
    }

    @Test
    @DisplayName("Проверка удаления тикета subtask")
    void shouldRemoveSubTask() {
        manager.createSubTask(subTask);
        Map<Integer, SubTask> subTasks = new HashMap<>();
        subTasks.put(0, subTask);
        assertEquals(1, subTasks.size());
        manager.removeSubTaskById(task.getTaskId());
        assertEquals(0, subTasks.size(), "Ошибка удаления");
    }

    @Test

    @DisplayName("Проверка удаления тикета epic")
    void shouldRemoveEpic() {
        manager.createTask(epic);
        Map<Integer, Epic> tasks = new HashMap<>();
        tasks.put(0, epic);
        assertEquals(1, tasks.size());
        manager.removeEpicById(epic.getTaskId());
        assertEquals(0, tasks.size(), "Ошибка удаления");
    }

    @Test
    @DisplayName("Проверка обновления тикета Task")
    void shouldUpdateTasks() {
        manager.createTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, task.getStatus(), "Не удалось обновить задачу");
    }

    @Test
    @DisplayName("Проверка обновления тикета SubTask")
    void shouldUpdateSubtasks() {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        subTask.setStatus(Status.DONE);
        manager.updateTask(subTask);
        assertEquals(Status.DONE, subTask.getStatus(), "Не удалось обновить задачу");
        assertEquals(Status.DONE, epic.getStatus(), "Не удалось обновить задачу");
    }

    @Test
    @DisplayName("Проверка наложения интервалов времени")
    void shoulReturnFalseIfOverlap() {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.createTask(task);
        assertThrows(ManagerValidateException.class, () -> manager.createEpic(epic1), "Добавляемая задача не прошла валидацию");
        assertThrows(ManagerValidateException.class, () -> manager.createSubTask(subTask1), "Добавляемая задача не прошла валидацию");
        assertThrows(ManagerValidateException.class, () -> manager.createTask(task1), "Добавляемая задача не прошла валидацию");
        assertFalse(manager.checkOverlaps(task1));
        assertFalse(manager.checkOverlaps(epic1));
        assertFalse(manager.checkOverlaps(subTask1));
    }
}
