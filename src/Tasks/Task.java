package Tasks;

import Storage.TaskStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final String name;
    private final String description;
    private final int id;
    private TaskStatus status;
    private LocalDateTime startTime;
    private long duration;
    private LocalDateTime endTime;

    public Task(String name, String description, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.id = hashCode();
        this.status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration);
        //System.out.println("Хеш код = " + id);
    }
    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.id = hashCode();
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration);
        //System.out.println("Хеш код = " + id);
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setTime(LocalDateTime startTime, long duration) {
        this.startTime = startTime;
        this.duration = duration;
        setEndTime(startTime.plusMinutes(duration));
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
                + id + ", Статус задачи - " + status + ", время начала задачи " + startTime +
                ", продолжительность задачи (в минутах) " + duration + ", время окончания задачи " + endTime;
    }
}
