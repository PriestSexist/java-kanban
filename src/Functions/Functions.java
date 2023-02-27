package Functions;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Functions {

    public Task mainTask = new Task();
    public Epic mainEpic = new Epic();
    public SubTask mainSubTask = new SubTask();

    public HashMap<Integer, Task> getAllTasks(){

        System.out.println("Начинаю искать все задачи");

        if (!mainTask.tasks.isEmpty()) {
            System.out.println("Вот найденные задачи: ");
            for (Integer key : mainTask.tasks.keySet()) {
                System.out.println(mainTask.tasks.get(key));
            }
            return mainTask.tasks;
        } else {
            System.out.println("Никаких заданий не найдено. Может вы их не добавили?");
            return null;
        }
    }

    public HashMap<Integer, Epic> getAllEpics() {

        if (!mainEpic.epics.isEmpty()) {
            System.out.println("Вот найденные эпики: ");
            for (Integer key : mainEpic.epics.keySet()) {
                System.out.println(mainEpic.epics.get(key));
            }
            return mainEpic.epics;
        } else {
            System.out.println("Никаких эпиков не найдено. Может вы их не добавили?");
            return null;
        }
    }

    public HashMap<Integer, SubTask> getAllSubTasks() {
        if (!mainSubTask.subTasks.isEmpty()) {
            System.out.println("Вот найденные подзадачи: ");
            for (Integer key : mainSubTask.subTasks.keySet()) {
                System.out.println(mainSubTask.subTasks.get(key));
            }
            return mainSubTask.subTasks;
        } else {
            System.out.println("Никаких подзадач не найдено. Может вы их не добавили?");
            return null;
        }
    }

    public void deleteAllTasks(){
        System.out.println("Удаляю задачи...");
        mainTask.tasks.clear();
    }

    public void deleteAllEpics() {
        mainEpic.epics.clear();
    }

    public void deleteAllSubTasks() {
        mainSubTask.subTasks.clear();
        System.out.println("Все задачи удалены!");
    }

    public Task getTask(int id){
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(mainTask.tasks.get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        return mainTask.tasks.get(id);
    }

    public Epic getEpic(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(mainEpic.epics.get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        return mainEpic.epics.get(id);
    }

    public SubTask getSubTask(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(mainSubTask.subTasks.get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        return mainSubTask.subTasks.get(id);
    }

    public void createTask(Task task) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!mainTask.tasks.containsKey(task.getId())) {
            mainTask.tasks.put(task.getId(), task);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    public void createEpic(Epic epic) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!mainEpic.epics.containsKey(epic.getId())) {
            mainEpic.epics.put(epic.getId(), epic);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    public void createSubTask(SubTask subTask) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!mainSubTask.subTasks.containsKey(subTask.getId())) {
            mainSubTask.subTasks.put(subTask.getId(), subTask);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    public void addingSubTaskToEpic(Epic epic, SubTask subTask){
        epic.subTasks.add(subTask.getId());
    }

    public void updateTask(Task task, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (mainTask.tasks.containsKey(oldId)) {
            mainTask.tasks.remove(oldId);
            mainTask.tasks.put(task.getId(), task);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

    public void updateEpic(Epic epic, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (mainEpic.epics.containsKey(oldId)) {
            mainEpic.epics.remove(oldId);
            mainEpic.epics.put(epic.getId(), epic);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

    public void updateSubTask(SubTask subTask, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (mainSubTask.subTasks.containsKey(oldId)) {
            mainSubTask.subTasks.remove(oldId);
            mainSubTask.subTasks.put(subTask.getId(), subTask);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

    public void updatingSubTaskToEpic(Epic epic, SubTask subTask, int oldId){
        epic.subTasks.remove(Integer.valueOf(oldId));
        epic.subTasks.add(subTask.getId());
    }

    public void deleteTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (mainTask.tasks.containsKey(id)) {
            mainTask.tasks.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    public void deleteEpic(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (mainEpic.epics.containsKey(id)) {
            mainEpic.epics.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    public void deleteSubTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (mainSubTask.subTasks.containsKey(id)) {
            mainSubTask.subTasks.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    public void deleteConnectedSubTasks(int id) {
        ArrayList<Integer> keys = new ArrayList<>();
        for (Integer key : mainSubTask.subTasks.keySet()) {
            if (mainSubTask.subTasks.get(key).getParentId() == id) {
                keys.add(key);
            }
        }
        for (Integer key : keys) {
            mainSubTask.subTasks.remove(key);
        }
    }

    public ArrayList<SubTask> subTasksOfEpic(int id) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (Integer key : mainSubTask.subTasks.keySet()) {
            if (mainSubTask.subTasks.get(key).getParentId() == id)
                subTaskArrayList.add(mainSubTask.subTasks.get(key));

        }
        System.out.println(subTaskArrayList);
        return subTaskArrayList;
    }
}
