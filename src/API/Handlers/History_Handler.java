package API.Handlers;

import Adapters.DurationAdapter;
import Adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class History_Handler extends BaseHttpHandler {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public History_Handler(TaskManager taskManager) {
        super(taskManager);

    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        getHistory(httpExchange);
    }

    private void getHistory(HttpExchange httpExchange) throws IOException {
        try {
            String jsonHistory = gson.toJson(taskManager.getHistory());
            sendText(httpExchange, jsonHistory);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "History not found");
        }
    }
}
