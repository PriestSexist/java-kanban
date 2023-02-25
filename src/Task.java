import java.util.*;

public class Task {
    HashMap<Integer, Task> tasks = new HashMap<>();
    private String name;
    private String description;
    private int id;
    private String status;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.id = hashCode();
        this.status = status;
        System.out.println("Хеш код = " + id);
    }

    public Task() {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    HashMap<Integer, Task> getAllTasks(){

        System.out.println("Начинаю искать все задачи");

        if (!tasks.isEmpty()) {
            System.out.println("Вот найденные задачи: ");
            for (Integer key : tasks.keySet()) {
                System.out.println(tasks.get(key));
            }
            return tasks;
        } else {
            System.out.println("Никаких заданий не найдено. Может вы их не добавили?");
            return null;
        }
    }

    void deleteAllTasks(){
        System.out.println("Удаляю задачи...");
        tasks.clear();
    }

    Task getTask(int id){
            System.out.println("Начинаю поиск задачи по представленном идентификатору...");
            System.out.println(tasks.get(id));
            System.out.println("Идентификатор найден. Возвращаю его");
            return tasks.get(id);
    }

    void createTask(Task task) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!tasks.containsKey(task.id)) {
            tasks.put(task.id, task);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }
    void updateTask(Task task, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (tasks.containsKey(oldId)) {
            tasks.remove(oldId);
            tasks.put(task.getId(), task);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

    void deleteTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return getId() == task.getId() && tasks.equals(task.tasks) && getName().equals(task.getName())
                && getDescription().equals(task.getDescription()) && getStatus().equals(task.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, getName(), getDescription(), getId(), getStatus());
    }

    @Override
    public String toString() {
        return "Название задачи - " + name + ", Описание задачи - " + description + ", Идентификатор задачи  - "
                + id + ", Статус задачи - " + status;
    }
}
