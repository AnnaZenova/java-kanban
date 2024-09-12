package model;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import service.InMemoryTaskManager;
import service.TaskManager;
import service.Managers;
import status.Status;


class EpicTest extends InMemoryTaskManager {
    TaskManager manager = Managers.getDefault();

    @Test
    @DisplayName("проверяем совпадает ли с копией")
    void shouldEqualsWithCopy() {
        Epic epic = new Epic("name","desc", Status.NEW);
        Epic epicExpected = new Epic("name1", "desc", Status.NEW);
        assertEquals(epicExpected, epic, "Эпики должны совпадать");

    }

    @Test
    @DisplayName("проверяем добавляет ли в лист сабтасков новый ID")
    void shouldAddSubTuskToList() {
    Epic epic = new Epic("Новый епик","новый епик",Status.IN_PROGRESS);
    SubTask subTask = new SubTask("Новый епик","новый епик",Status.IN_PROGRESS, epic.getTaskId());
    manager.createEpic(epic);
    manager.createSubTask(subTask);
    assertEquals(0,epic.subTaskIdList.size(),"Список сабтасков пустой");
    }
}