package Manager.TaskManager;

import Manager.HistoryManager.HistoryManager;
import Manager.Managers;
import Storage.EndPoint;
import Storage.Storage;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class HttpTaskServer {

    private static final int PORT = 8081;
    HttpServer httpServer;

    public void start() throws IOException {
        httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);

        System.out.println("HTTP-сервер остановлен");
    }

    static class TasksHandler implements HttpHandler {

        TaskManager httpTaskManager = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        Storage storage = Managers.getDefaultStorage();
        private static final Gson gson = new Gson();
        private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().toString();
            EndPoint endpoint = getEndpoint(requestPath, exchange.getRequestMethod());

            // http://localhost:8081/tasks/tasks/?id=100
            switch (endpoint) {
                case GET_ALL_TASKS: {
                    handleGetAllTasks(exchange);
                    break;
                }
                case GET_ALL_EPICS: {
                    handleGetAllEpics(exchange);
                    break;
                }
                case GET_ALL_SUBTASKS: {
                    handleGetAllSubTasks(exchange);
                    break;
                }
                case GET_TASK_BY_ID: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handleGetTaskByID(exchange, id);
                    }
                    break;
                }
                case GET_EPIC_BY_ID: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handleGetEpicByID(exchange, id);
                    }
                    break;
                }
                case GET_SUBTASK_BY_ID: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handleGetSubTaskByID(exchange, id);
                    }
                    break;
                }
                case POST_NEW_TASK: {
                    handlePostTask(exchange);
                    break;
                }
                case POST_NEW_EPIC: {
                    handlePostEpic(exchange);
                    break;
                }
                case POST_NEW_SUBTASK: {
                    handlePostSubTask(exchange);
                    break;
                }
                case POST_UPDATED_TASK: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handlePostUpdateTask(exchange, id);
                    }
                    break;
                }
                case POST_UPDATED_EPIC: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handlePostUpdateEpic(exchange, id);
                    }
                    break;
                }
                case POST_UPDATED_SUBTASK: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handlePostUpdateSubTask(exchange, id);
                    }
                    break;
                }
                case DELETE_TASK: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handleDeleteTask(exchange, id);
                    }
                    break;
                }
                case DELETE_EPIC: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handleDeleteEpic(exchange, id);
                    }
                    break;
                }
                case DELETE_SUBTASK: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handleDeleteSubTask(exchange, id);
                    }
                    break;
                }
                case DELETE_ALL_TASKS: {
                    handleDeleteAllTasks(exchange);
                    break;
                }
                case GET_ALL_SUBTASKS_OF_ONE_EPIC: {
                    Optional<Integer> idOpt = getTaskId(exchange);

                    if(idOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор поста", 400);
                    } else {
                        int id = idOpt.get();
                        handleGetSubTasksOfEpic(exchange, id);
                    }
                    break;
                }
                case GET_PRIORITISED_TASKS: {
                    handleGetPrioritisedTasks(exchange);
                    break;
                }
                case GET_HISTORY: {
                    handleGetHistory(exchange);
                    break;
                }
                default:{
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
                }
            }
        }

        private Optional<Integer> getTaskId(HttpExchange exchange) {
            // не придумал, как лучше достать ID из строки
            String params = exchange.getRequestURI().getQuery();
            String[] pathParts = params.split("=");
            try {
                return Optional.of(Integer.parseInt(pathParts[1]));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }

        private EndPoint getEndpoint(String requestPath, String requestMethod) {
            String[] pathParts = requestPath.split("/");

            switch (requestMethod){
                case "GET":
                    if (pathParts.length == 3 && pathParts[2].equals("task"))
                        return EndPoint.GET_ALL_TASKS;
                    if (pathParts.length == 3 && pathParts[2].equals("epic"))
                        return EndPoint.GET_ALL_EPICS;
                    if (pathParts.length == 3 && pathParts[2].equals("subtask"))
                        return EndPoint.GET_ALL_SUBTASKS;
                    if (pathParts.length == 4 && pathParts[2].equals("task"))
                        return EndPoint.GET_TASK_BY_ID;
                    if (pathParts.length == 4 && pathParts[2].equals("epic"))
                        return EndPoint.GET_EPIC_BY_ID;
                    if (pathParts.length == 4 && pathParts[2].equals("subtask"))
                        return EndPoint.GET_SUBTASK_BY_ID;
                    if (pathParts.length == 2)
                        return EndPoint.GET_PRIORITISED_TASKS;
                    if (pathParts.length == 3 && pathParts[2].equals("history"))
                        return EndPoint.GET_HISTORY;
                    if (pathParts.length == 5 && pathParts[2].equals("subtask") && pathParts[3].equals("epic"))
                        return EndPoint.GET_ALL_SUBTASKS_OF_ONE_EPIC;

                case "POST":
                    if (pathParts.length == 3 && pathParts[2].equals("task"))
                        return EndPoint.POST_NEW_TASK;
                    if (pathParts.length == 3 && pathParts[2].equals("epic"))
                        return EndPoint.POST_NEW_EPIC;
                    if (pathParts.length == 3 && pathParts[2].equals("subtask"))
                        return EndPoint.POST_NEW_SUBTASK;
                    if (pathParts.length == 4 && pathParts[2].equals("task"))
                        return EndPoint.POST_UPDATED_TASK;
                    if (pathParts.length == 4 && pathParts[2].equals("epic"))
                        return EndPoint.POST_UPDATED_EPIC;
                    if (pathParts.length == 4 && pathParts[2].equals("subtask"))
                        return EndPoint.POST_UPDATED_SUBTASK;

                case "DELETE":
                    if (pathParts.length == 3 && pathParts[2].equals("task"))
                        return EndPoint.DELETE_ALL_TASKS;
                    if (pathParts.length == 4 && pathParts[2].equals("task"))
                        return EndPoint.DELETE_TASK;
                    if (pathParts.length == 4 && pathParts[2].equals("epic"))
                        return EndPoint.DELETE_EPIC;
                    if (pathParts.length == 4 && pathParts[2].equals("subtask"))
                        return EndPoint.DELETE_SUBTASK;

                default:
                    return EndPoint.UNKNOWN;
            }
        }

        private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {

            if(responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        private void handleGetAllTasks(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(httpTaskManager.getAllTasks()), 200);
        }
        private void handleGetAllEpics(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(httpTaskManager.getAllEpics()), 200);
        }
        private void handleGetAllSubTasks(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(httpTaskManager.getAllSubTasks()), 200);
        }
        private void handleGetTaskByID(HttpExchange exchange, int id) throws IOException {
            writeResponse(exchange, gson.toJson(httpTaskManager.getTask(id)), 200);
        }
        private void handleGetEpicByID(HttpExchange exchange, int id) throws IOException {
            writeResponse(exchange, gson.toJson(httpTaskManager.getEpic(id)), 200);
        }
        private void handleGetSubTaskByID(HttpExchange exchange, int id) throws IOException {
            writeResponse(exchange, gson.toJson(httpTaskManager.getSubTask(id)), 200);
        }
        private void handlePostTask(HttpExchange exchange) throws IOException {

            String body = new String (exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task taskFromJson;
            Task task;

            try {
                taskFromJson = gson.fromJson(body, Task.class);
            } catch (JsonSyntaxException exception){
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            // Для автогенерации некоторых параметров как ID
            task = new Task(taskFromJson.getName(), taskFromJson.getDescription(), taskFromJson.getStartTime(), taskFromJson.getDuration());

            httpTaskManager.createTask(task);

            writeResponse(exchange, "Задача добавлена", 201);
        }

        private void handlePostEpic(HttpExchange exchange) throws IOException {

            String body = new String (exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epicFromJson;
            Epic epic;

            try {
                epicFromJson = gson.fromJson(body, Epic.class);
            } catch (JsonSyntaxException exception){
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            epic = new Epic(epicFromJson.getName(), epicFromJson.getDescription(), epicFromJson.getStartTime(), epicFromJson.getDuration());

            httpTaskManager.createEpic(epic);

            writeResponse(exchange, "Задача добавлена", 201);
        }

        private void handlePostSubTask(HttpExchange exchange) throws IOException {

            String body = new String (exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            SubTask subTaskFromJson;
            SubTask subTask;

            try {
                subTaskFromJson = gson.fromJson(body, SubTask.class);
            } catch (JsonSyntaxException exception){
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            subTask = new SubTask(subTaskFromJson.getName(), subTaskFromJson.getDescription(), subTaskFromJson.getStartTime(), subTaskFromJson.getDuration(), subTaskFromJson.getParentId());

            if (storage.getEpics().containsKey(subTaskFromJson.getParentId())){
                httpTaskManager.createSubTask(subTask);
            } else {
                System.out.println("Эпика для данного сабтаска не существует");
            }

            writeResponse(exchange, "Задача добавлена", 201);
        }

        private void handlePostUpdateTask(HttpExchange exchange, int id) throws IOException {

            String body = new String (exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task taskFromJson;
            Task task;

            try {
                taskFromJson = gson.fromJson(body, Task.class);
            } catch (JsonSyntaxException exception){
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            if (storage.getTasks().containsKey(id)) {
                task = new Task(taskFromJson.getName(), taskFromJson.getDescription(), taskFromJson.getStatus(), taskFromJson.getStartTime(), taskFromJson.getDuration());
                httpTaskManager.updateTask(task, id);
                writeResponse(exchange, "Таск обновлён", 200);
                return;
            }

            writeResponse(exchange, "Таск с идентификатором " + id + " не найден", 404);
        }

        private void handlePostUpdateEpic(HttpExchange exchange, int id) throws IOException {

            String body = new String (exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epicFromJson;
            Epic epic;

            try {
                epicFromJson = gson.fromJson(body, Epic.class);
            } catch (JsonSyntaxException exception){
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }

            if (storage.getEpics().containsKey(id)) {
                epic = new Epic(epicFromJson.getName(), epicFromJson.getDescription(), epicFromJson.getStatus(), epicFromJson.getStartTime(), epicFromJson.getDuration());
                httpTaskManager.updateEpic(epic, id);
                writeResponse(exchange, "Эпик обновлён", 200);
                return;
            }

            writeResponse(exchange, "Эпик с идентификатором " + id + " не найден", 404);
        }

        private void handlePostUpdateSubTask(HttpExchange exchange, int id) throws IOException {

            String body = new String (exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            SubTask subTaskFromJson;
            SubTask subTask;

            try {
                subTaskFromJson = gson.fromJson(body, SubTask.class);
            } catch (JsonSyntaxException exception){
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }

            if (storage.getSubTasks().containsKey(id)) {
                subTask = new SubTask(subTaskFromJson.getName(), subTaskFromJson.getDescription(), subTaskFromJson.getStatus(), subTaskFromJson.getStartTime(), subTaskFromJson.getDuration(), subTaskFromJson.getParentId());
                httpTaskManager.updateSubTask(subTask, id);
                writeResponse(exchange, "Сабтаск обновлён", 200);
                return;
            }
            writeResponse(exchange, "СабТаск с идентификатором " + id + " не найден", 404);
        }

        private void handleDeleteTask(HttpExchange exchange, int id) throws IOException {

            if (storage.getTasks().containsKey(id)) {
                httpTaskManager.deleteTask(id);
                writeResponse(exchange, "Tаск удалён", 200);
                return;
            }
            writeResponse(exchange, "Таск с идентификатором " + id + " не найден", 404);
        }

        private void handleDeleteEpic(HttpExchange exchange, int id) throws IOException {

            if (storage.getEpics().containsKey(id)) {
                httpTaskManager.deleteEpic(id);
                writeResponse(exchange, "Эпик удалён", 200);
                return;
            }
            writeResponse(exchange, "Эпик с идентификатором " + id + " не найден", 404);
        }

        private void handleDeleteSubTask(HttpExchange exchange, int id) throws IOException {

            if (storage.getSubTasks().containsKey(id)) {
                httpTaskManager.deleteSubTask(id);
                writeResponse(exchange, "Сабтаск удалён", 200);
                return;
            }
            writeResponse(exchange, "СабТаск с идентификатором " + id + " не найден", 404);
        }

        private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
            httpTaskManager.deleteAllTasks();
            writeResponse(exchange, "Все задачи удалены", 200);
        }

        private void handleGetSubTasksOfEpic(HttpExchange exchange, int id) throws IOException {

            if (storage.getEpics().containsKey(id)) {
                writeResponse(exchange, gson.toJson(httpTaskManager.gettingSubTasksOfEpic(id)), 200);
                return;
            }

            writeResponse(exchange, "Эпик с идентификатором " + id + " не найден", 404);
        }

        private void handleGetPrioritisedTasks(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(httpTaskManager.getPrioritizedTasks()), 200);
        }

        private void handleGetHistory(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(inMemoryHistoryManager.getTasks()), 200);
        }
    }
}
