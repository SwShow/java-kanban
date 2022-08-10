package handler;

import challenges.Epic;
import challenges.SubTask;
import challenges.Task;
import challenges.TypeTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import missions.HistoryManager;
import missions.Managers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryManagerAdapter extends TypeAdapter<HistoryManager> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
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
            jsonWriter.beginObject();
            String name = task.getType().name();
            jsonWriter.name(name).value(gson.toJson(task));
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
    }

    @Override
    public HistoryManager read(JsonReader jsonReader) throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            TypeTask type = TypeTask.valueOf(jsonReader.nextName());
            String serializedTask = jsonReader.nextString();
            Task task = switch(type) {
                case TASK -> gson.fromJson(serializedTask, Task.class);
                case EPIC -> gson.fromJson(serializedTask, Epic.class);
                case SUBTASK -> gson.fromJson(serializedTask, SubTask.class);
            };
            historyManager.addHistory(task);
            jsonReader.endObject();
        }
        jsonReader.endArray();

        return historyManager;

    }
}
