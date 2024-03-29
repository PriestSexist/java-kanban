package Manager.TaskManager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Storage.Storage;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

class HttpTaskManagerTest {

    Storage storage = new Storage();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager(storage);

    KVTaskClient kvTaskClient;
    KVServer kvServer;

    HttpTaskManager httpTaskManager;

    @BeforeEach
    public void serverStart() {
        try {
            this.kvServer = new KVServer();
        } catch (IOException e) {
            System.out.println("Или при запуске севера или при регистрации возникла ошибка");
        }
        this.kvTaskClient = new KVTaskClient("http://localhost:8078/", kvServer);
        this.httpTaskManager = new HttpTaskManager(inMemoryHistoryManager, storage, kvTaskClient);
    }

    @AfterEach
    public void serverEnd() {
        kvServer.stop();
    }

    @Test
    void shouldLoadDataFromServer() {

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);
        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        httpTaskManager.createTask(task);
        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubTask(subTask);

        httpTaskManager.load();

        Assertions.assertNotNull(storage.getTasks().get(task.getId()));
        Assertions.assertNotNull(storage.getEpics().get(epic.getId()));
        Assertions.assertNotNull(storage.getSubTasks().get(subTask.getId()));

        Assertions.assertEquals(task, storage.getTasks().get(task.getId()));
        Assertions.assertEquals(epic, storage.getEpics().get(epic.getId()));
        Assertions.assertEquals(subTask, storage.getSubTasks().get(subTask.getId()));
    }

    @Test
    void shouldCreateTaskOnServer() {
        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);

        httpTaskManager.createTask(task);

        httpTaskManager.load();

        Assertions.assertNotNull(storage.getTasks().get(task.getId()));
        Assertions.assertEquals(task, storage.getTasks().get(task.getId()));
    }

    @Test
    void shouldCreateEpicOnServer() {
        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:33"), 23);

        httpTaskManager.createEpic(epic);

        httpTaskManager.load();

        Assertions.assertNotNull(storage.getEpics().get(epic.getId()));
        Assertions.assertEquals(epic, storage.getEpics().get(epic.getId()));
    }

    @Test
    void shouldCreateSubTaskOnServer() {
        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubTask(subTask);

        httpTaskManager.load();

        Assertions.assertNotNull(storage.getSubTasks().get(subTask.getId()));
        Assertions.assertEquals(subTask, storage.getSubTasks().get(subTask.getId()));

    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);
        Task newTask = new Task("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:10"), 23);

        httpTaskManager.createTask(task);
        httpTaskManager.updateTask(newTask, task.getId());

        httpTaskManager.load();

        Assertions.assertNotNull(storage.getTasks().get(newTask.getId()));
        Assertions.assertNull(storage.getTasks().get(task.getId()));
        Assertions.assertEquals(newTask, storage.getTasks().get(newTask.getId()));
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);
        Epic newEpic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:10"), 23);

        httpTaskManager.createEpic(epic);
        httpTaskManager.updateEpic(newEpic, epic.getId());

        httpTaskManager.load();

        Assertions.assertNotNull(storage.getEpics().get(newEpic.getId()));
        Assertions.assertNull(storage.getEpics().get(epic.getId()));
        Assertions.assertEquals(newEpic, storage.getEpics().get(newEpic.getId()));
    }

    @Test
    void shouldUpdateSubTask() {
        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());
        SubTask newSubTask = new SubTask("QQ", "QQ", TaskStatus.NEW, LocalDateTime.parse("2003-11-11T11:56"), 23, epic.getId());

        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubTask(subTask);
        httpTaskManager.updateSubTask(newSubTask, subTask.getId());

        httpTaskManager.load();

        Assertions.assertNotNull(storage.getSubTasks().get(newSubTask.getId()));
        Assertions.assertNull(storage.getSubTasks().get(subTask.getId()));
        Assertions.assertEquals(newSubTask, storage.getSubTasks().get(newSubTask.getId()));
    }

    @Test
    void shouldGetTaskAndSaveItToHistory() {
        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);

        httpTaskManager.createTask(task);
        httpTaskManager.getTask(task.getId());

        httpTaskManager.load();

        Assertions.assertTrue(inMemoryHistoryManager.getTasks().contains(task));
    }

    @Test
    void shouldGetEpicAndSaveItToHistory() {
        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);

        httpTaskManager.createEpic(epic);
        httpTaskManager.getEpic(epic.getId());

        httpTaskManager.load();

        Assertions.assertTrue(inMemoryHistoryManager.getTasks().contains(epic));
    }

    @Test
    void shouldGetSubTaskAndSaveItToHistory() {
        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubTask(subTask);
        httpTaskManager.getSubTask(subTask.getId());

        httpTaskManager.load();

        Assertions.assertTrue(inMemoryHistoryManager.getTasks().contains(subTask));
    }

    @Test
    void deleteTask() {
        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);

        httpTaskManager.createTask(task);
        httpTaskManager.deleteTask(task.getId());

        httpTaskManager.load();

        Assertions.assertNull(storage.getTasks().get(task.getId()));
    }

    @Test
    void deleteEpic() {
        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);

        httpTaskManager.createEpic(epic);
        httpTaskManager.deleteEpic(epic.getId());

        httpTaskManager.load();

        Assertions.assertNull(storage.getEpics().get(epic.getId()));
    }

    @Test
    void deleteSubTask() {
        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubTask(subTask);
        httpTaskManager.deleteSubTask(subTask.getId());

        httpTaskManager.load();

        Assertions.assertNull(storage.getSubTasks().get(subTask.getId()));
    }

    @Test
    void deleteAllTasks() {
        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2000-11-11T11:10"), 23);
        Epic epic = new Epic("11", "22", TaskStatus.NEW, LocalDateTime.parse("2001-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        httpTaskManager.createTask(task);
        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubTask(subTask);

        httpTaskManager.deleteAllTasks();

        httpTaskManager.load();

        Assertions.assertNull(storage.getTasks().get(task.getId()));
        Assertions.assertNull(storage.getEpics().get(epic.getId()));
        Assertions.assertNull(storage.getSubTasks().get(subTask.getId()));

    }

}