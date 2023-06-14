package Storage;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Storage {

    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Node> historyMap = new HashMap<>();
    private final TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
    public Map<Integer, Node> getHistoryMap() {
        return historyMap;
    }
    public TreeSet<Task> getSortedTasks() {
        return sortedTasks;
    }

}
