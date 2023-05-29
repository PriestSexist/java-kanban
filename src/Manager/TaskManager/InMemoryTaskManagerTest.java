package Manager.TaskManager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager(new InMemoryHistoryManager()));
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
        taskManager.createSubTask(epicForTest2.getId(), firstSubTaskForTest2);
        taskManager.updateSubTask(epicForTest2.getId(), secondSubTaskForTest2, firstSubTaskForTest2.getId());

        TaskStatus status = taskManager.getEpic(epicForTest2.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.NEW, status);
    }

    @Test
    public void shouldReturnDoneStatusToEpicWithAllDoneSubTasks(){

        Epic epicForTest3 = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 1);
        SubTask firstSubTaskForTest3 = new SubTask("q", "q", TaskStatus.DONE, LocalDateTime.parse("2002-11-11T11:12"), 1, epicForTest3.getId());
        SubTask secondSubTaskForTest3 = new SubTask("a", "a", TaskStatus.DONE, LocalDateTime.parse("2002-11-11T11:13"), 1, epicForTest3.getId());

        taskManager.createEpic(epicForTest3);
        taskManager.createSubTask(epicForTest3.getId(), firstSubTaskForTest3);
        taskManager.updateSubTask(epicForTest3.getId(), secondSubTaskForTest3, firstSubTaskForTest3.getId());


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
        taskManager.createSubTask(epicForTest4.getId(),firstSubTaskForTest4);
        taskManager.updateSubTask(epicForTest4.getId(), secondSubTaskForTest4, firstSubTaskForTest4.getId());
        taskManager.createSubTask(epicForTest4.getId(),thirdSubTaskForTest4);


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
        taskManager.createSubTask(firstEpicForTest5.getId(), firstSubTaskForTest5);
        taskManager.updateSubTask(firstEpicForTest5.getId(), secondSubTaskForTest5, firstSubTaskForTest5.getId());

        taskManager.createEpic(secondEpicForTest5);
        taskManager.createSubTask(secondEpicForTest5.getId(), thirdSubTaskForTest5);

        TaskStatus status = taskManager.getEpic(firstEpicForTest5.getId()).getStatus();

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, status);
    }

    @Test
    void shouldValidateTimeOfTasks(){
        Task task = new Task("11", "22", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:10"), 23);

        Task task1 = new Task("22", "33", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:32"), 23);
        Task task2 = new Task("33", "44", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        Task task3 = new Task("44", "55", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T12:19"), 23);

        Task testTask = null;

        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        if (taskManager.getStorage().getTasks().containsKey(task1.getId())) {
            testTask = taskManager.getTask(task1.getId());
        }
        Assertions.assertNull(testTask);
        Assertions.assertNotNull(taskManager.getTask(task2.getId()));
        Assertions.assertNotNull(taskManager.getTask(task3.getId()));
    }

    @Test
    void shouldCorrectEpicStartTime() {

        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask.getParentId(), subTask);

        Assertions.assertEquals(LocalDateTime.parse("2002-11-11T11:56"), taskManager.getEpic(epic.getId()).getStartTime());
        Assertions.assertEquals(23, taskManager.getEpic(epic.getId()).getDuration());
        Assertions.assertEquals(LocalDateTime.parse("2002-11-11T12:19"), taskManager.getEpic(epic.getId()).getEndTime());

        taskManager.deleteSubTask(subTask.getId());

        Assertions.assertEquals(0, taskManager.getEpic(epic.getId()).getDuration());
        Assertions.assertEquals(LocalDateTime.MAX, taskManager.getEpic(epic.getId()).getStartTime());


    }
}