package http.handlers;
import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import model.Epic;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);

    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());

        switch (endpoint) {
            case "GET": //epics - getEpics
                getAllEpicList(httpExchange);
            case "GET_with_id"://epics/{id} - getEpicById
                getEpicById(httpExchange);
            case "GET_with_param"://epics/{id}/subtasks - getEpicSubTasks
                getEpicSubTasks(httpExchange);
            case "POST": //epics - createEpic(epic)
                createEpic(httpExchange);
            case "DELETE": //epics/{id} - deleteEpic(id)
                deleteEpicById(httpExchange);
            case "UNKNOWN": //ошибка
                sendNotFound(httpExchange, "Path not found");
        }

    }

    private void getAllEpicList(HttpExchange httpExchange) throws IOException {
        try {
            String jsonAllEpicList = gson.toJson(taskManager.getAllEpicList());
            sendText(httpExchange, jsonAllEpicList);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "Epiclist not found");
        }
    }

    private void getEpicById(HttpExchange httpExchange) throws IOException {
        try {
            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[2]);
            String epic = gson.toJson(taskManager.getEpicById(id));
            sendText(httpExchange, epic);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "Task not found by id");
        }
    }

    private void getEpicSubTasks(HttpExchange httpExchange) throws IOException {
        List<SubTask> subTaskList = new ArrayList<>();
        try {
            int ID = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[2]);
            for (Integer subTaskId : taskManager.getEpicById(ID).getSubTaskIdList()) {
                subTaskList.add(taskManager.getSubTaskById(subTaskId));
            }
            String jsonSubTaskList = gson.toJson(subTaskList);
            sendText(httpExchange, jsonSubTaskList);
        } catch (NotFoundException n) {
            sendNotFound(httpExchange, "Epic not found by id");
        }
    }

    private void createEpic(HttpExchange httpExchange) throws IOException {
        try {
            Epic epic = gson.fromJson(httpExchange.getRequestBody().toString(), Epic.class);
            taskManager.createEpic(epic);
            sendOnlyCode(httpExchange);
        } catch (Exception exception) {
            sendHasInteractions(httpExchange, "Found tasks overlaps");
        }
    }

    private void deleteEpicById(HttpExchange httpExchange) throws IOException {
        try {
            int ID = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[2]);
            taskManager.removeEpicById(ID);
            sendOnlyCode(httpExchange);
        } catch (NotFoundException exception) {
            sendNotFound(httpExchange, "Invalid ID");
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
        if (pathParts.length == 4 && requestMethod.equals("GET")) {
            return "GET_with_param";
        }
        if (pathParts.length == 2 && requestMethod.equals("POST")) {
            return "POST";
        }
        if (requestMethod.equals("DELETE")) {
            return "DELETE";
        }

        return "UNKNOWN";
    }
}

