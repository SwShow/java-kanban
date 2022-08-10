package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import missions.TaskManager;
import servermanager.StatusCode;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class Handler implements HttpHandler {
    protected static final String ID_QUERY = "id";

    protected final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

    protected final TaskManager taskManager;

    public Handler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
            HttpMethod httpMethod;
            try {
                String requestMethodName = httpExchange.getRequestMethod();
                httpMethod =  HttpMethod.valueOf(requestMethodName);
            } catch (IllegalArgumentException exception) {
                sendResponse(StatusCode.BAD_REQUEST.getCode(), httpExchange);
                return;
            }
        // обрабатывыем http метод
        switch (httpMethod) {
            case GET -> handleGet(httpExchange);
            case POST -> handlePost(httpExchange);
            case DELETE -> handleDelete(httpExchange);
            default -> sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid http method", httpExchange);
        }
    }

    protected void handleDelete(HttpExchange httpExchange) throws IOException {
        sendResponse(StatusCode.BAD_REQUEST.getCode(), httpExchange);
    }

    protected void handlePost(HttpExchange httpExchange)  throws IOException {
        sendResponse(StatusCode.BAD_REQUEST.getCode(), httpExchange);
    }

    protected void handleGet(HttpExchange httpExchange)  throws IOException {
        sendResponse(StatusCode.BAD_REQUEST.getCode(), httpExchange);
    }

    protected static void sendResponse(int statusCode, HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(statusCode, 0);
        httpExchange.close();
    }

    protected static void sendResponse(int statusCode, String message, HttpExchange httpExchange)
            throws IOException
    {
        byte[] bytesToWrite = message.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(statusCode, bytesToWrite.length);
        OutputStream output = httpExchange.getResponseBody();
        output.write(bytesToWrite);
        httpExchange.close();
    }
    protected static void sendResponse(int statusCode, Object toSerialize, HttpExchange httpExchange)
            throws IOException
    {
        String serializedTasks = gson.toJson(toSerialize);
        sendResponse(statusCode, serializedTasks, httpExchange);
    }

    protected static String readBody(HttpExchange httpExchange) throws IOException {
        byte[] body = httpExchange.getRequestBody().readAllBytes();
        return new String(body, StandardCharsets.UTF_8);
    }

    protected static Map<String, String> getQueryMap(HttpExchange httpExchange) {
        Map<String, String> queryMap = new HashMap<>();

        URI uri = httpExchange.getRequestURI();
        String query = uri.getQuery();

        if (query == null)
            return queryMap;

        String[] querySplit = query.split("&");
        for (String pair: querySplit) {
            int i = pair.indexOf("=");
            String key = i > 0 ? pair.substring(0, i) : pair;
            String value = i > 0 && pair.length() > i + 1 ? pair.substring(i + 1) : null;
            queryMap.put(key, value);
        }

        return queryMap;
    }
    protected static int getIntQuery(String key, Map<String, String> queryMap) {
        if (queryMap.isEmpty() || !queryMap.containsKey(key)) {
            throw new IllegalArgumentException("Invalid query");
        }

        try {
            return Integer.parseInt(queryMap.get(key));
        }
        catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Unable to parse id");
        }
    }

}
