package Manager.TaskManager;

import Storage.Storage;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.HashMap;

public interface TaskManager {

    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Epic> getAllEpics();

    HashMap<Integer, SubTask> getAllSubTasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    Task getTask(int id);

    Epic getEpic(int id);
    SubTask getSubTask(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task, int oldId);

    void updateEpic(Epic epic, int oldId);

    void updateSubTask(SubTask subTask, int oldId);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);
    Storage getStorage();

}