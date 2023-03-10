package Manager.HistoryManager;

import Manager.TaskManager.InMemoryTaskManager;
import Tasks.Task;

public class InMemoryHistoryManager implements HistoryManager{

    InMemoryTaskManager inMemoryTaskManager;

    public InMemoryHistoryManager(InMemoryTaskManager inMemoryTaskManager) {
        this.inMemoryTaskManager = inMemoryTaskManager;
    }

    @Override
    public void getHistory() {
        for (Integer key : inMemoryTaskManager.storage.getHistory().keySet()) {
            System.out.println(inMemoryTaskManager.storage.getHistory().get(key).getId());
        }
    }
    @Override
    public void add(Task task) {

    }

}
