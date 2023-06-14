package Manager.TaskManager;

import Manager.HistoryManager.InMemoryHistoryManager;
import Storage.Storage;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager(new InMemoryHistoryManager(new Storage()), new Storage()));
    }

    @Test
    void shouldValidateTimeOfTasks(){
        Task task = new Task("11", "22", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:10"), 23);

        Task task1 = new Task("22", "33", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:32"), 23);
        Task task2 = new Task("33", "44", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        Task task3 = new Task("44", "55", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T12:19"), 23);

        Task testTask;

        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        testTask = taskManager.getTask(task1.getId());

        Assertions.assertNull(testTask);
        Assertions.assertNotNull(taskManager.getTask(task2.getId()));
        Assertions.assertNotNull(taskManager.getTask(task3.getId()));
    }

    @Test
    void shouldCorrectEpicStartTime() {

        Epic epic = new Epic("1", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:33"), 23);
        SubTask subTask = new SubTask("Q", "Q", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:56"), 23, epic.getId());

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        Assertions.assertEquals(LocalDateTime.parse("2002-11-11T11:56"), taskManager.getEpic(epic.getId()).getStartTime());
        Assertions.assertEquals(23, taskManager.getEpic(epic.getId()).getDuration());
        Assertions.assertEquals(LocalDateTime.parse("2002-11-11T12:19"), taskManager.getEpic(epic.getId()).getEndTime());

        taskManager.deleteSubTask(subTask.getId());

        Assertions.assertEquals(0, taskManager.getEpic(epic.getId()).getDuration());
        Assertions.assertEquals(LocalDateTime.MAX, taskManager.getEpic(epic.getId()).getStartTime());


    }
}