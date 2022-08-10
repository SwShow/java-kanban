package servermanager;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import missions.Managers;
import missions.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HTTPTaskServer {

    private final HttpServer server;

    public HTTPTaskServer() throws IOException {
        // создаем сервер
        int port = 8080;
        String addressName = "localhost";
        InetSocketAddress address = new InetSocketAddress(addressName, port);
        int defaultBacklog = 0;
        server = HttpServer.create(address, defaultBacklog);

        TaskManager taskManager = Managers.getDefault();

        String TASK_ENDPOINT = "/tasks/task/";
        server.createContext(TASK_ENDPOINT, new TaskHandler(taskManager));

        String SUBTASK_ENDPOINT = "/tasks/subtask/";
        server.createContext(SUBTASK_ENDPOINT, new SubTaskHandler(taskManager));

        String EPIC_ENDPOINT = "/tasks/epic/";
        server.createContext(EPIC_ENDPOINT, new EpicHandler(taskManager));

        String ALL_TASKS_ENDPOINT = "/tasks/";
        server.createContext(ALL_TASKS_ENDPOINT, new AllTasksHandler(taskManager));

        String HISTORY_ENDPOINT = "/tasks/history/";
        server.createContext(HISTORY_ENDPOINT, new HistoryHandler(taskManager));

        String SUBTASK_EPIC_ENDPOINT = "/tasks/subtask/epic";
        server.createContext(SUBTASK_EPIC_ENDPOINT, new SubTaskEpicHandler(taskManager));
    }

    public void start() {
        server.start();
        System.out.println("HTTPServer запущен");
    }
    public void stop() {
        int delay = 0;
        server.stop(delay);
    }

}
