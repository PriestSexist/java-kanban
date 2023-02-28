import TaskManager.TaskManager;
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

        TaskManager taskManager = new TaskManager();

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
                    if (!taskManager.storage.getTasks().isEmpty()) {
                        taskManager.getAllTasks();
                    }
                    if (!taskManager.storage.getEpics().isEmpty()) {
                        taskManager.getAllEpics();
                    }
                    if (!taskManager.storage.getSubTasks().isEmpty()){
                        taskManager.getAllSubTasks();
                    }
                    if (taskManager.storage.getTasks().isEmpty() || taskManager.storage.getEpics().isEmpty() || taskManager.storage.getSubTasks().isEmpty()) {
                        System.out.println("Задач не найдено");
                    }
                    break;
                case 2:
                    taskManager.deleteAllTasks();
                    taskManager.deleteAllEpics();
                    taskManager.deleteAllSubTasks();
                    break;
                case 3:
                    System.out.println("Введите ID");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    if (taskManager.storage.getTasks().containsKey(id)) {
                        newTask = taskManager.getTask(id);
                        break;
                    } else if (taskManager.storage.getEpics().containsKey(id)) {
                        newEpic = taskManager.getEpic(id);
                        break;
                    } else if (taskManager.storage.getSubTasks().containsKey(id)){
                        newSubTask = taskManager.getSubTask(id);
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

                        newTask = new Task(name, description, status);
                        taskManager.createTask(newTask);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();

                        newEpic = new Epic(name, description, status);
                        taskManager.createEpic(newEpic);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите идентификатор эпика, подзадачей которого является " +
                                "данная задача");

                        id = scanner.nextInt();
                        scanner.nextLine();

                        if (!taskManager.storage.getEpics().containsKey(id)){
                            System.out.println("Данного эпика не существует!");
                            break;
                        }

                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();

                        newSubTask = new SubTask(name, description, id, status);
                        taskManager.createSubTask(newSubTask);

                        newEpic = taskManager.getEpic(id);
                        taskManager.addingSubTaskToEpic(newEpic, newSubTask);
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

                        newTask = new Task(name, description, status);
                        taskManager.updateTask(newTask, oldId);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи 4)Идентификатор задачи для замены");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();
                        oldId = scanner.nextInt();
                        scanner.nextLine();

                        newEpic = new Epic(name, description, status);
                        taskManager.updateEpic(newEpic, oldId);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите идентификатор эпика, подзадачей которого является " +
                                "данная задача");

                        id = scanner.nextInt();
                        scanner.nextLine();

                        if (!taskManager.storage.getEpics().containsKey(id)){
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

                        newSubTask = new SubTask(name, description, id, status);
                        taskManager.updateSubTask(newSubTask, oldId);

                        newEpic = taskManager.getEpic(id);
                        taskManager.updatingSubTaskToEpic(newEpic, newSubTask, oldId);
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
                        taskManager.deleteTask(id);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        taskManager.deleteEpic(id);
                        taskManager.deleteConnectedSubTasks(id);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        taskManager.deleteSubTask(id);
                        break;
                    } else {
                        System.out.println("Вы ввели не ту команду");
                        break;
                    }
                case 7:
                    System.out.println("Введите идентификатор эпика");
                    id = scanner.nextInt();
                    scanner.nextLine();

                    if (!taskManager.storage.getEpics().containsKey(id)){
                        System.out.println("Данного эпика не существует!");
                        break;
                    }

                    ArrayList<SubTask> subTaskArrayList = taskManager.subTasksOfEpic(id);
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
        System.out.println("0 - Выход");
        System.out.println("Выберите команду");

    }
}
