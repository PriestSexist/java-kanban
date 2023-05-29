package Tasks;

import Storage.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> subTasks;

    public Epic(String name, String description, LocalDateTime startTime, long duration) {
        super(name, description, startTime, duration);
        subTasks = new ArrayList<>();
    }

    public Epic(String name, String description, TaskStatus status, LocalDateTime startTime, long duration) {
        super(name, description, status, startTime, duration);
        subTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return getId() == epic.getId() && subTasks.equals(epic.subTasks) && epic.getName().equals(this.getName())
                && epic.getDescription().equals(this.getDescription()) && epic.getStatus().equals(this.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getId(), getStatus(), subTasks);
    }
}