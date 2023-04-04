package Manager.TaskManager;

import Manager.Managers;
import Storage.Storage;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {



    @Override
    public HashMap<Integer, Task> getAllTasks(){

        System.out.println("Начинаю искать все задачи");
        System.out.println("Вот найденные задачи: ");
        for (Integer key : Storage.getTasks().keySet()) {
            System.out.println(Storage.getTasks().get(key));
        }
        return Storage.getTasks();

    }
    @Override
    public HashMap<Integer, Epic> getAllEpics() {

        System.out.println("Вот найденные эпики: ");
        for (Integer key : Storage.getEpics().keySet()) {
            System.out.println(Storage.getEpics().get(key));
        }
        return Storage.getEpics();
    }
    @Override
    public HashMap<Integer, SubTask> getAllSubTasks() {

        System.out.println("Вот найденные подзадачи: ");
        for (Integer key : Storage.getSubTasks().keySet()) {
            System.out.println(Storage.getSubTasks().get(key));
        }
        return Storage.getSubTasks();
    }
    @Override
    public void deleteAllTasks(){
        System.out.println("Удаляю задачи...");
        Storage.getTasks().clear();
    }
    @Override
    public void deleteAllEpics() {
        Storage.getEpics().clear();
    }
    @Override
    public void deleteAllSubTasks() {
        Storage.getSubTasks().clear();
        System.out.println("Все задачи удалены!");
    }
    @Override
    public Task getTask(int id){
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(Storage.getTasks().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        Managers.getDefaultHistory().add(Storage.getTasks().get(id));
        return Storage.getTasks().get(id);
    }
    @Override
    public Epic getEpic(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(Storage.getEpics().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        Managers.getDefaultHistory().add(Storage.getEpics().get(id));
        return Storage.getEpics().get(id);
    }
    @Override
    public SubTask getSubTask(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(Storage.getSubTasks().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        Managers.getDefaultHistory().add(Storage.getSubTasks().get(id));
        return Storage.getSubTasks().get(id);
    }
    @Override
    public void createTask(Task task) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!Storage.getTasks().containsKey(task.getId())) {
            Storage.getTasks().put(task.getId(), task);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }
    @Override
    public void createEpic(Epic epic) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!Storage.getEpics().containsKey(epic.getId())) {
            Storage.getEpics().put(epic.getId(), epic);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }
    @Override
    public void createSubTask(SubTask subTask) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!Storage.getSubTasks().containsKey(subTask.getId())) {
            Storage.getSubTasks().put(subTask.getId(), subTask);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    public void addingSubTaskToEpic(int id, SubTask subTask){
        Epic epic = Storage.getEpics().get(id);
        epic.getSubTasks().add(subTask.getId());
    }
    @Override
    public void updateTask(Task task, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (Storage.getTasks().containsKey(oldId)) {
            Storage.getTasks().remove(oldId);
            Storage.getTasks().put(task.getId(), task);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }
    @Override
    public void updateEpic(Epic epic, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (Storage.getEpics().containsKey(oldId)) {
            epic.setStatus(Storage.getEpics().get(oldId).getStatus());
            for (Integer key: Storage.getEpics().get(oldId).getSubTasks()) {
                epic.getSubTasks().add(key);
            }
            for (Integer key :Storage.getSubTasks().keySet()) {
                if (Storage.getSubTasks().get(key).getParentId() == oldId){
                    Storage.getSubTasks().get(key).setParentId(epic.getId());
                }

            }
            Storage.getEpics().remove(oldId);
            Storage.getEpics().put(epic.getId(), epic);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }
    @Override
    public void updateSubTask(SubTask subTask, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (Storage.getSubTasks().containsKey(oldId)) {
            Storage.getSubTasks().remove(oldId);
            Storage.getSubTasks().put(subTask.getId(), subTask);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

    public void updatingSubTaskToEpic(int id, SubTask subTask, int oldId){
        Epic epic = Storage.getEpics().get(id);
        epic.getSubTasks().remove(Integer.valueOf(oldId));
        epic.getSubTasks().add(subTask.getId());
    }
    @Override
    public void deleteTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (Storage.getTasks().containsKey(id)) {
            Storage.getTasks().remove(id);
            Managers.getDefaultHistory().remove(id);
            System.out.println("Задача удалена");

        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }
    @Override
    public void deleteEpic(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (Storage.getEpics().containsKey(id)) {
            Storage.getEpics().remove(id);
            Managers.getDefaultHistory().remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }
    @Override
    public void deleteSubTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (Storage.getSubTasks().containsKey(id)) {
            Storage.getSubTasks().remove(id);
            Managers.getDefaultHistory().remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    public void deleteConnectedSubTasks(int id) {
        ArrayList<Integer> keys = new ArrayList<>();
        for (Integer key : Storage.getSubTasks().keySet()) {
            if (Storage.getSubTasks().get(key).getParentId() == id) {
                keys.add(key);
            }
        }
        for (Integer key : keys) {
            Storage.getSubTasks().remove(key);
            Managers.getDefaultHistory().remove(key);
        }
    }

    public ArrayList<SubTask> gettingSubTasksOfEpic(int id) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (Integer key : Storage.getSubTasks().keySet()) {
            if (Storage.getSubTasks().get(key).getParentId() == id)
                subTaskArrayList.add(Storage.getSubTasks().get(key));

        }
        System.out.println(subTaskArrayList);
        return subTaskArrayList;
    }

    public void statusCheckerAndChanger(int epicId){
        int countNewStatus = 0;
        for (Integer subTasksKey : Storage.getEpics().get(epicId).getSubTasks()) {

            if (Storage.getSubTasks().get(subTasksKey).getStatus().equals(TaskStatus.IN_PROGRESS)) {
                Storage.getEpics().get(epicId).setStatus(TaskStatus.IN_PROGRESS);
                return;
            }

            if (Storage.getSubTasks().get(subTasksKey).getStatus().equals(TaskStatus.NEW)) {
                countNewStatus += 1;
            }
        }
        if (countNewStatus == 0) {
            Storage.getEpics().get(epicId).setStatus(TaskStatus.DONE);
        } else if (countNewStatus != Storage.getSubTasks().size()) {
            Storage.getEpics().get(epicId).setStatus(TaskStatus.IN_PROGRESS);
        } else {
            Storage.getEpics().get(epicId).setStatus(TaskStatus.NEW);
        }
    }
}
