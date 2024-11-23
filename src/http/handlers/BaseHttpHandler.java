package http.handlers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected TaskManager taskManager;

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }

    public void sendText(HttpExchange exchange, String text) {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, 0);
            os.write(text.getBytes(DEFAULT_CHARSET));
        } catch (IOException e) {
            System.out.println("Error while sending response");
        }
        exchange.close();
    }

    public void sendOnlyCode(HttpExchange h) {
        try {
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(201, 0);
        } catch (IOException e) {
            System.out.println("Error while sending response");
        }
        h.close();
    }

    public void sendNotFound(HttpExchange h, String text) {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(404, resp.length);
            h.getResponseBody().write(resp);
        } catch (IOException e) {
            System.out.println("Error while sending response");
        }
        h.close();
    }

    public void sendHasInteractions(HttpExchange h, String text) {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(406, resp.length);
            h.getResponseBody().write(resp);
        } catch (IOException e) {
            System.out.println("Error while sending response");
        }
        h.close();
    }
}

