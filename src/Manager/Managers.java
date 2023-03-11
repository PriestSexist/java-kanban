package Manager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.HistoryManager.HistoryManager;
import Manager.TaskManager.InMemoryTaskManager;
import Manager.TaskManager.TaskManager;

public class Managers {

    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager(inMemoryTaskManager);

    public static TaskManager getDefault(){
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory(){
        return inMemoryHistoryManager;
    }
}
