package handler;

import challenges.Task;
import com.sun.net.httpserver.HttpExchange;
import missions.TaskManager;
import servermanager.StatusCode;

import java.io.IOException;
import java.util.List;

public class AllTasksHandler  extends Handler{
    public AllTasksHandler(TaskManager taskManager) {
        super(taskManager);
    }
    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        sendResponse(StatusCode.SUCCESS.getCode(), prioritizedTasks, httpExchange);
    }
}
