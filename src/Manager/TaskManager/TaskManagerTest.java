package Manager.TaskManager;

import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManagerDefault;
    T taskManager;

    public TaskManagerTest(T taskManager) {
        this.taskManagerDefault = taskManager;
    }

    @BeforeEach
    public void managerCreator(){
        taskManager = taskManagerDefault;
    }

    @Test
    void shouldReturnHashMapWithAllTasks() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task secondTaskForTest = new Task("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        taskManager.createTask(firstTaskForTest);
        taskManager.createTask(secondTaskForTest);

        HashMap<Integer, Task> shouldBeMap = new HashMap<>();
        shouldBeMap.put(firstTaskForTest.getId(), firstTaskForTest);
        shouldBeMap.put(secondTaskForTest.getId(), secondTaskForTest);

        HashMap<Integer, Task> tasksMap = taskManager.getAllTasks();

        // проверка поведения с пустым списком задач
        Assertions.assertNotNull(taskManager.getAllTasks());
        Assertions.assertEquals(2, taskManager.getAllTasks().size());

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных тасков
        Assertions.assertEquals(shouldBeMap.keySet(), tasksMap.keySet());

    }

    @Test
    void shouldReturnHashMapWithAllEpics() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic secondEpicForTest = new Epic("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        taskManager.createEpic(firstEpicForTest);
        taskManager.createEpic(secondEpicForTest);

        HashMap<Integer, Epic> shouldBeMap = new HashMap<>();
        shouldBeMap.put(firstEpicForTest.getId(), firstEpicForTest);
        shouldBeMap.put(secondEpicForTest.getId(), secondEpicForTest);

        HashMap<Integer, Epic> tasksMap = taskManager.getAllEpics();

        Assertions.assertNotNull(taskManager.getAllEpics());
        Assertions.assertEquals(2, taskManager.getAllEpics().size());

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных эпиков
        Assertions.assertEquals(shouldBeMap.keySet(), tasksMap.keySet());
    }

    @Test
    void shouldReturnHashMapWithAllSubTasks() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask secondSubTaskForTest = new SubTask("w", "w", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, firstEpicForTest.getId());

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), secondSubTaskForTest);

        HashMap<Integer, SubTask> shouldBeMap = new HashMap<>();
        shouldBeMap.put(firstSubTaskForTest.getId(), firstSubTaskForTest);
        shouldBeMap.put(secondSubTaskForTest.getId(), secondSubTaskForTest);

        HashMap<Integer, SubTask> tasksMap = taskManager.getAllSubTasks();

        Assertions.assertNotNull(taskManager.getAllSubTasks());
        Assertions.assertEquals(2, taskManager.getAllSubTasks().size());

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных эпиков
        Assertions.assertEquals(shouldBeMap.keySet(), tasksMap.keySet());
    }

    @Test
    void shouldClearHashMapWithAllTasks() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task secondTaskForTest = new Task("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:14"), 1, firstEpicForTest.getId());
        SubTask secondSubTaskForTest = new SubTask("w", "w", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:15"), 1, firstEpicForTest.getId());

        taskManager.createTask(firstTaskForTest);
        taskManager.createTask(secondTaskForTest);
        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), secondSubTaskForTest);

        HashMap<Integer, Task> shouldBeMap = new HashMap<>();
        taskManager.deleteAllTasks();

        // Сравниваю по ключам, так как ключи - хеши, хеши уникальные и делаются на основе данных тасков
        Assertions.assertEquals(shouldBeMap.keySet(), taskManager.getAllTasks().keySet());
        Assertions.assertEquals(shouldBeMap.keySet(), taskManager.getAllEpics().keySet());
        Assertions.assertEquals(shouldBeMap.keySet(), taskManager.getAllSubTasks().keySet());
    }

    @Test
    void shouldCreateAndReturnTask() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task task1 = null;
        Task task2 = null;

        taskManager.createTask(firstTaskForTest);
        if (taskManager.getStorage().getTasks().containsKey(firstTaskForTest.getId())) {
            task1 = taskManager.getTask(firstTaskForTest.getId());
        }
        if (taskManager.getStorage().getTasks().containsKey(12345)) {
            task2 = taskManager.getTask(12345);
        }

        Assertions.assertNotNull(task1);
        Assertions.assertNull(task2);
        Assertions.assertEquals(task1, firstTaskForTest);
    }

    @Test
    void shouldCreateAndReturnEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic epic1 = null;
        Epic epic2 = null;

        taskManager.createEpic(firstEpicForTest);
        if (taskManager.getStorage().getEpics().containsKey(firstEpicForTest.getId())) {
            epic1 = taskManager.getEpic(firstEpicForTest.getId());
        }
        if (taskManager.getStorage().getEpics().containsKey(12345)) {
            epic2 = taskManager.getEpic(12345);
        }



        Assertions.assertNotNull(epic1);
        Assertions.assertNull(epic2);
        Assertions.assertEquals(epic1, firstEpicForTest);
    }

    @Test
    void shouldCreateAndReturnSubTask() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask subTask1 = null;
        SubTask subTask2 = null;

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);

        if (taskManager.getStorage().getSubTasks().containsKey(firstSubTaskForTest.getId())){
            subTask1 = taskManager.getSubTask(firstSubTaskForTest.getId());
        }
        if (taskManager.getStorage().getSubTasks().containsKey(12345)){
            subTask2 = taskManager.getSubTask(12345);
        }

        Assertions.assertNotNull(subTask1);
        Assertions.assertNull(subTask2);
        Assertions.assertEquals(subTask1.getParentId(), firstEpicForTest.getId());
        Assertions.assertEquals(subTask1, firstSubTaskForTest);

    }

    @Test
    void shouldAddSubTaskToEpic(){
        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);

        // проверяю есть ли нужный ID сабтаск в листе его эпика
        Assertions.assertTrue(firstEpicForTest.getSubTasks().contains(firstSubTaskForTest.getId()));
    }

    @Test
    void shouldUpdateTask() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task secondTaskForTest = new Task("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        taskManager.createTask(firstTaskForTest);

        taskManager.updateTask(secondTaskForTest, firstTaskForTest.getId());
        Task taskAfterUpdate1 = taskManager.getTask(secondTaskForTest.getId());

        Assertions.assertNotNull(taskAfterUpdate1);
        Assertions.assertEquals(taskAfterUpdate1, secondTaskForTest);
    }

    @Test
    void  shouldUpdateEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic secondEpicForTest = new Epic("2", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        taskManager.createEpic(firstEpicForTest);

        taskManager.updateEpic(secondEpicForTest, firstEpicForTest.getId());
        Epic epicAfterUpdate = taskManager.getEpic(secondEpicForTest.getId());

        Assertions.assertNotNull(epicAfterUpdate);
        Assertions.assertEquals(epicAfterUpdate, secondEpicForTest);
    }

    @Test
    void shouldUpdateSubTaskAndUpdateSubTaskToEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask secondSubTaskForTest = new SubTask("w", "w", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, firstEpicForTest.getId());

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);

        taskManager.updateSubTask(firstEpicForTest.getId(), secondSubTaskForTest, firstSubTaskForTest.getId());
        Epic epicAfterUpdate = taskManager.getEpic(firstEpicForTest.getId());
        SubTask subTaskAfterUpdate = taskManager.getSubTask(secondSubTaskForTest.getId());

        Assertions.assertTrue(epicAfterUpdate.getSubTasks().contains(secondSubTaskForTest.getId()));
        Assertions.assertNotNull(subTaskAfterUpdate);
        Assertions.assertEquals(subTaskAfterUpdate, secondSubTaskForTest);
    }

    @Test
    void shouldDeleteTask() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task task1 = null;

        taskManager.createTask(firstTaskForTest);
        taskManager.deleteTask(firstTaskForTest.getId());

        if (taskManager.getStorage().getTasks().containsKey(firstTaskForTest.getId())) {
            task1 = taskManager.getTask(firstTaskForTest.getId());
        }
        Assertions.assertNull(task1);

    }

    @Test
    void shouldDeleteEpicAndConnectedSubTask() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        Epic epic = null;
        SubTask subTask = null;

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);

        taskManager.deleteEpic(firstEpicForTest.getId());

        if (taskManager.getStorage().getEpics().containsKey(firstEpicForTest.getId())) {
            epic = taskManager.getEpic(firstEpicForTest.getId());
        }
        if (taskManager.getStorage().getSubTasks().containsKey(firstSubTaskForTest.getId())) {
            subTask = taskManager.getSubTask(firstSubTaskForTest.getId());
        }
        Assertions.assertNull(epic);
        Assertions.assertNull(subTask);
    }

    @Test
    void ShouldDeleteSubTaskAndConnectionToEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask subTask = null;

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);
        taskManager.deleteSubTask(firstSubTaskForTest.getId());

        if (taskManager.getStorage().getEpics().containsKey(firstEpicForTest.getId())) {
            firstEpicForTest = taskManager.getEpic(firstEpicForTest.getId());
        }
        if (taskManager.getStorage().getSubTasks().containsKey(firstSubTaskForTest.getId())) {
            subTask = taskManager.getSubTask(firstSubTaskForTest.getId());
        }
        Assertions.assertFalse(firstEpicForTest.getSubTasks().contains(firstSubTaskForTest.getId()));
        Assertions.assertNull(subTask);
    }

    @Test
    void gettingSubTasksOfEpic() {
        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask secondSubTaskForTest = new SubTask("w", "w", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, firstEpicForTest.getId());
        SubTask thirdSubTaskForTest = new SubTask("a", "a", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:14"), 1, firstEpicForTest.getId());

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstEpicForTest.getId(),firstSubTaskForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), secondSubTaskForTest);
        taskManager.createSubTask(firstEpicForTest.getId(), thirdSubTaskForTest);

        ArrayList<SubTask> subTaskArrayList2 = taskManager.gettingSubTasksOfEpic(firstEpicForTest.getId());
        ArrayList<SubTask> subTaskArrayList3 = taskManager.gettingSubTasksOfEpic(12345);
        Epic epic = taskManager.getEpic(firstEpicForTest.getId());

        Assertions.assertNotNull(subTaskArrayList2);

        for (SubTask subTask: subTaskArrayList2) {
            Assertions.assertTrue(epic.getSubTasks().contains(subTask.getId()));
        }

        Assertions.assertTrue(subTaskArrayList2.contains(firstSubTaskForTest));
        Assertions.assertFalse(subTaskArrayList3.contains(firstSubTaskForTest));
    }

    @Test
    void ShouldReturnAllSubTasksOfChosenEpic() {



    }

    @Test
    void shouldReturnRightPrioritisedTasks() {

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:10"), 23);
        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask.getParentId(), subTask);
        taskManager.createTask(task);
        TreeSet<Task> sortedTasks = taskManager.getPrioritizedTasks();

        Assertions.assertNotNull(sortedTasks);
        Assertions.assertEquals(3, sortedTasks.size());
        Assertions.assertEquals(task, sortedTasks.first());
        Assertions.assertEquals(subTask, sortedTasks.last());

    }


}