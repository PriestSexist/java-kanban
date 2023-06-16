import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.Managers;
import Manager.TaskManager.HttpTaskManager;
import Storage.Storage;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int command;
        int input;

        Storage storage = Managers.getDefaultStorage();
        InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
        HttpTaskManager httpTaskManager = (HttpTaskManager) Managers.getDefault();

        Task newTask;
        Epic newEpic;
        SubTask newSubTask;

        String name;
        String description;
        int id;
        int oldId;
        String status;
        String startTime;
        long duration;

        while (true){
            printMenu();

            command = scanner.nextInt();
            scanner.nextLine();

            switch (command){
                case 1:
                    if (!storage.getTasks().isEmpty()) {
                        httpTaskManager.getAllTasks();
                    }
                    if (!storage.getEpics().isEmpty()) {
                        httpTaskManager.getAllEpics();
                    }
                    if (!storage.getSubTasks().isEmpty()){
                        httpTaskManager.getAllSubTasks();
                    }
                    if (storage.getTasks().isEmpty() && storage.getEpics().isEmpty() && storage.getSubTasks().isEmpty()) {
                        System.out.println("Задач не найдено");
                    }
                    break;
                case 2:
                    httpTaskManager.deleteAllTasks();
                    break;
                case 3:
                    System.out.println("Введите ID");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    if (storage.getTasks().containsKey(id)) {
                        newTask = httpTaskManager.getTask(id);
                        break;
                    } else if (storage.getEpics().containsKey(id)) {
                        newEpic = httpTaskManager.getEpic(id);
                        break;
                    } else if (storage.getSubTasks().containsKey(id)){
                        newSubTask = httpTaskManager.getSubTask(id);
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
                                "3)Время начала задачи (в формате dd.MM.yyyy, HH:mm) 4)Продолжительность задачи (в минутах)");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        startTime = scanner.nextLine();
                        duration = scanner.nextInt();
                        scanner.nextLine();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
                        LocalDateTime localDateTimeStartTime = LocalDateTime.parse(startTime, formatter);

                        newTask = new Task(name, description, localDateTimeStartTime, duration);
                        httpTaskManager.createTask(newTask);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Время начала задачи (в формате dd.MM.yyyy, HH:mm) 4)Продолжительность задачи (в минутах)");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        startTime = scanner.nextLine();
                        duration = scanner.nextInt();
                        scanner.nextLine();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
                        LocalDateTime localDateTimeStartTime = LocalDateTime.parse(startTime, formatter);

                        newEpic = new Epic(name, description, localDateTimeStartTime, duration);
                        httpTaskManager.createEpic(newEpic);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите идентификатор эпика, подзадачей которого является " +
                                "данная задача");

                        id = scanner.nextInt();
                        scanner.nextLine();

                        if (!storage.getEpics().containsKey(id)){
                            System.out.println("Данного эпика не существует!");
                            break;
                        }

                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Время начала задачи (в формате dd.MM.yyyy, HH:mm) 4)Продолжительность задачи (в минутах)");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        startTime = scanner.nextLine();
                        duration = scanner.nextInt();
                        scanner.nextLine();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
                        LocalDateTime localDateTimeStartTime = LocalDateTime.parse(startTime, formatter);

                        newSubTask = new SubTask(name, description, localDateTimeStartTime, duration, id);
                        httpTaskManager.createSubTask(newSubTask);
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
                                "3)Статус задачи 4)Идентификатор задачи для замены 5)Время начала задачи (в формате dd.MM.yyyy, HH:mm) 6)Продолжительность задачи (в минутах)");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();
                        oldId = scanner.nextInt();
                        scanner.nextLine();
                        startTime = scanner.nextLine();
                        duration = scanner.nextInt();
                        scanner.nextLine();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
                        LocalDateTime localDateTimeStartTime = LocalDateTime.parse(startTime, formatter);

                        newTask = new Task(name, description, TaskStatus.valueOf(status), localDateTimeStartTime, duration);
                        httpTaskManager.updateTask(newTask, oldId);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи 4)Идентификатор задачи для замены 5)Время начала задачи (в формате dd.MM.yyyy, HH:mm) 6)Продолжительность задачи (в минутах)");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();
                        oldId = scanner.nextInt();
                        scanner.nextLine();
                        startTime = scanner.nextLine();
                        duration = scanner.nextInt();
                        scanner.nextLine();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
                        LocalDateTime localDateTimeStartTime = LocalDateTime.parse(startTime, formatter);

                        newEpic = new Epic(name, description, TaskStatus.valueOf(status), localDateTimeStartTime, duration);
                        httpTaskManager.updateEpic(newEpic, oldId);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите идентификатор эпика, подзадачей которого является " +
                                "данная задача");

                        id = scanner.nextInt();
                        scanner.nextLine();

                        if (!storage.getEpics().containsKey(id)){
                            System.out.println("Данного эпика не существует!");
                            break;
                        }

                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи 4)Идентификатор задачи для замены 5)Время начала задачи (в формате dd.MM.yyyy, HH:mm) 6)Продолжительность задачи (в минутах)");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();
                        oldId = scanner.nextInt();
                        scanner.nextLine();
                        startTime = scanner.nextLine();
                        duration = scanner.nextInt();
                        scanner.nextLine();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
                        LocalDateTime localDateTimeStartTime = LocalDateTime.parse(startTime, formatter);

                        newSubTask = new SubTask(name, description, TaskStatus.valueOf(status), localDateTimeStartTime, duration, id);
                        httpTaskManager.updateSubTask(newSubTask, oldId);

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
                        httpTaskManager.deleteTask(id);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        httpTaskManager.deleteEpic(id);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        httpTaskManager.deleteSubTask(id);
                        break;
                    } else {
                        System.out.println("Вы ввели не ту команду");
                        break;
                    }
                case 7:
                    System.out.println("Введите идентификатор эпика");
                    id = scanner.nextInt();
                    scanner.nextLine();

                    if (!storage.getEpics().containsKey(id)){
                        System.out.println("Данного эпика не существует!");
                        break;
                    }

                    ArrayList<SubTask> subTaskArrayList = httpTaskManager.gettingSubTasksOfEpic(id);
                    break;
                case 8:
                    System.out.println("История: ");
                    System.out.println(inMemoryHistoryManager.getTasks());
                    break;
                case 9:
                    System.out.println("Список задач в порядке приоритета: ");
                    System.out.println(httpTaskManager.getPrioritizedTasks());
                    break;
                case 10:
                    System.out.println("Подтягиваю таски сервера: ");
                    httpTaskManager.load();
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
        System.out.println("9 - вывод списка задач в порядке приоритета");
        System.out.println("10 - Подтянуть таски с сервера. (!!!ОПАСНО!!!, ВСЕ ТАСКИ, КОТОРЫХ НЕТ НА СЕРВЕРЕ, ПРОПАДУТ. НО ВСЁ ТУДА СОХРАНЯЕТСЯ ПОСЛЕ СОЗДАНИЯ ТУТ, ДАЙ БОГ!)");
        System.out.println("0 - Выход");
        System.out.println("Выберите команду");

    }
}