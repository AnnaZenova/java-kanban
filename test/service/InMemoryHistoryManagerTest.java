package service;

import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager manager = Managers.getDefault();
    Task task;
    Epic epic;

    @BeforeEach
    @DisplayName("создать объекты/экземпляры")
    void shouldCreateObjects() {
        task = new Task("Новая таска", "для проверки", Status.NEW, LocalDateTime.of(1999, 11, 3, 17,55), Duration.ofHours(10));
        epic = new Epic("Новый епик", "для проверки", Status.NEW, LocalDateTime.of(1998, 11, 3, 17,55), Duration.ofHours(10));
    }

    @Test
    @DisplayName("проверяем добавление по add")
    void shouldAddNewTaskToStory() {
        manager.createTask(task);
        manager.getTaskById(task.getTaskId());
        manager.createEpic(epic);
        manager.getEpicById(epic.getTaskId());
        System.out.println(manager.getHistory());
        assertEquals(epic, manager.getHistory().get(1), "Объекты должны совпасть");
        assertEquals(task, manager.getHistory().get(0), "Объекты должны совпасть");
    }
}

