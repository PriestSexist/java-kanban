package Manager.HistoryManager;

import Manager.TaskManager.InMemoryTaskManager;
import Storage.TaskStatus;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class InMemoryHistoryManagerTest {

    InMemoryTaskManager inMemoryTaskManager;
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @BeforeEach
    public void managerCreator(){
        inMemoryTaskManager = new InMemoryTaskManager(inMemoryHistoryManager);
    }

    @Test
    void shouldAddTasksInHistoryAndReturnHistory() {
        Task task = new Task("eat", "eat", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 12);

        inMemoryTaskManager.createTask(task);

        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task.getId()));
        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task.getId()));
        List<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertNotNull(history);
        Assertions.assertTrue(history.contains(task));
        Assertions.assertEquals(1, history.size());
    }

    @Test
    void shouldRemoveTask5Task3Task1() {
        Task task1 = new Task("1", "1", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:11"), 12);
        Task task2 = new Task("2", "2", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:23"), 12);
        Task task3 = new Task("3", "3", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:35"), 12);
        Task task4 = new Task("4", "4", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:47"), 12);
        Task task5 = new Task("5", "5", TaskStatus.NEW, LocalDateTime.parse("2002-11-11T11:59"), 12);


        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.createTask(task4);
        inMemoryTaskManager.createTask(task5);

        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task1.getId()));
        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task2.getId()));
        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task3.getId()));
        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task4.getId()));
        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task5.getId()));

        inMemoryHistoryManager.remove(task5.getId());
        List<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertNotNull(history);
        Assertions.assertFalse(history.contains(task5));

        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task5.getId()));

        inMemoryHistoryManager.remove(task3.getId());
        history = inMemoryHistoryManager.getHistory();

        Assertions.assertNotNull(history);
        Assertions.assertFalse(history.contains(task3));

        inMemoryHistoryManager.add(inMemoryTaskManager.getStorage().getTasks().get(task3.getId()));

        inMemoryHistoryManager.remove(task1.getId());
        history = inMemoryHistoryManager.getHistory();

        Assertions.assertNotNull(history);
        Assertions.assertFalse(history.contains(task1));
    }

}