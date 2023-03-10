package Tasks;

import java.util.*;
import Storage.TaskStatus;

public class Task {
    private final String name;
    private final String description;
    private final int id;
    private TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = hashCode();
        this.status = status;
        System.out.println("Хеш код = " + id);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return getId() == task.getId() && getName().equals(task.getName())
                && getDescription().equals(task.getDescription()) && getStatus().equals(task.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash( getName(), getDescription(), getId(), getStatus());
    }

    @Override
    public String toString() {
        return "Название задачи - " + name + ", Описание задачи - " + description + ", Идентификатор задачи  - "
                + id + ", Статус задачи - " + status;
    }
}
