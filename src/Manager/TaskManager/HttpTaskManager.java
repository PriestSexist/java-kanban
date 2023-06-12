package Manager.TaskManager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Storage.Storage;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

public class HttpTaskManager extends FileBackendTasksManager {

     /* Не лучше ли наследовать от InMemoryTaskManager?
     Если наследовать от FileBackendTasksManager, то тогда надо как-то бороться с тем, что путь для файла мы не передаём по тз,
     а save() через super вызывается. И это либо сохранять Таски только на сервере, что ограничивает возможности
     ТаскМенеджера из-за маленького функционала сервера (например, нельзя удалять). Либо, как вариант, всё же передавать путь для сохранения файлов.
     Либо большое дублирование кода. Это если ручками переносить код из InMemoryTaskManager */
    String uri;
    Gson gson = new Gson();
    KVTaskClient kvTaskClient;
    public HttpTaskManager(InMemoryHistoryManager inMemoryHistoryManager, String uri, Storage storage) throws IOException {
        super(inMemoryHistoryManager, uri, storage);
        this.uri = uri;
        this.kvTaskClient = new KVTaskClient(uri);
    }

    @Override
    protected void save() {

        String tasks = gson.toJson(storage.getTasks());
        kvTaskClient.put("tasks", tasks);

        String epics = gson.toJson(storage.getEpics());
        kvTaskClient.put("epics", epics);

        String subTasks = gson.toJson(storage.getSubTasks());
        kvTaskClient.put("subTasks", subTasks);

        String history = gson.toJson(inMemoryHistoryManager.getTasks());
        kvTaskClient.put("history", history);

        String prioritisedTasks = gson.toJson(getPrioritizedTasks());
        kvTaskClient.put("prioritisedTasks", prioritisedTasks);

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subTasks);
        System.out.println(history);
        System.out.println(prioritisedTasks);

    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
    }

    @Override
    public Task getTask(int id) {
        //Чтобы история засчиталась. Мы же её не сохраняем на сервер
        super.getTask(id);
        String result = kvTaskClient.load("tasks");
        System.out.println("result");
        HashMap<Integer, Task> tasks = gson.fromJson(result, new TypeToken<HashMap<Integer, Task>>() {}.getType());
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        //Чтобы история засчиталась. Мы же её не сохраняем на сервер
        super.getEpic(id);
        String result = kvTaskClient.load(String.valueOf(id));
        return gson.fromJson(result, Epic.class);
    }

    @Override
    public SubTask getSubTask(int id) {
        //Чтобы история засчиталась. Мы же её не сохраняем на сервер
        super.getSubTask(id);
        String result = kvTaskClient.load(String.valueOf(id));
        return gson.fromJson(result, SubTask.class);
    }

    @Override
    public void updateTask(Task task, int oldId) {
        super.updateTask(task, oldId);
    }

    @Override
    public void updateEpic(Epic epic, int oldId) {
        super.updateEpic(epic, oldId);
    }

    @Override
    public void updateSubTask(SubTask subTask, int oldId) {
        super.updateSubTask(subTask, oldId);
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }
}
