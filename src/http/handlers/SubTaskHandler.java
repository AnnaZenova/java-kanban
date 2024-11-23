package http.handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskHandler extends BaseHttpHandler {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());

        switch (endpoint) {
            case "GET": //subtasks - getSubTasks
                getAllSubTaskList(httpExchange);
            case "GET_with_id"://subtasks/{id} - getSubTaskById
                getSubTaskById(httpExchange);
            case "POST": //subtasks - createSubTask(task)/orUpdate
                createOrUpdateSubTask(httpExchange);
            case "DELETE": //subtasks/{id} - deleteSubTask(id)
                deleteSubTaskById(httpExchange);
            case "UNKNOWN": //ошибка
                sendText(httpExchange, "Path not found");
        }
    }

    private void getAllSubTaskList(HttpExchange httpExchange) throws IOException {
        try {
            String jsonAllSubTaskList = gson.toJson(taskManager.getAllSubTaskList());
            sendText(httpExchange, jsonAllSubTaskList);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "Subtasklist not found");
        }
    }

    private void getSubTaskById(HttpExchange httpExchange) throws IOException {
        try {
            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[2]);
            String subtask = gson.toJson(taskManager.getSubTaskById(id));
            sendText(httpExchange, subtask);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "Subtask not found by id");
        }
    }


    private void createOrUpdateSubTask(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestBody()==null) {
            sendNotFound(httpExchange, "No request body was found");
        }
        try {
            SubTask subTask = gson.fromJson(httpExchange.getRequestBody().toString(), SubTask.class);
            if (subTask.getTaskId() == 0) {
                taskManager.createSubTask(subTask);
            }
            taskManager.updateSubTask(subTask);
        } catch (Exception exception) {
            sendHasInteractions(httpExchange, "Found tasks overlaps");
        }
    }

    private void deleteSubTaskById(HttpExchange httpExchange) throws IOException {
        try {
            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[2]);
            taskManager.removeSubTaskById(id);
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
