package Manager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.HistoryManager.HistoryManager;
import Manager.TaskManager.FileBackedTasksManager;
import Manager.TaskManager.InMemoryTaskManager;
import Manager.TaskManager.TaskManager;

public class Managers {

    public static TaskManager getDefault(){
        return new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBackend() {
        return new FileBackedTasksManager(Managers.getDefaultHistory(), "C:\\test.txt");
    }
}