package missions;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() throws ManagerSaveException {
        return new FileBackedTasksManager(new File("resources/history.csv"));}
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

