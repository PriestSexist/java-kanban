package Manager;

import Manager.HistoryManager.HistoryManager;
import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.TaskManager.FileBackendTasksManager;
import Manager.TaskManager.HttpTaskManager;
import Manager.TaskManager.TaskManager;
import Storage.Storage;

public class Managers {

    /*Изменил тут всё, чтобы объекты были статичными, чтобы можно было их использовать на сервере
    без передачи доп параметров при запуске сервера. +добавил getDefaultFileBackend() по тз */
    private static final Storage storage = new Storage();
    private static final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager(storage);
    private static final FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/save.txt", inMemoryHistoryManager, storage);
    private static final HttpTaskManager httpTaskManager = new HttpTaskManager(inMemoryHistoryManager, "http://localhost:8078/", storage);
    public static TaskManager getDefault(){
        return httpTaskManager;
    }
    public static Storage getDefaultStorage(){
        return storage;
    }
    public static HistoryManager getDefaultHistory(){
        return inMemoryHistoryManager;
    }
    public static FileBackendTasksManager getDefaultFileBackend(){
        return fileBackendTasksManager;
    }
}