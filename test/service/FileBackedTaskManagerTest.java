package service;
import static org.junit.jupiter.api.Assertions.*;

import exceptions.ManagerSaveException;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import status.Status;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTasksManagerTest extends FileBackedTaskManager {

    @Test
    @DisplayName("Проверка исключения")
    void shouldThrowException() {
        File file = new File("C:\\Users\\fored\\first-project\\tests.txt");
        Task task = new Task("Новая таска", "для проверки", Status.NEW, LocalDateTime.of(2024, 11, 3, 17, 55), Duration.ofHours(10));
        assertDoesNotThrow(() -> loadFromFile(file), "Исключение выброшено");
    }
}
