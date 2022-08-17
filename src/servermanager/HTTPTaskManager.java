package servermanager;

import challenges.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.HistoryManagerAdapter;
import handler.LocalDateTimeAdapter;
import handler.TaskAdapter;
import missions.FileBackedTasksManager;
import missions.HistoryManager;
import missions.ManagerSaveException;

import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final transient KVTaskClient client;

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(HistoryManager.class, new HistoryManagerAdapter())
            .registerTypeHierarchyAdapter(Task.class, new TaskAdapter())
            .create();
    private static final String KEY = "manager";

    public HTTPTaskManager(URI serverURL) {
        super(new File(""));

        try {
            client = new KVTaskClient(serverURL);
        }
        catch (Exception exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
    }

    @Override
    public void save()  {
        try {
            String serializedManager = gson.toJson(this);
            client.put(KEY, serializedManager);
        } catch (Exception exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
    }

    @Override
    public void load() {
        try {
            String serializedManager = client.load(KEY);
            HTTPTaskManager loadedManager = gson.fromJson(serializedManager, HTTPTaskManager.class);

            tasks.clear();
            tasks.putAll(loadedManager.tasks);

            subTasks.clear();
            subTasks.putAll(loadedManager.subTasks);

            epics.clear();
            epics.putAll(loadedManager.epics);

            prioritySet.clear();
            prioritySet.addAll(loadedManager.prioritySet);

            for (Task task: loadedManager.getHistory())
                historyManager.addHistory(task);
        }
        catch (WrongResponseException ignored) {
        }
        catch (Exception exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
    }
}
