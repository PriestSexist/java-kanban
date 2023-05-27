package Manager.TaskManager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

class TaskManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager;
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void managerCreator(){
        inMemoryHistoryManager = new InMemoryHistoryManager();
        inMemoryTaskManager = new InMemoryTaskManager(inMemoryHistoryManager);
    }

    @Test
    void shouldReturnHashMapWithAllTasks() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task secondTaskForTest = new Task("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        inMemoryTaskManager.createTask(firstTaskForTest);
        inMemoryTaskManager.createTask(secondTaskForTest);

        HashMap<Integer, Task> shouldBeMap = new HashMap<>();
        shouldBeMap.put(firstTaskForTest.getId(), firstTaskForTest);
        shouldBeMap.put(secondTaskForTest.getId(), secondTaskForTest);

        HashMap<Integer, Task> tasksMap = inMemoryTaskManager.getAllTasks();

        // проверка поведения с пустым списком задач
        Assertions.assertNotNull(inMemoryTaskManager.getAllTasks());
        Assertions.assertEquals(2, inMemoryTaskManager.getAllTasks().size());

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных тасков
        Assertions.assertEquals(shouldBeMap.keySet(), tasksMap.keySet());

    }

    @Test
    void shouldReturnHashMapWithAllEpics() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic secondEpicForTest = new Epic("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createEpic(secondEpicForTest);

        HashMap<Integer, Epic> shouldBeMap = new HashMap<>();
        shouldBeMap.put(firstEpicForTest.getId(), firstEpicForTest);
        shouldBeMap.put(secondEpicForTest.getId(), secondEpicForTest);

        HashMap<Integer, Epic> tasksMap = inMemoryTaskManager.getAllEpics();

        Assertions.assertNotNull(inMemoryTaskManager.getAllEpics());
        Assertions.assertEquals(2, inMemoryTaskManager.getAllEpics().size());

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных эпиков
        Assertions.assertEquals(shouldBeMap.keySet(), tasksMap.keySet());
    }

    @Test
    void shouldReturnHashMapWithAllSubTasks() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask secondSubTaskForTest = new SubTask("w", "w", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), secondSubTaskForTest);

        HashMap<Integer, SubTask> shouldBeMap = new HashMap<>();
        shouldBeMap.put(firstSubTaskForTest.getId(), firstSubTaskForTest);
        shouldBeMap.put(secondSubTaskForTest.getId(), secondSubTaskForTest);

        HashMap<Integer, SubTask> tasksMap = inMemoryTaskManager.getAllSubTasks();

        Assertions.assertNotNull(inMemoryTaskManager.getAllSubTasks());
        Assertions.assertEquals(2, inMemoryTaskManager.getAllSubTasks().size());

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных эпиков
        Assertions.assertEquals(shouldBeMap.keySet(), tasksMap.keySet());
    }

    @Test
    void shouldClearHashMapWithAllTasks() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task secondTaskForTest = new Task("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        inMemoryTaskManager.createTask(firstTaskForTest);
        inMemoryTaskManager.createTask(secondTaskForTest);

        HashMap<Integer, Task> shouldBeMap = new HashMap<>();
        inMemoryTaskManager.deleteAllTasks();

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных тасков
        Assertions.assertEquals(shouldBeMap.keySet(), inMemoryTaskManager.getAllTasks().keySet());
    }

    @Test
    void shouldClearHashMapWithAllEpics() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic secondEpicForTest = new Epic("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createEpic(secondEpicForTest);

        HashMap<Integer, Epic> shouldBeMap = new HashMap<>();
        inMemoryTaskManager.deleteAllEpics();

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных тасков
        Assertions.assertEquals(shouldBeMap.keySet(), inMemoryTaskManager.getAllEpics().keySet());
    }

    @Test
    void shouldClearHashMapWithAllSubTasks() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask secondSubTaskForTest = new SubTask("w", "w", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), secondSubTaskForTest);

        HashMap<Integer, SubTask> shouldBeMap = new HashMap<>();
        inMemoryTaskManager.deleteAllSubTasks();

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных тасков
        Assertions.assertEquals(shouldBeMap.keySet(), inMemoryTaskManager.getAllSubTasks().keySet());
    }

    @Test
    void shouldCreateAndReturnTask() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);

        inMemoryTaskManager.createTask(firstTaskForTest);
        Task task1 = inMemoryTaskManager.getTask(firstTaskForTest.getId());
        Task task2 = inMemoryTaskManager.getTask(12345);

        Assertions.assertNotNull(task1);
        Assertions.assertNull(task2);
        Assertions.assertEquals(task1, firstTaskForTest);
    }

    @Test
    void shouldCreateAndReturnEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);

        inMemoryTaskManager.createEpic(firstEpicForTest);
        Epic epic1 = inMemoryTaskManager.getEpic(firstEpicForTest.getId());
        Epic epic2 = inMemoryTaskManager.getEpic(12345);

        Assertions.assertNotNull(epic1);
        Assertions.assertNull(epic2);
        Assertions.assertEquals(epic1, firstEpicForTest);
    }

    @Test
    void shouldCreateAndReturnSubTask() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);

        SubTask subTask1 = inMemoryTaskManager.getSubTask(firstSubTaskForTest.getId());
        SubTask subTask2 = inMemoryTaskManager.getSubTask(12345);

        Assertions.assertNotNull(subTask1);
        Assertions.assertNull(subTask2);
        Assertions.assertEquals(subTask1.getParentId(), firstEpicForTest.getId());
        Assertions.assertEquals(subTask1, firstSubTaskForTest);

    }

    @Test
    void shouldAddSubTaskToEpic(){
        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);

        // проверяю есть ли нужный ID сабтаск в листе его эпика
        Assertions.assertTrue(firstEpicForTest.getSubTasks().contains(firstSubTaskForTest.getId()));
    }

    @Test
    void shouldUpdateTask() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task secondTaskForTest = new Task("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        inMemoryTaskManager.createTask(firstTaskForTest);

        inMemoryTaskManager.updateTask(secondTaskForTest, firstTaskForTest.getId());
        Task taskAfterUpdate1 = inMemoryTaskManager.getTask(secondTaskForTest.getId());

        Assertions.assertNotNull(taskAfterUpdate1);
        Assertions.assertEquals(taskAfterUpdate1, secondTaskForTest);
    }

    @Test
    void  shouldUpdateEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic secondEpicForTest = new Epic("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        inMemoryTaskManager.createEpic(firstEpicForTest);

        inMemoryTaskManager.updateEpic(secondEpicForTest, firstEpicForTest.getId());
        Epic epicAfterUpdate = inMemoryTaskManager.getEpic(secondEpicForTest.getId());

        Assertions.assertNotNull(epicAfterUpdate);
        Assertions.assertEquals(epicAfterUpdate, secondEpicForTest);
    }

    @Test
    void shouldUpdateSubTaskAndUpdateSubTaskToEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask secondSubTaskForTest = new SubTask("w", "w", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);

        inMemoryTaskManager.updateSubTask(firstEpicForTest.getId(), secondSubTaskForTest, firstSubTaskForTest.getId());
        Epic epicAfterUpdate = inMemoryTaskManager.getEpic(firstEpicForTest.getId());
        SubTask subTaskAfterUpdate = inMemoryTaskManager.getSubTask(secondSubTaskForTest.getId());

        Assertions.assertTrue(epicAfterUpdate.getSubTasks().contains(secondSubTaskForTest.getId()));
        Assertions.assertNotNull(subTaskAfterUpdate);
        Assertions.assertEquals(subTaskAfterUpdate, secondSubTaskForTest);
    }

    @Test
    void shouldDeleteTask() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);

        inMemoryTaskManager.createTask(firstTaskForTest);
        inMemoryTaskManager.deleteTask(firstTaskForTest.getId());

        Assertions.assertNull(inMemoryTaskManager.getTask(firstTaskForTest.getId()));

    }

    @Test
    void shouldDeleteEpicAndConnectedSubTask() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);

        inMemoryTaskManager.deleteEpic(firstEpicForTest.getId());

        Assertions.assertNull(inMemoryTaskManager.getEpic(firstEpicForTest.getId()));
        Assertions.assertNull(inMemoryTaskManager.getSubTask(firstSubTaskForTest.getId()));
    }

    @Test
    void ShouldDeleteSubTaskAndConnectionToEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);
        inMemoryTaskManager.deleteSubTask(firstSubTaskForTest.getId());

        Assertions.assertFalse(inMemoryTaskManager.getEpic(firstEpicForTest.getId()).getSubTasks().contains(firstSubTaskForTest.getId()));
        Assertions.assertNull(inMemoryTaskManager.getSubTask(firstSubTaskForTest.getId()));
    }


}