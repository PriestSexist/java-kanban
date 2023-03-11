import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.Managers;
import Storage.TaskStatus;
import Manager.TaskManager.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int command;
        int input;

        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();

        Task newTask;
        Epic newEpic;
        SubTask newSubTask;

        String name;
        String description;
        int id;
        int oldId;
        String status;

        while (true){
            printMenu();

            command = scanner.nextInt();
            scanner.nextLine();

            switch (command){
                case 1:
                    if (!inMemoryTaskManager.storage.getTasks().isEmpty()) {
                        inMemoryTaskManager.getAllTasks();
                    }
                    if (!inMemoryTaskManager.storage.getEpics().isEmpty()) {
                        inMemoryTaskManager.getAllEpics();
                    }
                    if (!inMemoryTaskManager.storage.getSubTasks().isEmpty()){
                        inMemoryTaskManager.getAllSubTasks();
                    }
                    if (inMemoryTaskManager.storage.getTasks().isEmpty() && inMemoryTaskManager.storage.getEpics().isEmpty() && inMemoryTaskManager.storage.getSubTasks().isEmpty()) {
                        System.out.println("Задач не найдено");
                    }
                    break;
                case 2:
                    inMemoryTaskManager.deleteAllTasks();
                    inMemoryTaskManager.deleteAllEpics();
                    inMemoryTaskManager.deleteAllSubTasks();
                    break;
                case 3:
                    System.out.println("Введите ID");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    if (inMemoryTaskManager.storage.getTasks().containsKey(id)) {
                        newTask = inMemoryTaskManager.getTask(id);
                        break;
                    } else if (inMemoryTaskManager.storage.getEpics().containsKey(id)) {
                        newEpic = inMemoryTaskManager.getEpic(id);
                        break;
                    } else if (inMemoryTaskManager.storage.getSubTasks().containsKey(id)){
                        newSubTask = inMemoryTaskManager.getSubTask(id);
                        break;
                    } else {
                        System.out.println("Задач не найдено");
                        break;
                    }
                case 4:
                    System.out.println("Первым делом введите тип задачи. 1 - обычная задача. 2 - эпик. " +
                                       "3 - подзадача");
                    input = scanner.nextInt();
                    scanner.nextLine();
                    if (input == 1) {
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();

                        newTask = new Task(name, description, TaskStatus.valueOf(status));
                        inMemoryTaskManager.createTask(newTask);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();

                        newEpic = new Epic(name, description, TaskStatus.valueOf(status));
                        inMemoryTaskManager.createEpic(newEpic);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите идентификатор эпика, подзадачей которого является " +
                                "данная задача");

                        id = scanner.nextInt();
                        scanner.nextLine();

                        if (!inMemoryTaskManager.storage.getEpics().containsKey(id)){
                            System.out.println("Данного эпика не существует!");
                            break;
                        }

                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();

                        newSubTask = new SubTask(name, description, id, TaskStatus.valueOf(status));
                        inMemoryTaskManager.createSubTask(newSubTask);

                        inMemoryTaskManager.addingSubTaskToEpic(id, newSubTask);
                        break;
                    } else {
                        System.out.println("Вы ввели не ту команду");
                        break;
                    }

                case 5:
                    System.out.println("Первым делом введите тип задачи. 1 - обычная задача. 2 - эпик. " +
                            "3 - подзадача");
                    input = scanner.nextInt();
                    scanner.nextLine();
                    if (input == 1) {
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи 4)Идентификатор задачи для замены");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();
                        oldId = scanner.nextInt();
                        scanner.nextLine();

                        newTask = new Task(name, description, TaskStatus.valueOf(status));
                        inMemoryTaskManager.updateTask(newTask, oldId);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Идентификатор задачи для замены");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        oldId = scanner.nextInt();
                        scanner.nextLine();

                        newEpic = new Epic(name, description, null);
                        inMemoryTaskManager.updateEpic(newEpic, oldId);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите идентификатор эпика, подзадачей которого является " +
                                "данная задача");

                        id = scanner.nextInt();
                        scanner.nextLine();

                        if (!inMemoryTaskManager.storage.getEpics().containsKey(id)){
                            System.out.println("Данного эпика не существует!");
                            break;
                        }

                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи 4)Идентификатор задачи для замены");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();
                        oldId = scanner.nextInt();
                        scanner.nextLine();

                        newSubTask = new SubTask(name, description, id, TaskStatus.valueOf(status));
                        inMemoryTaskManager.updateSubTask(newSubTask, oldId);

                        inMemoryTaskManager.updatingSubTaskToEpic(id, newSubTask, oldId);
                        inMemoryTaskManager.statusCheckerAndChanger(id);
                        break;
                    } else {
                        System.out.println("Вы ввели не ту команду");
                        break;
                    }
                case 6:
                    System.out.println("Первым делом введите тип задачи. 1 - обычная задача. 2 - эпик. " +
                            "3 - подзадача");
                    input = scanner.nextInt();
                    scanner.nextLine();
                    if (input == 1) {
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        inMemoryTaskManager.deleteTask(id);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        inMemoryTaskManager.deleteEpic(id);
                        inMemoryTaskManager.deleteConnectedSubTasks(id);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        inMemoryTaskManager.deleteSubTask(id);
                        break;
                    } else {
                        System.out.println("Вы ввели не ту команду");
                        break;
                    }
                case 7:
                    System.out.println("Введите идентификатор эпика");
                    id = scanner.nextInt();
                    scanner.nextLine();

                    if (!inMemoryTaskManager.storage.getEpics().containsKey(id)){
                        System.out.println("Данного эпика не существует!");
                        break;
                    }

                    ArrayList<SubTask> subTaskArrayList = inMemoryTaskManager.gettingSubTasksOfEpic(id);
                    break;
                case 8:
                    System.out.println("История: ");
                    inMemoryHistoryManager.getHistory();
                    break;
                case 0:
                    System.exit(0);
                    break;

            }

        }

    }

    public static void printMenu(){
        System.out.println("1 - Получение списка всех задач.");
        System.out.println("2 - Удаление всех задач.");
        System.out.println("3 - Получение задачи по идентификатору.");
        System.out.println("4 - Создание задачи. Сам объект должен передаваться в качестве параметра.");
        System.out.println("5 - Обновление задачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.");
        System.out.println("6 - Удаление по идентификатору.");
        System.out.println("7 - Получение списка всех подзадач определённого эпика.");
        System.out.println("8 - вывод истории");
        System.out.println("0 - Выход");
        System.out.println("Выберите команду");

    }
}
