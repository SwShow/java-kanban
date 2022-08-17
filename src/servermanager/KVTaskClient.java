package servermanager;

import challenges.Epic;
import challenges.SubTask;
import challenges.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.HistoryManagerAdapter;
import handler.LocalDateTimeAdapter;
import handler.TaskAdapter;
import missions.HistoryManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static challenges.TaskStatus.DONE;
import static challenges.TaskStatus.NEW;

public class KVTaskClient {

    private final HttpClient client;

    private final String apiTokenQuery;

    private final String saveEndpoint;
    private final String loadEndpoint;

    public KVTaskClient(URI serverURL) throws IOException, InterruptedException {

        client = HttpClient.newHttpClient();

        URI url = URI.create(serverURL.toString() + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == StatusCode.SUCCESS.getCode()) {
            apiTokenQuery = "API_TOKEN=" + response.body();
        } else
            throw new WrongResponseException("Unable to register client on KVServer");

        saveEndpoint = serverURL + "/save/";
        loadEndpoint = serverURL + "/load/";
    }

    public void put(String key, String toJson) throws IOException, InterruptedException {
        URI url = URI.create(saveEndpoint + "/" + key + "/?" + apiTokenQuery);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(toJson);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() != StatusCode.SUCCESS.getCode()) {
            throw new WrongResponseException("Невозможно отправить ключ и значение на KVServer");
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        URI url = URI.create(loadEndpoint + "/" + key + "/?" + apiTokenQuery);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == StatusCode.SUCCESS.getCode())
            return response.body();
        else
            throw new WrongResponseException("Unable to load value from KVServer");
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        KVServer kvServer = new KVServer();
        kvServer.start();
        HTTPTaskServer httpTaskServer = new HTTPTaskServer();
        httpTaskServer.start();

        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(HistoryManager.class, new HistoryManagerAdapter())
                .registerTypeHierarchyAdapter(Task.class, new TaskAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        LocalDateTime startTime1 = LocalDateTime.of(2022, 7, 14, 0, 0);
        long duration = 2000;
        // создаем задачу 1
        Task data = new Task("посмотреть фильм", "интересный", NEW,
                startTime1, duration);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(data);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response: " + response.body());

        LocalDateTime startTime2 = LocalDateTime.of(2022, 9, 14, 0, 0);
        // создаем задачу 2
        Task dinner = new Task("сходить в кафе", "изучить меню", NEW, startTime2, duration);
        String json2 = gson.toJson(dinner);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        System.out.println("response: " + response2.body());
        // создаем эпик
        Epic shopping = new Epic("сходить в магазин", "Ашан", NEW);
        String json3 = gson.toJson(shopping);
        URI url2 = URI.create("http://localhost:8080/tasks/epic");
        final HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json3);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).POST(body3).build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        System.out.println("response: " + response3.body());

        // сщздаем подзадачу 1
        LocalDateTime startTime3 = LocalDateTime.of(2022, 8, 14, 0, 0);
        SubTask Shop1 = new SubTask("купить мыло", "душистое", NEW, startTime3, duration);
        Shop1.setIdEpic(3);
        String json4 = gson.toJson(Shop1);
        URI url3 = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        final HttpRequest.BodyPublisher body4 = HttpRequest.BodyPublishers.ofString(json4);
        HttpRequest request4 = HttpRequest.newBuilder().uri(url3).POST(body4).build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        System.out.println("response: " + response4.body());

        // создаем подзадачу 2
        LocalDateTime startTime4 = LocalDateTime.of(2022, 8, 20, 0, 0);
        SubTask Shop2 = new SubTask( "купить шампунь", "для нормальных волос", DONE, startTime4, duration);
        Shop2.setIdEpic(3);
        String json5 = gson.toJson(Shop2);
        final HttpRequest.BodyPublisher body5 = HttpRequest.BodyPublishers.ofString(json5);
        HttpRequest request5 = HttpRequest.newBuilder().uri(url3).POST(body5).build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        System.out.println("response: " + response5.body());
        // получаем все задачи
        HttpRequest request6 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
        System.out.println("все задачи:" + response6.body());
        // получаем задачу 2
        URI url4 = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request7 = HttpRequest.newBuilder().uri(url4).GET().build();
        HttpResponse<String> response7 = client.send(request7, HttpResponse.BodyHandlers.ofString());
        System.out.println("Task id 2: " +  response7.body());
        // получаем эпик
        URI url6 = URI.create("http://localhost:8080/tasks/subtask/epic/?id=3");
        HttpRequest request9 = HttpRequest.newBuilder().uri(url6).GET().build();
        HttpResponse<String> response9 = client.send(request9, HttpResponse.BodyHandlers.ofString());
        System.out.println("tasks/subtask/epic: " + response9.body());
        // получаем историю
        URI url5 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request8 = HttpRequest.newBuilder().uri(url5).GET().build();
        HttpResponse<String> response8 = client.send(request8, HttpResponse.BodyHandlers.ofString());
        System.out.println("TasksHistory: " + response8.body());
        // удаляем задачу 2
        HttpRequest request10 = HttpRequest.newBuilder().uri(url4).DELETE().build();
        client.send(request10, HttpResponse.BodyHandlers.discarding());
        // удаляем историю
        HttpRequest request11 = HttpRequest.newBuilder().uri(url5).DELETE().build();
        HttpResponse<String> response11 = client.send(request11, HttpResponse.BodyHandlers.ofString());
        System.out.println("TasksHistory: " + response11.body());
    }
}
