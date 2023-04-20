package Manager.TaskManager;

import Manager.HistoryManager.HistoryManager;
import Storage.Storage;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager inMemoryHistoryManager;
    protected final Storage storage = new Storage();

    public InMemoryTaskManager(HistoryManager historyManager) {
        inMemoryHistoryManager = historyManager;
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public HashMap<Integer, Task> getAllTasks(){

        System.out.println("Начинаю искать все задачи");
        System.out.println("Вот найденные задачи: ");
        for (Integer key : storage.getTasks().keySet()) {
            System.out.println(storage.getTasks().get(key));
        }
        return storage.getTasks();

    }
    @Override
    public HashMap<Integer, Epic> getAllEpics() {

        System.out.println("Вот найденные эпики: ");
        for (Integer key : storage.getEpics().keySet()) {
            System.out.println(storage.getEpics().get(key));
        }
        return storage.getEpics();
    }
    @Override
    public HashMap<Integer, SubTask> getAllSubTasks() {

        System.out.println("Вот найденные подзадачи: ");
        for (Integer key : storage.getSubTasks().keySet()) {
            System.out.println(storage.getSubTasks().get(key));
        }
        return storage.getSubTasks();
    }
    @Override
    public void deleteAllTasks(){
        System.out.println("Удаляю задачи...");
        storage.getTasks().clear();
    }
    @Override
    public void deleteAllEpics() {
        storage.getEpics().clear();
    }
    @Override
    public void deleteAllSubTasks() {
        storage.getSubTasks().clear();
        System.out.println("Все задачи удалены!");
    }
    @Override
    public Task getTask(int id){
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(storage.getTasks().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        inMemoryHistoryManager.add(storage.getTasks().get(id));
        return storage.getTasks().get(id);
    }
    @Override
    public Epic getEpic(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(storage.getEpics().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        inMemoryHistoryManager.add(storage.getEpics().get(id));
        return storage.getEpics().get(id);
    }
    @Override
    public SubTask getSubTask(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(storage.getSubTasks().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        inMemoryHistoryManager.add(storage.getSubTasks().get(id));
        return storage.getSubTasks().get(id);
    }
    @Override
    public void createTask(Task task) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getTasks().containsKey(task.getId())) {
            storage.getTasks().put(task.getId(), task);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }
    @Override
    public void createEpic(Epic epic) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getEpics().containsKey(epic.getId())) {
            storage.getEpics().put(epic.getId(), epic);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }
    @Override
    public void createSubTask(SubTask subTask) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getSubTasks().containsKey(subTask.getId())) {
            storage.getSubTasks().put(subTask.getId(), subTask);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    public void addingSubTaskToEpic(int id, SubTask subTask){
        Epic epic = storage.getEpics().get(id);
        epic.getSubTasks().add(subTask.getId());
    }
    @Override
    public void updateTask(Task task, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (storage.getTasks().containsKey(oldId)) {
            storage.getTasks().remove(oldId);
            storage.getTasks().put(task.getId(), task);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }
    @Override
    public void updateEpic(Epic epic, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (storage.getEpics().containsKey(oldId)) {
            epic.setStatus(storage.getEpics().get(oldId).getStatus());
            for (Integer key: storage.getEpics().get(oldId).getSubTasks()) {
                epic.getSubTasks().add(key);
            }
            for (Integer key :storage.getSubTasks().keySet()) {
                if (storage.getSubTasks().get(key).getParentId() == oldId){
                    storage.getSubTasks().get(key).setParentId(epic.getId());
                }

            }
            storage.getEpics().remove(oldId);
            storage.getEpics().put(epic.getId(), epic);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }
    @Override
    public void updateSubTask(SubTask subTask, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (storage.getSubTasks().containsKey(oldId)) {
            storage.getSubTasks().remove(oldId);
            storage.getSubTasks().put(subTask.getId(), subTask);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

    public void updatingSubTaskToEpic(int id, SubTask subTask, int oldId){
        Epic epic = storage.getEpics().get(id);
        epic.getSubTasks().remove(Integer.valueOf(oldId));
        epic.getSubTasks().add(subTask.getId());
    }
    @Override
    public void deleteTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (storage.getTasks().containsKey(id)) {
            storage.getTasks().remove(id);
            inMemoryHistoryManager.remove(id);
            System.out.println("Задача удалена");

        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }
    @Override
    public void deleteEpic(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (storage.getEpics().containsKey(id)) {
            storage.getEpics().remove(id);
            inMemoryHistoryManager.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }
    @Override
    public void deleteSubTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (storage.getSubTasks().containsKey(id)) {
            storage.getSubTasks().remove(id);
            inMemoryHistoryManager.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    public void deleteConnectedSubTasks(int id) {
        ArrayList<Integer> keys = new ArrayList<>();
        for (Integer key : storage.getSubTasks().keySet()) {
            if (storage.getSubTasks().get(key).getParentId() == id) {
                keys.add(key);
            }
        }
        for (Integer key : keys) {
            storage.getSubTasks().remove(key);
            inMemoryHistoryManager.remove(key);
        }
    }

    public ArrayList<SubTask> gettingSubTasksOfEpic(int id) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (Integer key : storage.getSubTasks().keySet()) {
            if (storage.getSubTasks().get(key).getParentId() == id)
                subTaskArrayList.add(storage.getSubTasks().get(key));

        }
        System.out.println(subTaskArrayList);
        return subTaskArrayList;
    }

    public void statusCheckerAndChanger(int epicId){
        int countNewStatus = 0;
        for (Integer subTasksKey : storage.getEpics().get(epicId).getSubTasks()) {

            if (storage.getSubTasks().get(subTasksKey).getStatus().equals(TaskStatus.IN_PROGRESS)) {
                storage.getEpics().get(epicId).setStatus(TaskStatus.IN_PROGRESS);
                return;
            }

            if (storage.getSubTasks().get(subTasksKey).getStatus().equals(TaskStatus.NEW)) {
                countNewStatus += 1;
            }
        }
        if (countNewStatus == 0) {
            storage.getEpics().get(epicId).setStatus(TaskStatus.DONE);
        } else if (countNewStatus != storage.getSubTasks().size()) {
            storage.getEpics().get(epicId).setStatus(TaskStatus.IN_PROGRESS);
        } else {
            storage.getEpics().get(epicId).setStatus(TaskStatus.NEW);
        }
    }
}
