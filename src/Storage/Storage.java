package Storage;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class Storage {

    private static final HashMap<Integer, Epic> epics = new HashMap<>();
    private static final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private static final HashMap<Integer, Task> tasks = new HashMap<>();
    private static final Map<Integer, Node> historyMap = new HashMap<>();

    public static HashMap<Integer, Epic> getEpics() {
        return epics;
    }
    public static HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
    public static HashMap<Integer, Task> getTasks() {
        return tasks;
    }
    public static Map<Integer, Node> getHistoryMap() { return historyMap; }
}
