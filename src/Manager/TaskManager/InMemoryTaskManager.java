package Manager.TaskManager;

import Manager.HistoryManager.HistoryManager;
import Storage.Storage;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    protected HistoryManager inMemoryHistoryManager;
    protected Storage storage;

    public InMemoryTaskManager(HistoryManager historyManager, Storage storage) {
        this.inMemoryHistoryManager = historyManager;
        this.storage = storage;
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
        storage.getEpics().clear();
        storage.getSubTasks().clear();
        storage.getSortedTasks().clear();
        inMemoryHistoryManager.removeAll();
        System.out.println("Все задачи удалены!");
    }

    @Override
    public Task getTask(int id){
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println("Вот что мне удалось найти в памяти:");
        System.out.println(storage.getTasks().get(id));
        try {
            inMemoryHistoryManager.add(storage.getTasks().get(id));
        } catch (NullPointerException exception){
            System.out.println("Задача не найдена");
        }
        return storage.getTasks().get(id);

    }
    @Override
    public Epic getEpic(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println("Вот что мне удалось найти в памяти:");
        System.out.println(storage.getEpics().get(id));
        try {
            inMemoryHistoryManager.add(storage.getEpics().get(id));
        } catch (NullPointerException exception){
            System.out.println("Эпик не найден");
        }
        return storage.getEpics().get(id);

    }
    @Override
    public SubTask getSubTask(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println("Вот что мне удалось найти в памяти:");
        System.out.println(storage.getSubTasks().get(id));
        try {
            inMemoryHistoryManager.add(storage.getSubTasks().get(id));
        } catch (NullPointerException exception){
            System.out.println("Подзадача не найдена");
        }

        return storage.getSubTasks().get(id);
    }
    @Override
    public void createTask(Task task) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getTasks().containsKey(task.getId()) && timeValidator(task)) {
            storage.getTasks().put(task.getId(), task);
            storage.getSortedTasks().add(task);
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
            storage.getSortedTasks().add(epic);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят.Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void createSubTask(SubTask subTask) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getSubTasks().containsKey(subTask.getId()) && timeValidator(subTask)) {
            storage.getSubTasks().put(subTask.getId(), subTask);

            Epic epic = storage.getEpics().get(subTask.getParentId());
            epic.getSubTasks().add(subTask.getId());

            storage.getSortedTasks().add(subTask);

            epicTimeCorrector(subTask.getParentId());
            statusCheckerAndChanger(subTask.getParentId());

            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void updateTask(Task task, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (storage.getTasks().containsKey(oldId) && timeValidator(task)) {
            storage.getSortedTasks().remove(storage.getTasks().get(oldId));
            storage.getTasks().remove(oldId);

            storage.getSortedTasks().add(task);
            storage.getTasks().put(task.getId(), task);

            // При обновлении задачи, в истории должна удаляться старая версия?
            if (inMemoryHistoryManager.getTasks().contains(task)){
                inMemoryHistoryManager.remove(oldId);
            }

            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то пошло не так. Может вы ввели не тот идентификатор? Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void updateEpic(Epic epic, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (storage.getEpics().containsKey(oldId) && timeValidator(epic)) {
            epic.setStatus(storage.getEpics().get(oldId).getStatus());
            for (Integer key: storage.getEpics().get(oldId).getSubTasks()) {
                epic.getSubTasks().add(key);
            }
            for (Integer key :storage.getSubTasks().keySet()) {
                if (storage.getSubTasks().get(key).getParentId() == oldId){
                    storage.getSubTasks().get(key).setParentId(epic.getId());
                }

            }
            storage.getSortedTasks().remove(storage.getEpics().get(oldId));
            storage.getEpics().remove(oldId);

            storage.getSortedTasks().add(epic);
            storage.getEpics().put(epic.getId(), epic);

            // При обновлении задачи, в истории должна удаляться старая версия?
            if (inMemoryHistoryManager.getTasks().contains(epic)){
                inMemoryHistoryManager.remove(oldId);
            }

            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то пошло не так. Может вы ввели не тот идентификатор? Либо время, которое вы ввели, пересекается с временем другой задачи");
        }
    }
    @Override
    public void updateSubTask(SubTask subTask, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");

        if (storage.getSubTasks().containsKey(oldId) && timeValidator(subTask)) {
            storage.getSortedTasks().remove(storage.getSubTasks().get(oldId));
            storage.getSubTasks().remove(oldId);

            storage.getSortedTasks().add(subTask);
            storage.getSubTasks().put(subTask.getId(), subTask);

            Epic epic = storage.getEpics().get(subTask.getParentId());
            epic.getSubTasks().remove(Integer.valueOf(oldId));
            epic.getSubTasks().add(subTask.getId());

            epicTimeCorrector(subTask.getParentId());

            statusCheckerAndChanger(subTask.getParentId());

            // При обновлении задачи, в истории должна удаляться старая версия?
            if (inMemoryHistoryManager.getTasks().contains(subTask)){
                inMemoryHistoryManager.remove(oldId);
            }

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
            if (inMemoryHistoryManager.getTasks().contains(task)){
                inMemoryHistoryManager.remove(id);
            }
            storage.getSortedTasks().remove(task);
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
            if (inMemoryHistoryManager.getTasks().contains(epic)){
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
                if (inMemoryHistoryManager.getTasks().contains(subTask)){
                    inMemoryHistoryManager.remove(key);
                }
            }

            storage.getSortedTasks().remove(epic);
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
            if (inMemoryHistoryManager.getTasks().contains(subTask)){
                inMemoryHistoryManager.remove(id);
            }

            storage.getEpics().get(subTask.getParentId()).getSubTasks().remove((Integer) subTask.getId());
            System.out.println("Задача удалена");

            epicTimeCorrector(subTask.getParentId());

            storage.getSortedTasks().remove(subTask);

        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    @Override
    public ArrayList<SubTask> gettingSubTasksOfEpic(int id) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (Integer key : storage.getSubTasks().keySet()) {
            if (storage.getSubTasks().get(key).getParentId() == id)
                subTaskArrayList.add(storage.getSubTasks().get(key));

        }
        System.out.println(subTaskArrayList);
        return subTaskArrayList;
    }

    private void statusCheckerAndChanger(int epicId){
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
        } else if (countNewStatus != storage.getEpics().get(epicId).getSubTasks().size()) {
            storage.getEpics().get(epicId).setStatus(TaskStatus.IN_PROGRESS);
        } else {
            storage.getEpics().get(epicId).setStatus(TaskStatus.NEW);
        }
    }

    private void epicTimeCorrector(int id) {
        Epic epic = storage.getEpics().get(id);
        LocalDateTime startTime = LocalDateTime.MAX;
        long duration = 0;
        for (Integer key : epic.getSubTasks()) {
            SubTask subTask = storage.getSubTasks().get(key);

            if (subTask.getStartTime().isBefore(startTime)){
                startTime = subTask.getStartTime();
            }

            duration = duration + subTask.getDuration();
        }
        epic.setTime(startTime, duration);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks(){
        return storage.getSortedTasks();
    }

    private boolean timeValidator(Task task){
        TreeSet<Task> sortedTasks = getPrioritizedTasks();
        if (sortedTasks.size()==0){
            return true;
        }
        for (Task chosenTask : sortedTasks) {
            if ((chosenTask.getEndTime().isBefore(task.getStartTime()) || chosenTask.getEndTime().equals(task.getStartTime()))
                    || (chosenTask.getStartTime().isAfter(task.getEndTime()) || chosenTask.getStartTime().equals(task.getEndTime()))){
                return true;
            }
        }
        System.out.println("Время введённое вами пересекается с временем другой задачи");
        return false;
    }
}