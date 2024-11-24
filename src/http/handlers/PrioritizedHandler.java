package http.handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import service.TaskManager;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class PrioritizedHandler extends BaseHttpHandler {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        getPrioritizedTasksList(httpExchange);
    }

    private void getPrioritizedTasksList(HttpExchange httpExchange) throws IOException {
        try {
            String jsonPrioritizedList = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(httpExchange, jsonPrioritizedList);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "Prioritized list not found");
        }
    }
}
