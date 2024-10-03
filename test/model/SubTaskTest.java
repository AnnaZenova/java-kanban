package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;
import status.Status;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest extends InMemoryTaskManager {
    TaskManager manager = Managers.getDefault();
    Epic epic;
    SubTask subTask;

    @BeforeEach
    @DisplayName("создать объекты/экземпляры")
    void shouldCreateEpicsAndTasksAndSubTasks() {
        epic = new Epic("name", "desc", Status.NEW);
        subTask = new SubTask("Новый епик", "новый епик", Status.IN_PROGRESS, epic.getTaskId());
    }

    @Test
    @DisplayName("проверяем получение id")
    void getEpicId() {
        SubTask subTask1 = new SubTask("Новый епик", "новый епик", Status.IN_PROGRESS, 1);
        assertEquals(1, subTask1.getEpicId(), "ID не совпадают");

    }

    @Test
    @DisplayName("проверяем установку id")
    void setEpicId() {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        subTask.setEpicId(1);
        assertEquals(1, subTask.getEpicId(), "ID не совпадают");

    }
}