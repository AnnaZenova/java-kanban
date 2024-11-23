package http;
import java.io.IOException;

import http.handlers.*;
import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;
import status.Status;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final String PATH_FOR_TASKS = "/tasks";
    private static final String PATH_FOR_SUBTASKS = "/subtasks";
    private static final String PATH_FOR_EPICS = "/epics";
    private static final String PATH_FOR_HISTORY = "/history";
    private static final String PATH_FOR_PRIORITIZED = "/prioritized";
    private static HttpServer httpServer;
    private final Gson gson;
    private static HistoryManager historyManager = Managers.getDefaultHistory();
    private static TaskManager taskManager = Managers.getDefault(historyManager);

    public static void main(String[] args) throws IOException {
        Task task = new Task("Test1", "Test2", Status.IN_PROGRESS, LocalDateTime.of(2222, 01, 01, 00, 00), Duration.ofHours(50));
        task.getCalculatedEndTime(task.getStartTime(), task.getDuration());
        taskManager.createTask(task);
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext(PATH_FOR_TASKS, new TasksHandler(taskManager));
        httpServer.createContext(PATH_FOR_SUBTASKS, new SubTaskHandler(taskManager));
        httpServer.createContext(PATH_FOR_EPICS, new EpicHandler(taskManager));
        httpServer.createContext(PATH_FOR_HISTORY, new HistoryHandler(taskManager));
        httpServer.createContext(PATH_FOR_PRIORITIZED, new PrioritizedHandler(taskManager));
        httpServer.start();
    }

    public HttpTaskServer(int port, TaskManager taskManager) throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        HttpTaskServer.taskManager = taskManager;
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(port), 0);
        httpServer.createContext(PATH_FOR_TASKS, new TasksHandler(taskManager));
        httpServer.createContext(PATH_FOR_SUBTASKS, new SubTaskHandler(taskManager));
        httpServer.createContext(PATH_FOR_EPICS, new EpicHandler(taskManager));
        httpServer.createContext(PATH_FOR_HISTORY, new HistoryHandler(taskManager));
        httpServer.createContext(PATH_FOR_PRIORITIZED, new PrioritizedHandler(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }
}

