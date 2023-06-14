package Manager.TaskManager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Storage.Node;
import Storage.Storage;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class HttpTaskManager extends FileBackendTasksManager {

     /* Не лучше ли наследовать от InMemoryTaskManager?
     Если наследовать от FileBackendTasksManager, то тогда надо как-то бороться с тем, что путь для файла мы не передаём по тз,
     а save() через super вызывается. И это либо сохранять Таски только на сервере, что ограничивает возможности
     ТаскМенеджера из-за маленького функционала сервера (например, нельзя удалять). Либо, как вариант, всё же передавать путь для сохранения файлов.
     Либо большое дублирование кода. Это если ручками переносить код из InMemoryTaskManager */
    String uri;
    Gson gson = new Gson();
    KVTaskClient kvTaskClient;
    public HttpTaskManager(InMemoryHistoryManager inMemoryHistoryManager, String uri, Storage storage) {
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

        String history = gson.toJson(storage.getHistoryMap());
        kvTaskClient.put("history", history);

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subTasks);
        System.out.println(history);

    }

    public void load() {
        String resultTasks;
        resultTasks = kvTaskClient.load("tasks");
        HashMap<Integer, Task> tasks = gson.fromJson(resultTasks, new TypeToken<HashMap<Integer, Task>>() {}.getType());
        if (tasks != null){
            storage.getTasks().putAll(tasks);
        }

        resultTasks = kvTaskClient.load("epics");
        HashMap<Integer, Epic> epics = gson.fromJson(resultTasks, new TypeToken<HashMap<Integer, Epic>>() {}.getType());
        if (epics != null){
            storage.getEpics().putAll(epics);
        }

        resultTasks = kvTaskClient.load("subTasks");
        HashMap<Integer, SubTask> subTasks = gson.fromJson(resultTasks, new TypeToken<HashMap<Integer, SubTask>>() {}.getType());
        if (subTasks != null){
            storage.getSubTasks().putAll(subTasks);
        }

        resultTasks = kvTaskClient.load("history");
        HashMap<Integer, Node> history = gson.fromJson(resultTasks, new TypeToken<HashMap<Integer, Node>>() {}.getType());
        if (history != null){
            storage.getHistoryMap().putAll(history);
        }

        storage.getSortedTasks().clear();
        for (Task task : storage.getTasks().values()) {
            storage.getSortedTasks().add(task);
        }
        for (Epic epic : storage.getEpics().values()) {
            storage.getSortedTasks().add(epic);
        }
        for (SubTask subTask : storage.getSubTasks().values()) {
            storage.getSortedTasks().add(subTask);
        }

        /* ошибка: class Tasks.Task cannot be cast to class java.lang.Comparable (Tasks.Task is in unnamed module of loader 'app'; java.lang.Comparable is in module java.base of loader 'bootstrap')
        хз, чо с ней делать, так что, буду сначала загружать, а потом список формировать. Сл-но не буду сохранять это.
        resultTasks = kvTaskClient.load("prioritisedTasks");
        TreeSet<Task> prioritisedTasks = gson.fromJson(resultTasks, new TypeToken<TreeSet<Task>>() {}.getType());
        if (prioritisedTasks != null){
            storage.getSortedTasks().addAll(prioritisedTasks);
        }*/

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
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(int id) {
        return super.getEpic(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        return super.getSubTask(id);
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

    @Override
    public ArrayList<SubTask> gettingSubTasksOfEpic(int id) {
        return super.gettingSubTasksOfEpic(id);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }
}
