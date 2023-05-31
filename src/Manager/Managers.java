package Manager;

import Manager.HistoryManager.HistoryManager;
import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.TaskManager.InMemoryTaskManager;
import Manager.TaskManager.TaskManager;
import Storage.Storage;

public class Managers {

    public static TaskManager getDefault(){
        return new InMemoryTaskManager(Managers.getDefaultHistory(), new Storage());
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}