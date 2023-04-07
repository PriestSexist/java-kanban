package Manager.HistoryManager;

import Storage.Node;
import Tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HistoryManager {

    Map<Integer, Node> historyMap = new HashMap<>();

    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
