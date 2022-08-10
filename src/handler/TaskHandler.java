package handler;

import challenges.Task;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import missions.TaskManager;
import servermanager.StatusCode;

import java.io.IOException;
import java.util.Map;

public class TaskHandler  extends Handler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.isEmpty()) {
            getAllTasks(httpExchange);
        }
        else if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            getTask(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }
    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.isEmpty()) {
            createTask(httpExchange);
        }
        else if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            updateTask(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    @Override
    protected void handleDelete(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.isEmpty()) {
            removeAllTasks(httpExchange);
        }
        else if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            removeTask(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    private void getAllTasks(HttpExchange httpExchange) throws IOException {
        sendResponse(StatusCode.SUCCESS.getCode(), taskManager.getTasks(), httpExchange);
    }

    private void getTask(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id, если задан некорректно - отправить ошибку и закончить обработку запроса
        int id ;
        try {
            id = getIntQuery(ID_QUERY, queryMap);
        } catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        // получить задачу
        Task task = taskManager.findTaskById(id);

        // отправить задачу клиенту и закончить обработку запроса
        sendResponse(StatusCode.SUCCESS.getCode(), task, httpExchange);
    }
    private void createTask(HttpExchange httpExchange) throws IOException {
        try {
            String serializedTask = readBody(httpExchange);
            Task task = gson.fromJson(serializedTask, Task.class);
            taskManager.createTask(task);
            String id = String.valueOf(task.getId()); // клиенту в body отправляется id созданной задачи
            sendResponse(StatusCode.SUCCESS.getCode(), id, httpExchange);
        }
        catch (JsonSyntaxException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse task", httpExchange);
        }
    }

    private void updateTask(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        int id;
        try {
            id = getIntQuery(ID_QUERY, queryMap);
        } catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        try {
            String serializedTask = readBody(httpExchange);
            Task task = gson.fromJson(serializedTask, Task.class);
            taskManager.updateTask(id, task);
            sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
        }
        catch (JsonSyntaxException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse task", httpExchange);
        }
    }

    private void removeTask(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        int id;
        try {
            id = getIntQuery(ID_QUERY, queryMap);
        } catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        taskManager.removeTask(id);

        sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
    }

    private void removeAllTasks(HttpExchange httpExchange) throws IOException {
        taskManager.removeTasks();

        sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
    }
}
