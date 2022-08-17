package handler;

import challenges.SubTask;
import com.sun.net.httpserver.HttpExchange;
import missions.TaskManager;
import servermanager.StatusCode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SubTaskEpicHandler extends Handler{
    public SubTaskEpicHandler(TaskManager taskManager) {
        super(taskManager);
    }
    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(httpExchange);

        if (queryMap.size() == 1 && queryMap.containsKey(ID_QUERY)) {
            getEpicSubTasks(queryMap, httpExchange);
        }
        else {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Invalid query", httpExchange);
        }
    }

    private void getEpicSubTasks(Map<String, String> queryMap, HttpExchange httpExchange) throws IOException {
        // получить id, если задан некорректно - отправить ошибку и закончить запрос
        int epicId;

        try {
            epicId = getIntQuery(ID_QUERY, queryMap);
        }
        catch (IllegalArgumentException exception) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "Unable to parse id", httpExchange);
            return;
        }

        if (taskManager.findEpicById(epicId) == null) {
            sendResponse(StatusCode.BAD_REQUEST.getCode(), "There is no epic with given id", httpExchange);
            return;
        }

        // получить список подзадач
        List<Integer> subTaskIds = taskManager.findSubtasksOfIdEpic(epicId);
        List<SubTask> subTasks = subTaskIds.stream()
                .map(taskManager::findSubTaskById)
                .toList();

        // отправить список подзадач
        sendResponse(StatusCode.SUCCESS.getCode(), subTasks, httpExchange);
    }
}
