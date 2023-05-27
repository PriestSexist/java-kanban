package Manager.TaskManager;

import Manager.HistoryManager.HistoryManager;
import Storage.Storage;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    protected static HistoryManager inMemoryHistoryManager;
    private final Storage storage = new Storage();

    public InMemoryTaskManager(HistoryManager historyManager) {
        inMemoryHistoryManager = historyManager;
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

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
        if (storage.getTasks().containsKey(id)) {
            System.out.println(storage.getTasks().get(id));
            System.out.println("Идентификатор найден. Возвращаю его");
            inMemoryHistoryManager.add(storage.getTasks().get(id));
            return storage.getTasks().get(id);
        }
        System.out.println("Ничего не найдено");
        return null;

    }
    @Override
    public Epic getEpic(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        if (storage.getEpics().containsKey(id)) {
            System.out.println(storage.getEpics().get(id));
            System.out.println("Идентификатор найден. Возвращаю его");
            inMemoryHistoryManager.add(storage.getEpics().get(id));
            return storage.getEpics().get(id);
        }
        System.out.println("Ничего не найдено");
        return null;

    }
    @Override
    public SubTask getSubTask(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        if (storage.getSubTasks().containsKey(id)){
            System.out.println(storage.getSubTasks().get(id));
            System.out.println("Идентификатор найден. Возвращаю его");
            inMemoryHistoryManager.add(storage.getSubTasks().get(id));
            return storage.getSubTasks().get(id);
        }
        System.out.println("Ничего не найдено");
        return null;
    }
    @Override
    public void createTask(Task task) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getTasks().containsKey(task.getId()) && timeValidator(task)) {
            storage.getTasks().put(task.getId(), task);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже либо данный идентификатор уже занят. Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void createEpic(Epic epic) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getEpics().containsKey(epic.getId()) && timeValidator(epic)) {
            storage.getEpics().put(epic.getId(), epic);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят.Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void createSubTask(int id, SubTask subTask) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getSubTasks().containsKey(subTask.getId()) && timeValidator(subTask)) {
            storage.getSubTasks().put(subTask.getId(), subTask);

            Epic epic = storage.getEpics().get(id);
            epic.getSubTasks().add(subTask.getId());

            epicTimeCorrector(id);

            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void updateTask(Task task, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (storage.getTasks().containsKey(oldId) && timeValidator(task)) {
            storage.getTasks().remove(oldId);
            storage.getTasks().put(task.getId(), task);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то пошло не так. Может вы ввели не тот идентификатор? Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void updateEpic(Epic epic, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (storage.getEpics().containsKey(oldId ) && timeValidator(epic)) {
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
            System.out.println("Хм... Что-то пошло не так. Может вы ввели не тот идентификатор? Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void updateSubTask(int id, SubTask subTask, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");

        if (storage.getSubTasks().containsKey(oldId) && timeValidator(subTask)) {
            storage.getSubTasks().remove(oldId);
            storage.getSubTasks().put(subTask.getId(), subTask);

            Epic epic = storage.getEpics().get(id);
            epic.getSubTasks().remove(Integer.valueOf(oldId));
            epic.getSubTasks().add(subTask.getId());

            epicTimeCorrector(id);

            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то пошло не так. Может вы ввели не тот идентификатор? Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }

    @Override
    public void deleteTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (storage.getTasks().containsKey(id)) {
            Task task = storage.getTasks().get(id);
            storage.getTasks().remove(id);
            if (inMemoryHistoryManager.getHistory().contains(task)){
                inMemoryHistoryManager.remove(id);
            }
            System.out.println("Задача удалена");

        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }
    @Override
    public void deleteEpic(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        ArrayList<Integer> keys = new ArrayList<>();
        if (storage.getEpics().containsKey(id)) {
            Epic epic = storage.getEpics().get(id);
            storage.getEpics().remove(id);
            if (inMemoryHistoryManager.getHistory().contains(epic)){
                inMemoryHistoryManager.remove(id);
            }

            for (Integer key : storage.getSubTasks().keySet()) {
                if (storage.getSubTasks().get(key).getParentId() == id) {
                    keys.add(key);
                }
            }
            for (Integer key : keys) {
                SubTask subTask = storage.getSubTasks().get(id);
                storage.getSubTasks().remove(key);
                if (inMemoryHistoryManager.getHistory().contains(subTask)){
                    inMemoryHistoryManager.remove(key);
                }
            }

            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }
    @Override
    public void deleteSubTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (storage.getSubTasks().containsKey(id)) {
            SubTask subTask = storage.getSubTasks().get(id);
            storage.getSubTasks().remove(id);
            if (inMemoryHistoryManager.getHistory().contains(subTask)){
                inMemoryHistoryManager.remove(id);
            }

            storage.getEpics().get(subTask.getParentId()).getSubTasks().remove( (Integer) subTask.getId());
            System.out.println("Задача удалена");

        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
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

    private void epicTimeCorrector(int id) {
        Epic epic = storage.getEpics().get(id);
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        for (Integer key : epic.getSubTasks()) {
            if (storage.getSubTasks().get(key).getEndTime().isAfter(endTime)) {
                endTime = storage.getSubTasks().get(key).getEndTime();
                epic.setEndTime(storage.getSubTasks().get(key).getEndTime());
            }
            if (storage.getSubTasks().get(key).getStartTime().isBefore(startTime)){
                epic.setStartTime(storage.getSubTasks().get(key).getStartTime());
                startTime = storage.getSubTasks().get(key).getStartTime();
            }
        }
        long duration = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        epic.setDuration(duration/60000);
        storage.getEpics().remove(epic.getId());
        storage.getEpics().put(epic.getId(), epic);
    }

    public TreeSet<Task> getPrioritizedTasks(){
        //Почему-то игнорирует последнюю строчку и не добавляет задачи в TreeSet. Причём если поменять местами, то всё равно не работает именно последняя строчка
        TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedTasks.addAll(storage.getTasks().values());
        sortedTasks.addAll(storage.getSubTasks().values());
        sortedTasks.addAll(storage.getEpics().values());
        return sortedTasks;
    }

    private boolean timeValidator(Task task){
        TreeSet<Task> sortedTasks = getPrioritizedTasks();
        for (Task chosenTask : sortedTasks) {
            if (chosenTask.getEndTime().isAfter(task.getStartTime())){
                System.out.println("Время введённое вами пересекается с временем другой задачи");
                return false;
            }
        }
        return true;
    }
}