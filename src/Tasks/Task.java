package Tasks;

import java.util.*;

public class Task {
    public HashMap<Integer, Task> tasks = new HashMap<>();
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
