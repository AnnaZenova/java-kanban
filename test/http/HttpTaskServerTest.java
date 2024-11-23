package http;
import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import exceptions.NotFoundException;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import status.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(8080, manager);
    HttpClient client;
    Task task;
    Task taskN;
    Gson gson;
    Epic epic;
    SubTask subTask;
    SubTask subTaskN;
    public static final String TASK_URL = "http://localhost:8080/tasks";
    public static final String TASK_BY_ID_URL = "http://localhost:8080/tasks/0";
    public static final String EPIC_URL = "http://localhost:8080/epics/";
    public static final String EPIC_BY_ID_URL = "http://localhost:8080/epics/0";
    public static final String EPICS_SUBTASKS_BY_ID_URL = "http://localhost:8080/epics/0/subtasks";
    public static final String PRIORITIZED_URL = "http://localhost:8080/prioritized";
    public static final String HISTORY_URL = "http://localhost:8080/history";

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTaskList();
        manager.deleteSubTaskList();
        manager.deleteEpicList();
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        taskServer.start();
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1).build();

        task = new Task("Test1", "Test2", Status.IN_PROGRESS, LocalDateTime.of(2222, 01, 01, 00, 00), Duration.ofHours(50));
        task.getCalculatedEndTime(task.getStartTime(), task.getDuration());
        taskN = new Task(-1, "Test1", "Test2", Status.IN_PROGRESS, LocalDateTime.of(2222, 01, 01, 00, 00), Duration.ofHours(50));
        taskN.getCalculatedEndTime(task.getStartTime(), task.getDuration());
        epic = new Epic("Epic", "status check", Status.NEW);
        manager.calculateEpicStartTimeAndDurationAndEndTime(epic);
        subTask = new SubTask("Epic", "status check", Status.NEW, epic.getTaskId(), LocalDateTime.of(1099, 11, 3, 17, 55), Duration.ofHours(10));
        subTaskN = new SubTask("Epic", "status check", Status.DONE, epic.getTaskId(), LocalDateTime.of(2000, 11, 3, 17, 55), Duration.ofHours(10));
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }


    @Test
    @DisplayName("Проверка получения списка тасков")
    public void shouldGetTaskList() throws IOException, InterruptedException, NotFoundException {
        manager.createTask(task);
        URI url = URI.create(TASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .GET()
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа
        assertEquals(200, response.statusCode(), "Код ответа отличается");
        Task[] body = gson.fromJson(response.body(), Task[].class);

        // проверяем, что создалась одна задача с корректным именем
        final List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        assertEquals(manager.getAllTaskList(), tasks, "Некорректное имя задачи");
    }

    @Test
    @DisplayName("Проверка добавления таска")
    public void testAddTask() throws IOException, InterruptedException, NotFoundException {

        URI url = URI.create(TASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(taskN)))
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа

        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTaskList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    @DisplayName("Проверка обновления таска")
    public void testUpdateTask() throws IOException, InterruptedException, NotFoundException {
        manager.createTask(task);
        URI url = URI.create(TASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа
        assertEquals(201, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasks = manager.getAllTaskList();
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Некорректное количество задач");
        assertEquals("Test1", tasks.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    @DisplayName("Проверка удаления таска")
    public void testDeleteTask() throws IOException, InterruptedException, NotFoundException {
        manager.createTask(task);
        URI url = URI.create(TASK_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .DELETE()
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа
        assertEquals(201, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasks = manager.getAllTaskList();
        assertEquals(0, tasks.size(), "Некорректное количество задач");
    }

    @Test
    @DisplayName("Проверка получения епика по ID")
    public void testGetTaskById() throws IOException, InterruptedException, NotFoundException {
        manager.createTask(task);
        URI url = URI.create(TASK_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .GET()
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasks = manager.getAllTaskList();
        assertEquals(1, tasks.size(), "Некорректное количество задач");
    }

    @Test
    @DisplayName("Проверка получения списка епиков")
    public void shouldGetEpicList() throws IOException, InterruptedException, NotFoundException {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.calculateEpicStartTimeAndDurationAndEndTime(epic);
        System.out.println(epic);
        URI url = URI.create(EPIC_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .GET()
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа
        assertEquals(200, response.statusCode(), "Код ответа отличается");
        // проверяем, что создалась одна задача с корректным именем
        final List<Epic> epics = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        assertEquals(manager.getAllEpicList(), epics, "Некорректное имя задачи");
    }

    @Test
    @DisplayName("Проверка получения епика по ID")
    public void testGETEpicById() throws IOException, InterruptedException, NotFoundException {
        URI url = URI.create(EPIC_BY_ID_URL);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.calculateEpicStartTimeAndDurationAndEndTime(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .GET()
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа

        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Epic> epics = manager.getAllEpicList();
        assertEquals(1, epics.size(), "Некорректное количество задач");
        assertEquals(epic, epics.get(0), "Разные епики");
    }

    @Test
    @DisplayName("Проверка получения сабтасков епика с ID")
    public void testEpicSubTasks() throws IOException, InterruptedException, NotFoundException {
        URI url = URI.create(EPICS_SUBTASKS_BY_ID_URL);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.calculateEpicStartTimeAndDurationAndEndTime(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .GET()
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        final List<SubTask> subTasks = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {
        }.getType());
        assertEquals(1, subTasks.size(), "Некорректное количество задач");
        assertEquals(subTask, subTasks.get(0), "Разные сабтаски");
    }


    @Test
    @DisplayName("Проверка получения истории")
    public void testGetHistory() throws IOException, InterruptedException, NotFoundException {
        URI url = URI.create(HISTORY_URL);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .GET()
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        final List<SubTask> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {
        }.getType());
        assertEquals(manager.getHistory().get(0), tasks.get(0), "Первый элемент истории отличается");
        final List<Epic> epics = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        assertEquals(manager.getHistory().get(1), epics.get(1), "Второй элемент истории отличается");
        assertEquals(2, tasks.size(), "Некорректное количество задач");
    }

    @Test
    @DisplayName("Проверка получения приоритезированного списка")
    public void testGetPrioritized() throws IOException, InterruptedException, NotFoundException {
        URI url = URI.create(PRIORITIZED_URL);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.createTask(task);
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .GET()
                .header("Accept", "text/json")
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, bodyHandler);
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        final List<SubTask> subTasks = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {
        }.getType());

        assertTrue(manager.getPrioritizedTasks().contains(subTasks.get(0)), "Первый элемент истории отличается");
        assertEquals(manager.getPrioritizedTasks().size(), subTasks.size(), "Некорректное количество задач");
    }
}

