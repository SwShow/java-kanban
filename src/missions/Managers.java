package missions;

import servermanager.HTTPTaskManager;
import java.net.URI;

public class Managers {

    public static TaskManager getDefault() throws ManagerSaveException {
        return new HTTPTaskManager(URI.create("http://localhost:8078"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

