package servermanager;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import missions.Managers;
import missions.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;


public class HTTPTaskServer {

    private final HttpServer server;

    public HTTPTaskServer() throws IOException {
        server = createServer();

        TaskManager taskManager = Managers.getDefault();

        connectHandlersWithEndpoints(taskManager);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        int delay = 0;
        server.stop(delay);
    }

    private void connectHandlersWithEndpoints(TaskManager taskManager) {
        Map<String, Handler> endpoints = Map.of(
                "/tasks/task", new TaskHandler(taskManager),
                "/tasks/subtask", new SubTaskHandler(taskManager),
                "/tasks/epic", new EpicHandler(taskManager),
                "/tasks", new AllTasksHandler(taskManager),
                "/tasks/history", new HistoryHandler(taskManager),
                "/tasks/subtask/epic", new SubTaskEpicHandler(taskManager)
        );

        endpoints.forEach(server::createContext);
    }

    private HttpServer createServer() throws IOException {
        int port = 8080;
        String addressName = "localhost";
        InetSocketAddress address = new InetSocketAddress(addressName, port);
        int defaultBacklog = 0;
        return HttpServer.create(address, defaultBacklog);
    }

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HTTPTaskServer taskServer = new HTTPTaskServer();
        taskServer.start();

        taskServer.stop();
        kvServer.stop();
    }

}
