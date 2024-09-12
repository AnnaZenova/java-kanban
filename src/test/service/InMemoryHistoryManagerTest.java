package service;

import model.Epic;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import status.Status;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager manager = Managers.getDefault();
    Task task = new Task("Новая таска", "для проверки", Status.NEW);

    @Test
    @DisplayName("проверяем добавление по add")
    void shouldAddNewTaskToStory() {
        manager.createTask(task);
        Epic epic = new Epic("Новый епик", "для проверки", Status.NEW);
        manager.createEpic(epic);
        manager.getEpicById(epic.getTaskId());
        manager.getTaskById(task.getTaskId());
        assertEquals(epic, historyManager.viewHistory.get(0), "Объекты должны совпасть");
        assertEquals(task, historyManager.viewHistory.get(1), "Объекты должны совпасть");
    }
}

