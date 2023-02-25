import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int command;
        int input;

        Task task = new Task();
        Epic epic = new Epic();
        SubTask subTask = new SubTask();

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
                    if (!task.tasks.isEmpty()) {
                        task.getAllTasks();
                    }
                    if (!epic.epics.isEmpty()) {
                        epic.getAllEpics();
                    }
                    if (!subTask.subTasks.isEmpty()){
                        subTask.getAllSubTasks();
                    }
                    if (task.tasks.isEmpty() || epic.epics.isEmpty() || subTask.subTasks.isEmpty()) {
                        System.out.println("Задач для удаления не найдено");
                    }
                    break;
                case 2:
                    task.deleteAllTasks();
                    epic.deleteAllTasks();
                    subTask.deleteAllTasks();
                    break;
                case 3:
                    System.out.println("Введите ID");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    if (task.tasks.containsKey(id)) {
                        newTask = task.getTask(id);
                        break;
                    } else if (epic.epics.containsKey(id)) {
                        newEpic = epic.getEpic(id);
                        break;
                    } else if (subTask.subTasks.containsKey(id)){
                        newSubTask = subTask.getSubTask(id);
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
                        task.createTask(newTask);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();

                        newEpic = new Epic(name, description, status);
                        epic.createEpic(newEpic);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите идентификатор эпика, подзадачей которого является " +
                                "данная задача");

                        id = scanner.nextInt();
                        scanner.nextLine();

                        if (!epic.epics.containsKey(id)){
                            System.out.println("Данного эпика не существует!");
                            break;
                        }

                        System.out.println("Введите данные задачи, а именно 1)Название задачи 2)Описание задачи " +
                                "3)Статус задачи");

                        name = scanner.nextLine();
                        description = scanner.nextLine();
                        status = scanner.nextLine();

                        newSubTask = new SubTask(name, description, id, status);
                        subTask.createSubTask(newSubTask);

                        newEpic = epic.getEpic(id);
                        epic.addingSubTaskToEpic(newEpic, newSubTask);
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
                        task.updateTask(newTask, oldId);
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
                        epic.updateEpic(newEpic, oldId);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите идентификатор эпика, подзадачей которого является " +
                                "данная задача");

                        id = scanner.nextInt();
                        scanner.nextLine();

                        if (!epic.epics.containsKey(id)){
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
                        subTask.updateSubTask(newSubTask, oldId);

                        newEpic = epic.getEpic(id);
                        epic.updatingSubTaskToEpic(newEpic, newSubTask, oldId);
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
                        task.deleteTask(id);
                        break;
                    } else if (input == 2){
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        epic.deleteTask(id);
                        subTask.deleteConnectedSubTasks(id);
                        break;
                    } else if (input == 3){
                        System.out.println("Введите ID");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        subTask.deleteSubTask(id);
                        break;
                    } else {
                        System.out.println("Вы ввели не ту команду");
                        break;
                    }
                case 7:
                    System.out.println("Введите идентификатор эпика");
                    id = scanner.nextInt();
                    scanner.nextLine();

                    if (!epic.epics.containsKey(id)){
                        System.out.println("Данного эпика не существует!");
                        break;
                    }

                    ArrayList<SubTask> subTaskArrayList = subTask.subTasksOfEpic(id);
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
