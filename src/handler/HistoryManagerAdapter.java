package handler;

import challenges.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import missions.HistoryManager;
import missions.InMemoryHistoryManager;
import missions.Managers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryManagerAdapter extends TypeAdapter<HistoryManager> {

    private final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Task.class, new TaskAdapter())
            .create();

    @Override
    public void write(JsonWriter jsonWriter, HistoryManager historyManager) throws IOException {
        if (historyManager == null) {
            jsonWriter.nullValue();
            return;
        }

        List<Task> history = historyManager.getHistory();

        jsonWriter.beginArray();
        for (Task task: history) {
            String serializedTask = gson.toJson(task);
            jsonWriter.jsonValue(serializedTask);
        }
        jsonWriter.endArray();
    }

    @Override
    public HistoryManager read(JsonReader jsonReader) throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            Task task = gson.fromJson(jsonReader, Task.class);
            historyManager.addHistory(task);
        }
        jsonReader.endArray();

        return historyManager;

    }
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(HistoryManager.class, new HistoryManagerAdapter())
                .create();

        HistoryManager historyManager = new InMemoryHistoryManager();

        LocalDateTime startTime = LocalDateTime.of(1900, 11, 20, 1, 2);
        int duration = 100;
        Task task1 = new Task("1", "2", TaskStatus.NEW, startTime, duration);
        task1.setId(1);
        Task task2 = new Task("3", "4", TaskStatus.NEW, startTime, duration);
        task2.setId(2);
        historyManager.addHistory(task1);
        historyManager.addHistory(task2);

        String serializedManager = gson.toJson(historyManager);
        System.out.println(serializedManager);

        HistoryManager deserializedManager = gson.fromJson(serializedManager, HistoryManager.class);
        Task deserializedTask1 = deserializedManager.getHistory().get(0);

        System.out.println(deserializedTask1.equals(task1));
    }
}
