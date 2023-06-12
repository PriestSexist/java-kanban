package Manager.TaskManager;

import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;
    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
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
        taskManager.createSubTask(firstSubTaskForTest);
        taskManager.createSubTask(secondSubTaskForTest);

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
        taskManager.createSubTask(firstSubTaskForTest);
        taskManager.createSubTask(secondSubTaskForTest);

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
        Task task1;
        Task task2;

        taskManager.createTask(firstTaskForTest);

        task1 = taskManager.getTask(firstTaskForTest.getId());
        task2 = taskManager.getTask(12345);

        Assertions.assertNotNull(task1);
        Assertions.assertNull(task2);
        Assertions.assertEquals(task1, firstTaskForTest);
    }

    @Test
    void shouldCreateAndReturnEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic epic1;
        Epic epic2;

        taskManager.createEpic(firstEpicForTest);

        epic1 = taskManager.getEpic(firstEpicForTest.getId());
        epic2 = taskManager.getEpic(12345);



        Assertions.assertNotNull(epic1);
        Assertions.assertNull(epic2);
        Assertions.assertEquals(epic1, firstEpicForTest);
    }

    @Test
    void shouldCreateAndReturnSubTask() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask subTask1;
        SubTask subTask2;

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstSubTaskForTest);

        subTask1 = taskManager.getSubTask(firstSubTaskForTest.getId());
        subTask2 = taskManager.getSubTask(12345);

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
        taskManager.createSubTask(firstSubTaskForTest);

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
        taskManager.createSubTask(firstSubTaskForTest);

        taskManager.updateSubTask(secondSubTaskForTest, firstSubTaskForTest.getId());
        Epic epicAfterUpdate = taskManager.getEpic(firstEpicForTest.getId());
        SubTask subTaskAfterUpdate = taskManager.getSubTask(secondSubTaskForTest.getId());

        Assertions.assertTrue(epicAfterUpdate.getSubTasks().contains(secondSubTaskForTest.getId()));
        Assertions.assertNotNull(subTaskAfterUpdate);
        Assertions.assertEquals(subTaskAfterUpdate, secondSubTaskForTest);
    }

    @Test
    void shouldDeleteTask() {

        Task firstTaskForTest = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Task task1;

        taskManager.createTask(firstTaskForTest);
        taskManager.deleteTask(firstTaskForTest.getId());

        task1 = taskManager.getTask(firstTaskForTest.getId());

        Assertions.assertNull(task1);

    }

    @Test
    void shouldDeleteEpicAndConnectedSubTask() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        Epic epic;
        SubTask subTask;

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstSubTaskForTest);

        taskManager.deleteEpic(firstEpicForTest.getId());

        epic = taskManager.getEpic(firstEpicForTest.getId());
        subTask = taskManager.getSubTask(firstSubTaskForTest.getId());

        Assertions.assertNull(epic);
        Assertions.assertNull(subTask);
    }

    @Test
    void ShouldDeleteSubTaskAndConnectionToEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask subTask;

        taskManager.createEpic(firstEpicForTest);
        taskManager.createSubTask(firstSubTaskForTest);
        taskManager.deleteSubTask(firstSubTaskForTest.getId());

        firstEpicForTest = taskManager.getEpic(firstEpicForTest.getId());
        subTask = taskManager.getSubTask(firstSubTaskForTest.getId());

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
        taskManager.createSubTask(firstSubTaskForTest);
        taskManager.createSubTask(secondSubTaskForTest);
        taskManager.createSubTask(thirdSubTaskForTest);

        ArrayList<SubTask> subTaskArrayList2 = taskManager.gettingSubTasksOfEpic(firstEpicForTest.getId());
        ArrayList<SubTask> subTaskArrayList3 = taskManager.gettingSubTasksOfEpic(12345);
        Epic epic = taskManager.getEpic(firstEpicForTest.getId());

        Assertions.assertNotNull(subTaskArrayList2);

        for (SubTask subTask : subTaskArrayList2) {
            Assertions.assertTrue(epic.getSubTasks().contains(subTask.getId()));
        }

        Assertions.assertTrue(subTaskArrayList2.contains(firstSubTaskForTest));
        Assertions.assertFalse(subTaskArrayList3.contains(firstSubTaskForTest));
    }

    @Test
    void shouldReturnRightPrioritisedTasks() {

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:10"), 23);
        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createTask(task);
        TreeSet<Task> sortedTasks = taskManager.getPrioritizedTasks();

        Assertions.assertNotNull(sortedTasks);
        Assertions.assertEquals(3, sortedTasks.size());
        Assertions.assertEquals(task, sortedTasks.first());
        Assertions.assertEquals(subTask, sortedTasks.last());

    }

    @Test
    public void shouldReturnNewStatusToEpicWithoutSubTasks(){

        Epic firstEpicForTest1 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        Epic secondEpicForTest1 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1);

        taskManager.createEpic(firstEpicForTest1);
        taskManager.updateEpic(secondEpicForTest1, firstEpicForTest1.getId());

        TaskStatus status = taskManager.getEpic(secondEpicForTest1.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.NEW, status);
    }

    @Test
    public void shouldReturnNewStatusToEpicWithAllNewSubTasks(){

        Epic epicForTest2 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest2 = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, epicForTest2.getId());
        SubTask secondSubTaskForTest2 = new SubTask("a", "a", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, epicForTest2.getId());

        taskManager.createEpic(epicForTest2);
        taskManager.createSubTask(firstSubTaskForTest2);
        taskManager.updateSubTask(secondSubTaskForTest2, firstSubTaskForTest2.getId());

        TaskStatus status = taskManager.getEpic(epicForTest2.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.NEW, status);
    }

    @Test
    public void shouldReturnDoneStatusToEpicWithAllDoneSubTasks(){

        Epic epicForTest3 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest3 = new SubTask("q", "q", TaskStatus.DONE, LocalDateTime.parse("2002-11-11T11:12"), 1, epicForTest3.getId());
        SubTask secondSubTaskForTest3 = new SubTask("a", "a", TaskStatus.DONE, LocalDateTime.parse("2002-11-11T11:13"), 1, epicForTest3.getId());

        taskManager.createEpic(epicForTest3);
        taskManager.createSubTask(firstSubTaskForTest3);
        taskManager.updateSubTask(secondSubTaskForTest3, firstSubTaskForTest3.getId());


        TaskStatus status = taskManager.getEpic(epicForTest3.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.DONE, status);
    }

    @Test
    public void shouldReturnInProgressStatusToEpicWithNewAndDoneSubTasks(){

        Epic epicForTest4 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest4 = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, epicForTest4.getId());
        SubTask secondSubTaskForTest4 = new SubTask("a", "a", TaskStatus.DONE, LocalDateTime.parse("2002-11-11T11:13"), 1, epicForTest4.getId());
        SubTask thirdSubTaskForTest4 = new SubTask("s", "s", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:15"), 1, epicForTest4.getId());

        taskManager.createEpic(epicForTest4);
        taskManager.createSubTask(firstSubTaskForTest4);
        taskManager.updateSubTask(secondSubTaskForTest4, firstSubTaskForTest4.getId());
        taskManager.createSubTask(thirdSubTaskForTest4);


        TaskStatus status = taskManager.getEpic(epicForTest4.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, status);
    }

    @Test
    public void shouldReturnInProgressStatusToEpicWithInProgressSubTasks4(){

        Epic firstEpicForTest5 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest5 = new SubTask("q", "q", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest5.getId());
        SubTask secondSubTaskForTest5 = new SubTask("a", "a", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:13"), 1, firstEpicForTest5.getId());

        Epic secondEpicForTest5 = new Epic("121", "232", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:14"), 1);
        SubTask thirdSubTaskForTest5 = new SubTask("g", "g", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:15"), 1, secondEpicForTest5.getId());

        taskManager.createEpic(firstEpicForTest5);
        taskManager.createSubTask(firstSubTaskForTest5);
        taskManager.updateSubTask(secondSubTaskForTest5, firstSubTaskForTest5.getId());

        taskManager.createEpic(secondEpicForTest5);
        taskManager.createSubTask(thirdSubTaskForTest5);

        TaskStatus status = taskManager.getEpic(firstEpicForTest5.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, status);
    }


}