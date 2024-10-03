package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import service.InMemoryTaskManager;
import service.TaskManager;
import service.Managers;
import status.Status;


class EpicTest extends InMemoryTaskManager {
    TaskManager manager = Managers.getDefault();
    Epic epic;
    Epic epicExpected;
    SubTask subTask;

    @BeforeEach
    @DisplayName("создать объекты/экземпляры")
    void shouldCreateEpicsAndTasksAndSubTasks() {
        epic = new Epic("name", "desc", Status.NEW);
        epicExpected = new Epic("name1", "desc", Status.NEW);
        subTask = new SubTask("Новый епик", "новый епик", Status.IN_PROGRESS, epic.getTaskId());
    }

    @Test
    @DisplayName("проверяем добавляет ли в лист сабтасков новый ID")
    void shouldAddSubTuskToList() {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        assertEquals(1, epic.subTaskIdList.size(), "Список сабтасков пустой");
    }
}