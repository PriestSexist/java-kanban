package Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {

    public HashMap<Integer, Epic> epics = new HashMap<>();
    public ArrayList<Integer> subTasks;


    public Epic(String name, String description, String status) {
        super(name, description, status);
        subTasks = new ArrayList<>();
    }

    public Epic() {}

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