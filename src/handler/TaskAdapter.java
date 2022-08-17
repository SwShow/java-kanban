package handler;

import challenges.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class TaskAdapter  extends TypeAdapter<Task> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        if (task == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();

        String typeName = task.getType().name();
        String serializedTask = gson.toJson(task);
        jsonWriter.name(typeName).jsonValue(serializedTask);

        jsonWriter.endObject();
    }

    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        jsonReader.beginObject();

        String typeName = jsonReader.nextName();
        TypeTask type = TypeTask.valueOf(typeName);

        Task task = switch(type) {
            case TASK -> gson.fromJson(jsonReader, Task.class);
            case SUBTASK -> gson.fromJson(jsonReader, SubTask.class);
            case EPIC -> gson.fromJson(jsonReader, Epic.class);
        };

        jsonReader.endObject();

        return task;
    }

    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.of(1900, 10, 10, 10, 10);
        int duration = 10;

        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Task.class, new TaskAdapter())
                .create();

        Task task = new Task("1", "1", TaskStatus.IN_PROGRESS, startTime, duration);
        String serializedTask = gson.toJson(task);
        Task deserializedTask = gson.fromJson(serializedTask, Task.class);
        System.out.println(task.equals(deserializedTask));

        Task subTask = new SubTask("1", "1", TaskStatus.IN_PROGRESS, startTime, duration);
        serializedTask = gson.toJson(subTask);
        deserializedTask = gson.fromJson(serializedTask, Task.class);
        System.out.println(subTask.equals(deserializedTask));
    }
}
