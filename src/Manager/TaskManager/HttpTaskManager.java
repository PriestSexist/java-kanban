package Manager.TaskManager;

import Manager.HistoryManager.HistoryManager;
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

    private final Gson gson = new Gson();
    private final KVTaskClient kvTaskClient;
    public HttpTaskManager(HistoryManager historyManager, String uri, Storage storage) {
        super(historyManager, storage);
        this.kvTaskClient = new KVTaskClient(uri);
    }

    // для тестов
    public HttpTaskManager(HistoryManager historyManager, Storage storage, KVTaskClient kvTaskClient) {
        super(historyManager, storage);
        this.kvTaskClient = kvTaskClient;
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

        String prioritisedTasks = gson.toJson(storage.getSortedTasks());
        kvTaskClient.put("prioritisedTasks", prioritisedTasks);

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subTasks);
        System.out.println(history);
        System.out.println(prioritisedTasks);

    }

    public void load() {
        String resultTasks;
        String resultEpics;
        String resultSubTasks;
        String resultHistory;
        String resultPrioritisedTasks;

        resultTasks = kvTaskClient.load("tasks");
        HashMap<Integer, Task> tasks = gson.fromJson(resultTasks, new TypeToken<HashMap<Integer, Task>>() {}.getType());
        if (tasks != null){
            storage.getTasks().putAll(tasks);
        }

        resultEpics = kvTaskClient.load("epics");
        HashMap<Integer, Epic> epics = gson.fromJson(resultEpics, new TypeToken<HashMap<Integer, Epic>>() {}.getType());
        if (epics != null){
            storage.getEpics().putAll(epics);
        }

        resultSubTasks = kvTaskClient.load("subTasks");
        HashMap<Integer, SubTask> subTasks = gson.fromJson(resultSubTasks, new TypeToken<HashMap<Integer, SubTask>>() {}.getType());
        if (subTasks != null){
            storage.getSubTasks().putAll(subTasks);
        }

        resultHistory = kvTaskClient.load("history");
        ArrayList<Task> history = gson.fromJson(resultHistory, new TypeToken<ArrayList<Task>>() {}.getType());
        if (history != null){
            for (Task task : history) {
                if (storage.getTasks().containsKey(task.getId())){
                    inMemoryHistoryManager.add(storage.getTasks().get(task.getId()));
                } else if (storage.getEpics().containsKey(task.getId())) {
                    inMemoryHistoryManager.add(storage.getEpics().get(task.getId()));
                } else if (storage.getSubTasks().containsKey(task.getId())){
                    inMemoryHistoryManager.add(storage.getSubTasks().get(task.getId()));
                }
            }
        }

        resultPrioritisedTasks = kvTaskClient.load("prioritisedTasks");
        TreeSet<Task> prioritisedTasks = gson.fromJson(resultPrioritisedTasks, new TypeToken<TreeSet<Task>>() {}.getType());
        if (prioritisedTasks != null){
            storage.getSortedTasks().addAll(prioritisedTasks);
        }

    }

}
