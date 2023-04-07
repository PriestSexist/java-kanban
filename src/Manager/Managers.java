package Manager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.HistoryManager.HistoryManager;
import Manager.TaskManager.InMemoryTaskManager;
import Manager.TaskManager.TaskManager;

public class Managers {

    private static final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(inMemoryHistoryManager);

    public static TaskManager getDefault(){
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory(){
        return inMemoryHistoryManager;
    }
}
