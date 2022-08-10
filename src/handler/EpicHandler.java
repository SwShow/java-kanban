package handler;

import challenges.Epic;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import missions.TaskManager;
import servermanager.StatusCode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EpicHandler extends Handler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.isEmpty()) {
            getAllEpics(httpExchange);
        }
        else if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            getEpic(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.isEmpty()) {
            createEpic(httpExchange);
        }
        else if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            updateEpic(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    @Override
    protected void handleDelete(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.isEmpty()) {
            removeAllEpics(httpExchange);
        }
        else if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            removeEpic(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    private void createEpic(HttpExchange httpExchange) throws IOException {
        // получить эпик из запроса, в случае ошибки - отправить клиенту сообщение и закончить обработку запроса
        String serializedEpic = readBody(httpExchange);
        Epic epic;
        try {
            epic = gson.fromJson(serializedEpic, Epic.class);
        }
        catch (JsonParseException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to deserialize epic", httpExchange);
            return;
        }

        // создать эпик
        taskManager.createEpic(epic);

        // сообщить клиенту об успехе, отправить ему id эпика и закончить обработку запроса
        sendResponse(StatusCode.SUCCESS.getCode(), String.valueOf(epic.getId()), httpExchange);
    }

    private void getEpic(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id, если задан некорректно - отправить ошибку и завершить обработку запроса
        int id;
        try {
            id = getIntQuery(ID_QUERY, queryMap);
        }
        catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        // получить эпик
        Epic epic = taskManager.findEpicById(id);

        // отправить эпик клиенту и закончить обработку запроса
        sendResponse(StatusCode.SUCCESS.getCode(), epic, httpExchange);
    }

    private void updateEpic(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id, если задан некорректно - отправить ошибку и завершить обработку запроса
        int id;
        try {
            id = getIntQuery(ID_QUERY, queryMap);
        }
        catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        // получить эпик из запроса, в случае ошибки - отправить клиенту сообщение и закончить обработку запроса
        String serializedEpic = readBody(httpExchange);
        Epic epic;
        try {
            epic = gson.fromJson(serializedEpic, Epic.class);
        }
        catch (JsonParseException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to deserialize epic", httpExchange);
            return;
        }

        // обновить эпик
        taskManager.updateEpic(id, epic);

        // отправить пользователю сообщение об успехе
        sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
    }

    private void removeAllEpics(HttpExchange httpExchange) throws IOException {
        taskManager.removeEpics();
        sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
    }

    private void removeEpic(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id, если задан некорректно - отправить ошибку и завершить обработку запроса
        int id;
        try {
            id = getIntQuery(ID_QUERY, queryMap);
        }
        catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        // удалить epic
        taskManager.removeEpic(id);

        // отправить пользователю сообщение об успехе
        sendResponse(StatusCode.SUCCESS.getCode(), httpExchange);
    }

    private void getAllEpics(HttpExchange httpExchange) throws IOException {
        List<Epic> epicList = taskManager.getEpics();
        sendResponse(StatusCode.SUCCESS.getCode(), epicList, httpExchange);
    }
}
