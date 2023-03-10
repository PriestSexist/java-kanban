package Manager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.HistoryManager.HistoryManager;
import Manager.TaskManager.InMemoryTaskManager;
import Manager.TaskManager.TaskManager;

public class Managers {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager(inMemoryTaskManager);

    public TaskManager getDefault(){
        return inMemoryTaskManager;
    }

    public HistoryManager getDefaultHistory(){
        return inMemoryHistoryManager;
    }
}
