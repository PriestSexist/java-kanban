package Storage;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class Storage {

    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Node> historyMap = new HashMap<>();

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }
    public Map<Integer, Node> getHistoryMap() { return historyMap; }
}
