package Functions;

import Storage.Storage;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    public Storage storage = new Storage();

    public HashMap<Integer, Task> getAllTasks(){

        System.out.println("Начинаю искать все задачи");
        System.out.println("Вот найденные задачи: ");
        for (Integer key : storage.getTasks().keySet()) {
            System.out.println(storage.getTasks().get(key));
        }
        return storage.getTasks();

    }

    public HashMap<Integer, Epic> getAllEpics() {

        System.out.println("Вот найденные эпики: ");
        for (Integer key : storage.getEpics().keySet()) {
            System.out.println(storage.getEpics().get(key));
        }
        return storage.getEpics();
    }

    public HashMap<Integer, SubTask> getAllSubTasks() {

        System.out.println("Вот найденные подзадачи: ");
        for (Integer key : storage.getSubTasks().keySet()) {
            System.out.println(storage.getSubTasks().get(key));
        }
        return storage.getSubTasks();
    }

    public void deleteAllTasks(){
        System.out.println("Удаляю задачи...");
        storage.getTasks().clear();
    }

    public void deleteAllEpics() {
        storage.getEpics().clear();
    }

    public void deleteAllSubTasks() {
        storage.getSubTasks().clear();
        System.out.println("Все задачи удалены!");
    }

    public Task getTask(int id){
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(storage.getTasks().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        return storage.getTasks().get(id);
    }

    public Epic getEpic(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(storage.getEpics().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        return storage.getEpics().get(id);
    }

    public SubTask getSubTask(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(storage.getSubTasks().get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        return storage.getSubTasks().get(id);
    }

    public void createTask(Task task) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getTasks().containsKey(task.getId())) {
            storage.getTasks().put(task.getId(), task);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    public void createEpic(Epic epic) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getEpics().containsKey(epic.getId())) {
            storage.getEpics().put(epic.getId(), epic);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    public void createSubTask(SubTask subTask) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!storage.getSubTasks().containsKey(subTask.getId())) {
            storage.getSubTasks().put(subTask.getId(), subTask);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    public void addingSubTaskToEpic(Epic epic, SubTask subTask){
        epic.getSubTasks().add(subTask.getId());
    }

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

    public void updateEpic(Epic epic, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (storage.getEpics().containsKey(oldId)) {
            storage.getEpics().remove(oldId);
            storage.getEpics().put(epic.getId(), epic);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

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

    public void updatingSubTaskToEpic(Epic epic, SubTask subTask, int oldId){
        epic.getSubTasks().remove(Integer.valueOf(oldId));
        epic.getSubTasks().add(subTask.getId());
    }

    public void deleteTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (storage.getTasks().containsKey(id)) {
            storage.getTasks().remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    public void deleteEpic(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (storage.getEpics().containsKey(id)) {
            storage.getEpics().remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    public void deleteSubTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (storage.getSubTasks().containsKey(id)) {
            storage.getSubTasks().remove(id);
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
        }
    }

    public ArrayList<SubTask> subTasksOfEpic(int id) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (Integer key : storage.getSubTasks().keySet()) {
            if (storage.getSubTasks().get(key).getParentId() == id)
                subTaskArrayList.add(storage.getSubTasks().get(key));

        }
        System.out.println(subTaskArrayList);
        return subTaskArrayList;
    }
}
