package Manager.HistoryManager;

import Manager.TaskManager.InMemoryTaskManager;
import Tasks.Task;

public class InMemoryHistoryManager implements HistoryManager{

    private static int historyCounter = 0;

    InMemoryTaskManager inMemoryTaskManager;

    public InMemoryHistoryManager(InMemoryTaskManager inMemoryTaskManager) {
        this.inMemoryTaskManager = inMemoryTaskManager;
    }

    @Override
    public void getHistory() {
        for (Integer key : inMemoryTaskManager.storage.getHistoryMap().keySet()) {
            System.out.println(inMemoryTaskManager.storage.getHistoryMap().get(key).getId());
        }
    }

    public void add(Task task) {
        if (historyCounter >= 10) {
            historyCounter = 0;
        }
        inMemoryTaskManager.storage.getHistoryMap().put(historyCounter, task);
        historyCounter+=1;
    }
}
