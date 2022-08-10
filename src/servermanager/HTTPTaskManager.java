package servermanager;

import challenges.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.HistoryManagerAdapter;
import handler.LocalDateTimeAdapter;
import missions.FileBackedTasksManager;
import missions.HistoryManager;
import missions.ManagerSaveException;

import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class HTTPTaskManager extends FileBackedTasksManager {
    private transient KVTaskClient client;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeHierarchyAdapter(HistoryManager.class, new HistoryManagerAdapter())
            .serializeNulls()
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
            client.put(KEY, gson.toJson(this));
        } catch (Exception exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
    }

    public static HTTPTaskManager loadFromServer(URI serverURL) {
        try {
            HTTPTaskManager httpTaskManager = new HTTPTaskManager(serverURL);

            // получаем поля, не восстанавливающиеся при десериализации
            KVTaskClient client = httpTaskManager.client;
            TreeSet<Task> prioritySet = httpTaskManager.prioritySet;

            // загружаем менеджер
            String serializedManager = client.load(KEY);
            httpTaskManager = gson.fromJson(serializedManager, HTTPTaskManager.class);

            // восстанавливаем остальные поля
            httpTaskManager.client = client;
            prioritySet.addAll(httpTaskManager.tasks.values());
            prioritySet.addAll(httpTaskManager.subTasks.values());
            httpTaskManager.prioritySet = prioritySet;

            return httpTaskManager;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new ManagerSaveException(exception.getMessage());
        }
    }
}
