package Manager.TaskManager;

import Manager.Exceptions.ManagerSaveException;
import Manager.HistoryManager.InMemoryHistoryManager;
import Storage.Storage;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

class FileBackendTasksManagerTest extends InMemoryTaskManagerTest{

    Storage storage;
    @BeforeEach
    void reseter (){
        storage = new Storage();
    }

    @BeforeEach
    void fileBackendTasksManagerReseter(){
        Path pathToSave = Paths.get("./resources/test.txt");
        try (Writer fileWriter = new FileWriter("./resources/test.txt")) {

            if (Files.notExists(pathToSave)) {
                Files.createFile(pathToSave);
            }

            fileWriter.write("");

        } catch (IOException e) {
            throw new ManagerSaveException("IO ошибка");
        }

    }

    @Test
    void shouldLoadFromFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/loadtest.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 23);
        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:34"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:57"), 23, epic.getId());


        taskManager.createEpic(epic);
        taskManager.createSubTask(epic.getId(), subTask);

        Assertions.assertFalse(fileBackendTasksManager.storage.getTasks().isEmpty());
        Assertions.assertFalse(fileBackendTasksManager.storage.getSubTasks().isEmpty());
        Assertions.assertFalse(fileBackendTasksManager.storage.getEpics().isEmpty());

        Assertions.assertEquals(task, fileBackendTasksManager.getTask(task.getId()));
        Assertions.assertEquals(taskManager.getEpic(epic.getId()), fileBackendTasksManager.getEpic(epic.getId()));
        Assertions.assertEquals(subTask, fileBackendTasksManager.getSubTask(subTask.getId()));
    }

    @Test
    void shouldCreateTaskAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Task task = new Task("eat", "eat", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        fileBackendTasksManager.createTask(task);
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);
        Assertions.assertEquals(fileBackendTasksManager.getTask(task.getId()), task);
    }

    @Test
    void shouldCreateEpicAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:11"), 1);
        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);
        Assertions.assertEquals(fileBackendTasksManager.getEpic(epic.getId()), epic);

    }

    @Test
    void shouldCreateSubTaskAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask subTask = new SubTask("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:12"), 1, epic.getId());

        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager.createSubTask(epic.getId(), subTask);
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);
        Assertions.assertEquals(fileBackendTasksManager.getSubTask(subTask.getId()), subTask);
    }

    @Test
    void shouldGetTaskAndSaveItToFileHistory() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Task task = new Task("eat", "eat", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        fileBackendTasksManager.createTask(task);
        fileBackendTasksManager.getTask(task.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);
        //У fileBackendTasksManager менеджера есть свой inMemoryHistoryManager, но эта история унаследована от inMemoryTaskManager и служит только для записи в файл истории.
        //Для записи из файла в память мы передаём нужный inMemoryHistoryManager, таким образом, мы вызываем inMemoryHistoryManager от taskManager, а не от fileBackendTasksManager
        Assertions.assertTrue(taskManager.inMemoryHistoryManager.getTasks().contains(task));
    }

    @Test
    void shouldGetEpicAndSaveItToFileHistory() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("eat", "eat", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager.getEpic(epic.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);
        //У fileBackendTasksManager менеджера есть свой inMemoryHistoryManager, но эта история унаследована от inMemoryTaskManager и служит только для записи в файл истории.
        //Для записи из файла в память мы передаём нужный inMemoryHistoryManager, таким образом, мы вызываем inMemoryHistoryManager от taskManager, а не от fileBackendTasksManager
        Assertions.assertTrue(taskManager.inMemoryHistoryManager.getTasks().contains(epic));
    }

    @Test
    void shouldGetSubTaskAndSaveItToFileHistory() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("eat", "eat", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask subTask = new SubTask("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:12"), 1, epic.getId());

        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager.createSubTask(epic.getId(), subTask);
        fileBackendTasksManager.getSubTask(subTask.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);
        //У fileBackendTasksManager менеджера есть свой inMemoryHistoryManager, но эта история унаследована от inMemoryTaskManager и служит только для записи в файл истории.
        //Для записи из файла в память мы передаём нужный inMemoryHistoryManager, таким образом, мы вызываем inMemoryHistoryManager от taskManager, а не от fileBackendTasksManager
        Assertions.assertTrue(taskManager.inMemoryHistoryManager.getTasks().contains(subTask));
    }

    @Test
    void shouldUpdateTaskAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Task task = new Task("eat", "eat", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task task1 = new Task("ate", "ate", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:12"), 1);
        fileBackendTasksManager.createTask(task);
        fileBackendTasksManager.updateTask(task1, task.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Assertions.assertEquals(fileBackendTasksManager.getTask(task1.getId()), task1);
    }

    @Test
    void shouldUpdateEpicAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic epic1 = new Epic("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);
        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager.updateEpic(epic1, epic.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Assertions.assertEquals(fileBackendTasksManager.getEpic(epic1.getId()), epic1);
    }

    @Test
    void shouldUpdateSubTaskAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask subTask = new SubTask("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:12"), 1, epic.getId());
        SubTask subTask1 = new SubTask("2", "3", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:13"), 1, epic.getId());

        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager.createSubTask(epic.getId(), subTask);
        fileBackendTasksManager.updateSubTask(epic.getId(), subTask1, subTask.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Assertions.assertEquals(fileBackendTasksManager.getSubTask(subTask1.getId()), subTask1);
    }

    @Test
    void shouldDeleteTaskAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Task task = new Task("eat", "eat", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task testTask;

        fileBackendTasksManager.createTask(task);
        fileBackendTasksManager.deleteTask(task.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        testTask = fileBackendTasksManager.getTask(task.getId());

        Assertions.assertNull(testTask);
    }

    @Test
    void shouldDeleteEpicAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("eat", "eat", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic testEpic;

        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager.deleteEpic(epic.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        testEpic = fileBackendTasksManager.getEpic(epic.getId());

        Assertions.assertNull(testEpic);
    }

    @Test
    void shouldDeleteSubTaskAndSaveItToFile() {
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask subTask = new SubTask("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:12"), 1, epic.getId());
        SubTask testSubTask;

        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager.createSubTask(epic.getId(), subTask);
        fileBackendTasksManager.deleteSubTask(subTask.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        testSubTask = fileBackendTasksManager.getSubTask(subTask.getId());

        Assertions.assertNull(testSubTask);
    }

    @Test
    void shouldSaveChangedStatusToFile(){
        FileBackendTasksManager fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask subTask = new SubTask("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, epic.getId());
        SubTask subTask1 = new SubTask("1", "2", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:13"), 1, epic.getId());

        fileBackendTasksManager.createEpic(epic);
        fileBackendTasksManager.createSubTask(epic.getId(), subTask);
        fileBackendTasksManager.updateSubTask(epic.getId(), subTask1, subTask.getId());
        fileBackendTasksManager = FileBackendTasksManager.loadFromFile("./resources/test.txt", (InMemoryHistoryManager) taskManager.inMemoryHistoryManager, storage);

        Assertions.assertEquals(fileBackendTasksManager.getEpic(epic.getId()).getStatus(), TaskStatus.IN_PROGRESS);
    }

}