package handler;

import challenges.SubTask;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import missions.TaskManager;
import servermanager.StatusCode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SubTaskHandler extends Handler {

    private final String ID_EPIC_QUERY = "idEpic";

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.isEmpty()) {
            getAllSubTasks(httpExchange);
        }
        else if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            getSubTask(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            addSubTask(queryMap, httpExchange);
        }
        else if (queryMap.size() == 2 && queryMap.containsKey(ID_QUERY) && queryMap.containsKey(ID_EPIC_QUERY)) {
            changeSubTask(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    @Override
    protected void handleDelete(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.isEmpty()) {
            removeAllSubTasks(httpExchange);
        }
        else if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            removeSubTask(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    private void getAllSubTasks(HttpExchange httpExchange) throws IOException {
        List<SubTask> subTaskList = taskManager.getSubTasks();
        sendResponse(StatusCode.SUCCESS.getCode(), subTaskList, httpExchange);
    }

    private void getSubTask(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id, если задан некорректно - отправить ошибку и завершить обработку запроса
        int id;
        try {
            id = getIntQuery(ID_QUERY, queryMap);
        }
        catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        // получить SubTask
        SubTask task = taskManager.findSubTaskById(id);

        // сообщить клиенту об успехе и завершить обработку запроса
        sendResponse(StatusCode.SUCCESS.getCode(), task, httpExchange);
    }

    private void addSubTask(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id эпика, если задан некорректно - отправить ошибку и завершить обработку запроса
        int idEpic;
        try {
            idEpic = getIntQuery(ID_QUERY, queryMap);
        }
        catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }


        // получить подзадачу, если задана некорректно - отправить ошибку и завершить обработку запроса
        String serializedTask = readBody(httpExchange);
        SubTask task;
        try {
            task = gson.fromJson(serializedTask, SubTask.class);
        }
        catch (JsonParseException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse subtask", httpExchange);
            return;
        }

        // добавить в менеджер
        taskManager.addSubTask(idEpic, task);

        // сообщить клиенту об успехе, вернуть новое id подзадачи и закончить обработку запроса
        sendResponse(StatusCode.SUCCESS.getCode(), String.valueOf(task.getId()), httpExchange);
    }

    private void changeSubTask(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id эпика и сабтаска, если заданы некорректно - отправить ошибку и завершить обработку запроса
        int idEpic;
        int idSubTask;
        try {
            idEpic = getIntQuery(ID_EPIC_QUERY, queryMap);
            idSubTask = getIntQuery(ID_QUERY, queryMap);
        }
        catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        // получить подзадачу, если задана некорректно - отправить ошибку и завершить обработку запроса
        String serializedTask = readBody(httpExchange);
        SubTask task;
        try {
            task = gson.fromJson(serializedTask, SubTask.class);
        }
        catch (JsonParseException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse subtask", httpExchange);
            return;
        }

        // изменить подзадачу
        taskManager.changeSubTask(idSubTask, task, idEpic);

        // сообщить клиенту об успехе и закончить обработку запроса
        sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
    }

    private void removeAllSubTasks(HttpExchange httpExchange) throws IOException {
        taskManager.removeSubTasks();
        sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
    }

    private void removeSubTask(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id, если задан некорректно - отправить ошибку и завершить обработку запроса
        int id;
        try {
            id = getIntQuery(ID_QUERY, queryMap);
        }
        catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        // удалить подзадачу
        taskManager.removeSubTask(id);

        // сообщить клиенту об успехе и закончить обработку запроса
        sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
    }
}
