package service;

import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import status.Status;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager manager = Managers.getDefault();
    Task task;
    Epic epic;

    @BeforeEach
    @DisplayName("создать объекты/экземпляры")
    void shouldCreateObjects() {
        task = new Task("Новая таска", "для проверки", Status.NEW);
        epic = new Epic("Новый епик", "для проверки", Status.NEW);
    }

    @Test
    @DisplayName("проверяем добавление по add")
    void shouldAddNewTaskToStory() {
        manager.createTask(task);
        manager.createEpic(epic);
        manager.getEpicById(epic.getTaskId());
        manager.getTaskById(task.getTaskId());
        assertEquals(epic, historyManager.getHistory().get(0), "Объекты должны совпасть");
        assertEquals(task, historyManager.getHistory().get(1), "Объекты должны совпасть");
    }
}

