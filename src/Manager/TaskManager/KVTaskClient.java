package Manager.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String apiToken;
    private final String baseUri;

    public KVTaskClient(String uri)  {
        this.baseUri = uri;
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
            URI uriRegister = URI.create(baseUri + "register");
            HttpClient clientRegister = HttpClient.newHttpClient();

            HttpRequest requestRegister = HttpRequest
                    .newBuilder()
                    .uri(uriRegister)
                    .GET()
                    .build();

            final HttpResponse<String> responseRegister = clientRegister.send(requestRegister, HttpResponse.BodyHandlers.ofString());
            this.apiToken = responseRegister.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Или при запуске севера или при регистрации возникла ошибка");
        }

    }

    // для тестов
    public KVTaskClient(String uri, KVServer kvServer) {
        this.baseUri = uri;
        kvServer.start();
        URI uriRegister = URI.create(baseUri + "register");
        HttpClient clientRegister = HttpClient.newHttpClient();

        HttpRequest requestRegister = HttpRequest
                .newBuilder()
                .uri(uriRegister)
                .GET()
                .build();

        try {
            final HttpResponse<String> responseRegister = clientRegister.send(requestRegister, HttpResponse.BodyHandlers.ofString());
            this.apiToken = responseRegister.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Или при запуске севера или при регистрации возникла ошибка");
        }
    }

    public void put(String key, String json){
        String saveUri = baseUri + "save/" + key + "?API_TOKEN=" + apiToken;
        URI uri = URI.create(saveUri);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        String loadUri = baseUri + "load/" + key + "?API_TOKEN=" + apiToken;
        URI uri = URI.create(loadUri);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw  new RuntimeException("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}
