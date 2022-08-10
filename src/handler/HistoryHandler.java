package handler;

import challenges.Task;
import com.sun.net.httpserver.HttpExchange;
import missions.TaskManager;
import servermanager.StatusCode;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends Handler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }
    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        sendResponse(StatusCode.SUCCESS.getCode(), history, httpExchange);
    }
}
