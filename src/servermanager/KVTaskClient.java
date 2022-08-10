package servermanager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
        KVServer kvServer = new KVServer();
        kvServer.start();
        HTTPTaskServer httpTaskServer = new HTTPTaskServer();
        httpTaskServer.start();
       /* URI serverURL = URI.create("http://localhost:8078");
        KVTaskClient kvTaskClient = new KVTaskClient(serverURL);*/
    }
}
