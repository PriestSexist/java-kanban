package Manager.HistoryManager;

import Tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);
    void remove(int id);
    void removeAll();
    ArrayList<Task> getTasks();
}
