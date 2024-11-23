package http.handlers;
import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());

        switch (endpoint) {
            case "GET": //tasks - getTasks
                getAllTaskList(httpExchange);
            case "GET_with_id"://tasks/{id} - getTaskById
                getTaskById(httpExchange);
            case "POST": //tasks - createTask(task)/orUpdate
                createOrUpdateTask(httpExchange);
            case "DELETE": //tasks/{id} - deleteTask(id)
                deleteTaskById(httpExchange);
            case "UNKNOWN": //ошибка
                sendText(httpExchange, "Path not found");
        }
    }

    private void getAllTaskList(HttpExchange httpExchange) throws IOException {
        try {
            String jsonAllTaskList = gson.toJson(taskManager.getAllTaskList());
            sendText(httpExchange, jsonAllTaskList);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "Tasklist not found");
        }
    }

    private void getTaskById(HttpExchange httpExchange) throws IOException {
        try {
            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[2]);
            String task = gson.toJson(taskManager.getTaskById(id));
            sendText(httpExchange, task);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "Task not found by id");
        }
    }


    private void createOrUpdateTask(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestBody()==null) {
            sendNotFound(httpExchange, "No request body was found");
        }
        boolean isNewTask = true;
        InputStream is = httpExchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        if (body.equals("")) {
            sendNotFound(httpExchange, "Body is empty");
        }
        Task task = gson.fromJson(body, Task.class);
        try {
            for (Task taskToMatchIds : taskManager.getAllTaskList()) {
                int id = taskToMatchIds.getTaskId();
                if (id == task.getTaskId()) {
                    isNewTask = false;
                }
            }
            if (isNewTask) {
                taskManager.createTask(task);
                sendText(httpExchange, gson.toJson(task));
            }
            if (!isNewTask) {
                taskManager.updateTask(task);
                sendOnlyCode(httpExchange);
            }
        } catch (Exception exception) {
            sendHasInteractions(httpExchange, "Found tasks overlaps");
        }
    }

    private void deleteTaskById(HttpExchange httpExchange) throws IOException {
        try {
            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[2]);
            taskManager.removeTaskById(id);
            sendOnlyCode(httpExchange);
        } catch (NotFoundException exception) {
            sendNotFound(httpExchange, "Invalid id");
        }
    }

    private String getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && requestMethod.equals("GET")) {
            return "GET";
        }
        if (pathParts.length == 3 && requestMethod.equals("GET")) {
            return "GET_with_id";
        }

        if (requestMethod.equals("POST")) {
            return "POST";
        }
        if (requestMethod.equals("DELETE")) {
            return "DELETE";
        }

        return "UNKNOWN";
    }
}


