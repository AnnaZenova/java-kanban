package API;
import java.io.IOException;

import API.Handlers.*;
import Adapters.DurationAdapter;
import Adapters.LocalDateTimeAdapter;
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
    HttpServer httpServer;
    private final Gson gson;
    static HistoryManager historyManager = Managers.getDefaultHistory();
    static TaskManager taskManager = Managers.getDefault(historyManager);
    private static final String pathForTasks = "/tasks";
    private static final String pathForSubTasks = "/subtasks";
    private static final String pathForEpics = "/epics";
    private static final String pathForHistory = "/history";
    private static final String pathForPrioritized = "/prioritized";


    public static void main(String[] args) throws IOException {
        Task task = new Task("Test1", "Test2", Status.IN_PROGRESS, LocalDateTime.of(2222, 01, 01, 00, 00), Duration.ofHours(50));
        task.getCalculatedEndTime(task.getStartTime(), task.getDuration());
        taskManager.createTask(task);
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext(pathForTasks, new Tasks_Handler(taskManager));
        httpServer.createContext(pathForSubTasks, new SubTask_Handler(taskManager));
        httpServer.createContext(pathForEpics, new Epic_Handler(taskManager));
        httpServer.createContext(pathForHistory, new History_Handler(taskManager));
        httpServer.createContext(pathForPrioritized, new Prioritized_Handler(taskManager));
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
        httpServer.createContext(pathForTasks, new Tasks_Handler(taskManager));
        httpServer.createContext(pathForSubTasks, new SubTask_Handler(taskManager));
        httpServer.createContext(pathForEpics, new Epic_Handler(taskManager));
        httpServer.createContext(pathForHistory, new History_Handler(taskManager));
        httpServer.createContext(pathForPrioritized, new Prioritized_Handler(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }
}

