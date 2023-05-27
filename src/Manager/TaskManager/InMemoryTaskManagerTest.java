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
import java.util.ArrayList;
import java.util.TreeSet;

class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void managerCreator(){
        inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    void gettingSubTasksOfEpic() {
        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(),firstSubTaskForTest);

        ArrayList<SubTask> subTaskArrayList2 = inMemoryTaskManager.gettingSubTasksOfEpic(firstEpicForTest.getId());
        ArrayList<SubTask> subTaskArrayList3 = inMemoryTaskManager.gettingSubTasksOfEpic(12345);

        Assertions.assertTrue(subTaskArrayList2.contains(firstSubTaskForTest));
        Assertions.assertFalse(subTaskArrayList3.contains(firstSubTaskForTest));
    }

    @Test
    public void shouldReturnNewStatusToEpicWithoutSubTasks(){

        Epic epicForTest1 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);

        inMemoryTaskManager.createEpic(epicForTest1);

        inMemoryTaskManager.statusCheckerAndChanger(epicForTest1.getId());

        TaskStatus status = inMemoryTaskManager.getEpic(epicForTest1.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.DONE, status);
    }

    @Test
    public void shouldReturnNewStatusToEpicWithAllNewSubTasks(){

        Epic epicForTest2 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest2 = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, epicForTest2.getId());
        SubTask secondSubTaskForTest2 = new SubTask("a", "a", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, epicForTest2.getId());

        inMemoryTaskManager.createEpic(epicForTest2);
        inMemoryTaskManager.createSubTask(epicForTest2.getId(), firstSubTaskForTest2);
        inMemoryTaskManager.createSubTask(epicForTest2.getId(), secondSubTaskForTest2);

        inMemoryTaskManager.statusCheckerAndChanger(epicForTest2.getId());

        TaskStatus status = inMemoryTaskManager.getEpic(epicForTest2.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.NEW, status);
    }

    @Test
    public void shouldReturnDoneStatusToEpicWithAllDoneSubTasks(){

        Epic epicForTest3 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest3 = new SubTask("q", "q", TaskStatus.DONE, LocalDateTime.parse("2002-11-11T11:12"), 1, epicForTest3.getId());
        SubTask secondSubTaskForTest3 = new SubTask("a", "a", TaskStatus.DONE, LocalDateTime.parse("2002-11-11T11:13"), 1, epicForTest3.getId());

        inMemoryTaskManager.createEpic(epicForTest3);
        inMemoryTaskManager.createSubTask(epicForTest3.getId(), firstSubTaskForTest3);
        inMemoryTaskManager.createSubTask(epicForTest3.getId(), secondSubTaskForTest3);


        inMemoryTaskManager.statusCheckerAndChanger(epicForTest3.getId());

        TaskStatus status = inMemoryTaskManager.getEpic(epicForTest3.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.DONE, status);
    }

    @Test
    public void shouldReturnInProgressStatusToEpicWithNewAndDoneSubTasks(){

        Epic epicForTest4 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest4 = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, epicForTest4.getId());
        SubTask secondSubTaskForTest4 = new SubTask("a", "a", TaskStatus.DONE, LocalDateTime.parse("2002-11-11T11:13"), 1, epicForTest4.getId());

        inMemoryTaskManager.createEpic(epicForTest4);
        inMemoryTaskManager.createSubTask(epicForTest4.getId(),firstSubTaskForTest4);
        inMemoryTaskManager.createSubTask(epicForTest4.getId(),secondSubTaskForTest4);


        inMemoryTaskManager.statusCheckerAndChanger(epicForTest4.getId());

        TaskStatus status = inMemoryTaskManager.getEpic(epicForTest4.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, status);
    }

    @Test
    public void shouldReturnInProgressStatusToEpicWithInProgressSubTasks4(){

        Epic epicForTest5 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest5 = new SubTask("q", "q", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:12"), 1, epicForTest5.getId());
        SubTask secondSubTaskForTest5 = new SubTask("a", "a", TaskStatus.IN_PROGRESS, LocalDateTime.parse("2002-11-11T11:13"), 1, epicForTest5.getId());

        inMemoryTaskManager.createEpic(epicForTest5);
        inMemoryTaskManager.createSubTask(epicForTest5.getId(), firstSubTaskForTest5);
        inMemoryTaskManager.createSubTask(epicForTest5.getId(),secondSubTaskForTest5);

        inMemoryTaskManager.statusCheckerAndChanger(epicForTest5.getId());

        TaskStatus status = inMemoryTaskManager.getEpic(epicForTest5.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, status);
    }

    @Test
    void ShouldReturnAllSubTasksOfChosenEpic() {

        Epic firstEpicForTest = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest = new SubTask("q", "q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:12"), 1, firstEpicForTest.getId());
        SubTask secondSubTaskForTest = new SubTask("w", "w", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:13"), 1, firstEpicForTest.getId());
        SubTask thirdSubTaskForTest = new SubTask("a", "a", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:14"), 1, firstEpicForTest.getId());

        inMemoryTaskManager.createEpic(firstEpicForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), firstSubTaskForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), secondSubTaskForTest);
        inMemoryTaskManager.createSubTask(firstEpicForTest.getId(), thirdSubTaskForTest);

        ArrayList <SubTask> subTasks = inMemoryTaskManager.gettingSubTasksOfEpic(firstEpicForTest.getId());
        Epic epic = inMemoryTaskManager.getEpic(firstEpicForTest.getId());

        Assertions.assertNotNull(subTasks);

        for (SubTask subTask: subTasks) {
            Assertions.assertTrue(epic.getSubTasks().contains(subTask.getId()));
        }

    }

    @Test
    void shouldReturnRightPrioritisedTasks() {

        Task task = new Task("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:10"), 23);
        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask.getParentId(), subTask);
        TreeSet<Task> sortedTasks = inMemoryTaskManager.getPrioritizedTasks();

        Assertions.assertNotNull(sortedTasks);
        Assertions.assertEquals(3, sortedTasks.size());
        Assertions.assertEquals(task, sortedTasks.first());
        Assertions.assertEquals(subTask, sortedTasks.last());


    }

    @Test
    void shouldValidateTimeOfEpic(){
        Task task = new Task("11", "22", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:10"), 23);

        Task task1 = new Task("22", "33", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:32"), 23);
        Task task2 = new Task("33", "44", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        Task task3 = new Task("44", "55", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T12:19"), 23);

        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);

        Assertions.assertNull(inMemoryTaskManager.getTask(task1.getId()));
        Assertions.assertNotNull(inMemoryTaskManager.getTask(task2.getId()));
        Assertions.assertNotNull(inMemoryTaskManager.getTask(task3.getId()));
    }

    @Test
    void shouldCorrectEpicStartTime() {

        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask.getParentId(), subTask);

        Assertions.assertEquals(LocalDateTime.parse("2002-11-11T11:56"), inMemoryTaskManager.getEpic(epic.getId()).getStartTime());
        Assertions.assertEquals(23, inMemoryTaskManager.getEpic(epic.getId()).getDuration());
        Assertions.assertEquals(LocalDateTime.parse("2002-11-11T12:19"), inMemoryTaskManager.getEpic(epic.getId()).getEndTime());


    }
}