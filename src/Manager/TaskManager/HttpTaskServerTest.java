package Manager.TaskManager;

import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

class HttpTaskServerTest {

    HttpTaskServer httpTaskServer;
    Gson gson = new Gson();

    @BeforeEach
    public void serverStart() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    public void serverEnd() throws IOException, InterruptedException {
        System.out.println("Закрытие HTTP сервера");

        URI urlPost = URI.create("http://localhost:8081/tasks/task/");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(urlPost)
                .DELETE()
                .build();

        client.send(request1, HttpResponse.BodyHandlers.ofString());

        httpTaskServer.stop();
    }

    @Test
    public void shouldCreateTaskAndReturnAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-10-22T10:00"), 23);

        //post task to server
        URI urlPost = URI.create("http://localhost:8081/tasks/task/");

        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //get task from server
        URI urlGet = URI.create("http://localhost:8081/tasks/task/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(urlGet)
                .GET()
                .build();

        //sending requests
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, Task> tasks = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, Task>>() {}.getType());

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(task, tasks.get(task.getId()));

    }

    @Test
    public void shouldCreateEpicAndReturnAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);

        //post epic to server
        URI urlPost = URI.create("http://localhost:8081/tasks/epic/");

        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //get epic from server
        URI url = URI.create("http://localhost:8081/tasks/epic/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, Epic> epics = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, Epic>>() {}.getType());

        Assertions.assertNotNull(epics);
        Assertions.assertEquals(1, epics.size());
        Assertions.assertEquals(epic, epics.get(epic.getId()));

    }

    @Test
    public void shouldCreateSubTaskAndReturnAllSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);
        SubTask subTask = new SubTask("111", "222", TaskStatus.NEW, LocalDateTime.parse("2020-10-22T10:00"), 23, epic.getId());

        //post epic to server
        URI urlPostEpic = URI.create("http://localhost:8081/tasks/epic/");

        String jsonEpic = gson.toJson(epic);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestPostEpic = HttpRequest.newBuilder()
                .uri(urlPostEpic)
                .POST(bodyEpic)
                .build();

        //post subtask to server
        URI urlPostSubTask = URI.create("http://localhost:8081/tasks/subtask/");

        String jsonSubTask = gson.toJson(subTask);
        HttpRequest.BodyPublisher bodySubTask = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest requestPostSubTask = HttpRequest.newBuilder()
                .uri(urlPostSubTask)
                .POST(bodySubTask)
                .build();

        //get subtasks from server
        URI url = URI.create("http://localhost:8081/tasks/subtask/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(requestPostEpic, HttpResponse.BodyHandlers.ofString());
        client.send(requestPostSubTask, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, SubTask> subTasks = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, SubTask>>() {}.getType());

        Assertions.assertNotNull(subTasks);
        Assertions.assertEquals(1, subTasks.size());
        Assertions.assertEquals(subTask, subTasks.get(subTask.getId()));

    }

    @Test
    public void shouldReturnTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-10-22T10:00"), 23);

        //post task to server
        URI urlPost = URI.create("http://localhost:8081/tasks/task/");

        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //get task from server by id
        URI urlGet = URI.create("http://localhost:8081/tasks/task/?id=" + task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(urlGet)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task1 = gson.fromJson(response.body(), Task.class);

        Assertions.assertNotNull(task1);
        Assertions.assertEquals(task, task1);

    }

    @Test
    public void shouldReturnEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);

        //post epic to server
        URI urlPost = URI.create("http://localhost:8081/tasks/epic/");

        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //get epic from server by id
        URI url = URI.create("http://localhost:8081/tasks/epic/?id=" + epic.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(),Epic.class);

        Assertions.assertNotNull(epic1);
        Assertions.assertEquals(epic, epic1);

    }

    @Test
    public void shouldReturnSubTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);
        SubTask subTask = new SubTask("111", "222", TaskStatus.NEW, LocalDateTime.parse("2020-10-22T10:00"), 23, epic.getId());

        //post epic to server
        URI urlPost = URI.create("http://localhost:8081/tasks/epic/");

        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //post subtask to server
        URI urlPostSubTask = URI.create("http://localhost:8081/tasks/subtask/");

        String jsonSubTask = gson.toJson(subTask);
        HttpRequest.BodyPublisher bodySubTask = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest requestPostSubTask = HttpRequest.newBuilder()
                .uri(urlPostSubTask)
                .POST(bodySubTask)
                .build();

        //get task from server by id
        URI url = URI.create("http://localhost:8081/tasks/subtask/?id=" + subTask.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        client.send(requestPostSubTask, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = gson.fromJson(response.body(),SubTask.class);

        Assertions.assertNotNull(subTask1);
        Assertions.assertEquals(subTask, subTask1);

    }

    @Test
    public void shouldDeleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-10-22T10:00"), 23);

        //post task to server
        URI urlPost = URI.create("http://localhost:8081/tasks/task/");

        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //delete task from server by id
        URI urlDelete = URI.create("http://localhost:8081/tasks/task/?id=" + task.getId());

        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(urlDelete)
                .DELETE()
                .build();

        //get task from server by id
        URI urlGet = URI.create("http://localhost:8081/tasks/task/?id=" + task.getId());

        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(urlGet)
                .GET()
                .build();

        //sending requests
        client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Task task1 = gson.fromJson(response.body(), Task.class);

        Assertions.assertNull(task1);
    }

    @Test
    public void shouldDeleteEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);

        //post epic to server
        URI urlPost = URI.create("http://localhost:8081/tasks/epic/");

        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //delete epic from server by id
        URI urlDelete = URI.create("http://localhost:8081/tasks/epic/?id=" + epic.getId());

        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(urlDelete)
                .DELETE()
                .build();

        //get epic from server by id
        URI url = URI.create("http://localhost:8081/tasks/epic/?id=" + epic.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(),Epic.class);

        Assertions.assertNull(epic1);

    }

    @Test
    public void shouldDeleteSubTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);
        SubTask subTask = new SubTask("111", "222", TaskStatus.NEW, LocalDateTime.parse("2020-10-22T10:00"), 23, epic.getId());

        //post epic to server
        URI urlPost = URI.create("http://localhost:8081/tasks/epic/");

        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //post subtask to server
        URI urlPostSubTask = URI.create("http://localhost:8081/tasks/subtask/");

        String jsonSubTask = gson.toJson(subTask);
        HttpRequest.BodyPublisher bodySubTask = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest requestPostSubTask = HttpRequest.newBuilder()
                .uri(urlPostSubTask)
                .POST(bodySubTask)
                .build();

        //delete subtask from server by id
        URI urlDelete = URI.create("http://localhost:8081/tasks/subtask/?id=" + subTask.getId());

        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(urlDelete)
                .DELETE()
                .build();

        //get subtask from server by id
        URI url = URI.create("http://localhost:8081/tasks/subtask/?id=" + subTask.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        client.send(requestPostSubTask, HttpResponse.BodyHandlers.ofString());
        client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = gson.fromJson(response.body(),SubTask.class);

        Assertions.assertNull(subTask1);

    }

    @Test
    public void shouldUpdateTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-10-22T10:00"), 23);
        Task newTask = new Task("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-10-22T10:00"), 23);

        //post task to server
        URI urlPost = URI.create("http://localhost:8081/tasks/task/");

        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //post updated task to server
        URI urlPostUpd = URI.create("http://localhost:8081/tasks/task/?id=" + task.getId());

        String jsonUpd = gson.toJson(newTask);
        HttpRequest.BodyPublisher bodyUpd = HttpRequest.BodyPublishers.ofString(jsonUpd);
        HttpRequest postRequestUpd = HttpRequest.newBuilder()
                .uri(urlPostUpd)
                .POST(bodyUpd)
                .build();

        //get task from server by id
        URI urlGet = URI.create("http://localhost:8081/tasks/task/?id=" + newTask.getId());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(urlGet)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        client.send(postRequestUpd, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Task task1 = gson.fromJson(response.body(), Task.class);

        Assertions.assertNotNull(task1);
        Assertions.assertEquals(newTask, task1);

    }

    @Test
    public void shouldUpdateEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);
        Epic newEpic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2018-10-22T10:00"), 23);

        //post epic to server
        URI urlPost = URI.create("http://localhost:8081/tasks/epic/");

        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //post updated epic to server
        URI urlPostUpd = URI.create("http://localhost:8081/tasks/epic/?id=" + epic.getId());

        String jsonUpd = gson.toJson(newEpic);
        HttpRequest.BodyPublisher bodyUpd = HttpRequest.BodyPublishers.ofString(jsonUpd);
        HttpRequest postRequestUpd = HttpRequest.newBuilder()
                .uri(urlPostUpd)
                .POST(bodyUpd)
                .build();

        //get epic from server by id
        URI url = URI.create("http://localhost:8081/tasks/epic/?id=" + newEpic.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        client.send(postRequestUpd, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(),Epic.class);

        Assertions.assertNotNull(epic1);
        Assertions.assertEquals(newEpic, epic1);

    }

    @Test
    public void shouldUpdateSubTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);
        SubTask subTask = new SubTask("111", "222", TaskStatus.NEW, LocalDateTime.parse("2020-10-22T10:00"), 23, epic.getId());
        SubTask newSubTask = new SubTask("1111", "2222", TaskStatus.NEW, LocalDateTime.parse("2022-10-22T10:00"), 23, epic.getId());

        //post epic to server
        URI urlPost = URI.create("http://localhost:8081/tasks/epic/");

        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //post subtask to server
        URI urlPostSubTask = URI.create("http://localhost:8081/tasks/subtask/");

        String jsonSubTask = gson.toJson(subTask);
        HttpRequest.BodyPublisher bodySubTask = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest requestPostSubTask = HttpRequest.newBuilder()
                .uri(urlPostSubTask)
                .POST(bodySubTask)
                .build();

        //post updated subtask to server
        URI urlPostUpd = URI.create("http://localhost:8081/tasks/subtask/?id=" + subTask.getId());

        String jsonUpd = gson.toJson(newSubTask);
        HttpRequest.BodyPublisher bodyUpd = HttpRequest.BodyPublishers.ofString(jsonUpd);
        HttpRequest postRequestUpd = HttpRequest.newBuilder()
                .uri(urlPostUpd)
                .POST(bodyUpd)
                .build();

        //get subtask from server by id
        URI url = URI.create("http://localhost:8081/tasks/subtask/?id=" + newSubTask.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        client.send(requestPostSubTask, HttpResponse.BodyHandlers.ofString());
        client.send(postRequestUpd, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = gson.fromJson(response.body(),SubTask.class);

        Assertions.assertNotNull(subTask1);
        Assertions.assertEquals(newSubTask, subTask1);

    }

    @Test
    public void shouldDeleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);
        SubTask subTask = new SubTask("111", "222", TaskStatus.NEW, LocalDateTime.parse("2020-10-22T10:00"), 23, epic.getId());
        Task task = new Task("1111", "2222", TaskStatus.NEW, LocalDateTime.parse("2001-10-22T10:00"), 23);

        //post task to server
        URI urlPostTask = URI.create("http://localhost:8081/tasks/task/");

        String jsonTask = gson.toJson(task);
        HttpRequest.BodyPublisher bodyTask = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest postRequestTask = HttpRequest.newBuilder()
                .uri(urlPostTask)
                .POST(bodyTask)
                .build();

        //post epic to server
        URI urlPostEpic = URI.create("http://localhost:8081/tasks/epic/");

        String jsonEpic = gson.toJson(epic);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest postRequestEpic = HttpRequest.newBuilder()
                .uri(urlPostEpic)
                .POST(bodyEpic)
                .build();

        //post subtask to server
        URI urlPostSubTask = URI.create("http://localhost:8081/tasks/subtask/");

        String jsonSubTask = gson.toJson(subTask);
        HttpRequest.BodyPublisher bodySubTask = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest postRequestSubTask = HttpRequest.newBuilder()
                .uri(urlPostSubTask)
                .POST(bodySubTask)
                .build();

        //delete all tasks
        URI urlDeleteTasks = URI.create("http://localhost:8081/tasks/task/");

        HttpRequest deleteTasks = HttpRequest.newBuilder()
                .uri(urlDeleteTasks)
                .DELETE()
                .build();

        //get all tasks from server
        URI urlNewTask = URI.create("http://localhost:8081/tasks/task/");

        HttpRequest requestNewTask = HttpRequest.newBuilder()
                .uri(urlNewTask)
                .GET()
                .build();

        //get all epics from server
        URI urlNewEpic = URI.create("http://localhost:8081/tasks/epic/");

        HttpRequest requestNewEpic = HttpRequest.newBuilder()
                .uri(urlNewEpic)
                .GET()
                .build();

        //get all subtasks from server
        URI url = URI.create("http://localhost:8081/tasks/subtask/");

        HttpRequest requestNewSubTask = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(postRequestTask, HttpResponse.BodyHandlers.ofString());
        client.send(postRequestEpic, HttpResponse.BodyHandlers.ofString());
        client.send(postRequestSubTask, HttpResponse.BodyHandlers.ofString());

        client.send(deleteTasks, HttpResponse.BodyHandlers.ofString());

        HashMap<Integer, Task> shouldBe = new HashMap<>();

        HttpResponse<String> responseTask = client.send(requestNewTask, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, Task> tasks = gson.fromJson(responseTask.body(), new TypeToken<HashMap<Integer, Task>>() {}.getType());

        HttpResponse<String> responseEpic = client.send(requestNewEpic, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, Epic> epics = gson.fromJson(responseEpic.body(), new TypeToken<HashMap<Integer, Epic>>() {}.getType());

        HttpResponse<String> responseSubTask = client.send(requestNewSubTask, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, SubTask> subTasks = gson.fromJson(responseSubTask.body(), new TypeToken<HashMap<Integer, SubTask>>() {}.getType());

        Assertions.assertEquals(shouldBe, tasks);
        Assertions.assertEquals(shouldBe, epics);
        Assertions.assertEquals(shouldBe, subTasks);

    }

    @Test
    public void shouldReturnSubTasksOfEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2019-10-22T10:00"), 23);
        SubTask subTask1 = new SubTask("111", "222", TaskStatus.NEW, LocalDateTime.parse("2020-10-22T10:00"), 23, epic.getId());
        SubTask subTask2 = new SubTask("1111", "2222", TaskStatus.NEW, LocalDateTime.parse("2023-10-22T10:00"), 23, epic.getId());

        //post epic to server
        URI urlPost = URI.create("http://localhost:8081/tasks/epic/");

        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(urlPost)
                .POST(body)
                .build();

        //post subtask to server
        URI urlPostSubTask1 = URI.create("http://localhost:8081/tasks/subtask/");

        String jsonSubTask1 = gson.toJson(subTask1);
        HttpRequest.BodyPublisher bodySubTask1 = HttpRequest.BodyPublishers.ofString(jsonSubTask1);
        HttpRequest requestPostSubTask1 = HttpRequest.newBuilder()
                .uri(urlPostSubTask1)
                .POST(bodySubTask1)
                .build();

        //post subtask to server
        URI urlPostSubTask2 = URI.create("http://localhost:8081/tasks/subtask/");

        String jsonSubTask2 = gson.toJson(subTask2);
        HttpRequest.BodyPublisher bodySubTask2 = HttpRequest.BodyPublishers.ofString(jsonSubTask2);
        HttpRequest requestPostSubTask2 = HttpRequest.newBuilder()
                .uri(urlPostSubTask2)
                .POST(bodySubTask2)
                .build();

        //getting subtasks of epic from server by id
        URI url = URI.create("http://localhost:8081/tasks/subtask/epic/?id=" + epic.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //sending requests
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        client.send(requestPostSubTask1, HttpResponse.BodyHandlers.ofString());
        client.send(requestPostSubTask2, HttpResponse.BodyHandlers.ofString());

        ArrayList<SubTask> shouldBe = new ArrayList<>();
        shouldBe.add(subTask2);
        shouldBe.add(subTask1);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<SubTask> subTasks = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {}.getType());

        Assertions.assertNotNull(subTasks);
        Assertions.assertEquals(shouldBe, subTasks);

    }

    @Test
    public void shouldReturnHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task1 = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-10-22T10:00"), 23);
        Task task2 = new Task("2", "3", TaskStatus.NEW, LocalDateTime.parse("2001-10-22T10:00"), 23);
        Task task3 = new Task("3", "4", TaskStatus.NEW, LocalDateTime.parse("2000-10-22T10:00"), 23);

        //post task to server
        URI urlPost1 = URI.create("http://localhost:8081/tasks/task/");

        String json1 = gson.toJson(task1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest postRequest1 = HttpRequest.newBuilder()
                .uri(urlPost1)
                .POST(body1)
                .build();

        //post task to server
        URI urlPost2 = URI.create("http://localhost:8081/tasks/task/");

        String json2 = gson.toJson(task2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest postRequest2 = HttpRequest.newBuilder()
                .uri(urlPost2)
                .POST(body2)
                .build();

        //post task to server
        URI urlPost3 = URI.create("http://localhost:8081/tasks/task/");

        String json3 = gson.toJson(task3);
        HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json3);
        HttpRequest postRequest3 = HttpRequest.newBuilder()
                .uri(urlPost3)
                .POST(body3)
                .build();

        //get task from server by id
        URI urlGet1 = URI.create("http://localhost:8081/tasks/task/?id=" + task1.getId());

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(urlGet1)
                .GET()
                .build();

        //get task from server by id
        URI urlGet2 = URI.create("http://localhost:8081/tasks/task/?id=" + task2.getId());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(urlGet2)
                .GET()
                .build();

        //get task from server by id
        URI urlGet3 = URI.create("http://localhost:8081/tasks/task/?id=" + task3.getId());

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(urlGet3)
                .GET()
                .build();

        //get history
        URI urlGetHistory = URI.create("http://localhost:8081/tasks/history");

        HttpRequest requestHistory = HttpRequest.newBuilder()
                .uri(urlGetHistory)
                .GET()
                .build();

        //sending requests
        client.send(postRequest1, HttpResponse.BodyHandlers.ofString());
        client.send(postRequest2, HttpResponse.BodyHandlers.ofString());
        client.send(postRequest3, HttpResponse.BodyHandlers.ofString());

        client.send(request1, HttpResponse.BodyHandlers.ofString());
        client.send(request2, HttpResponse.BodyHandlers.ofString());
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        ArrayList<Task> shouldBe = new ArrayList<>();
        shouldBe.add(task1);
        shouldBe.add(task2);
        shouldBe.add(task3);

        HttpResponse<String> response = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> history = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {}.getType());


        Assertions.assertNotNull(history);
        Assertions.assertEquals(shouldBe, history);

    }

}