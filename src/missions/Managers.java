package missions;

import servermanager.HTTPTaskManager;
import java.net.URI;

public class Managers {

    public static TaskManager getDefault() throws ManagerSaveException {
        HTTPTaskManager taskManager = new HTTPTaskManager(URI.create("http://localhost:8078"));
        taskManager.load();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

