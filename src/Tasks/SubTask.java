package Tasks;

import java.util.Objects;

public class SubTask extends Task {

    private int parentId;

    public SubTask(String name, String description, int parentId, String status) {
        super(name, description, status);
        this.parentId = parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return parentId == subTask.parentId && subTask.getName().equals(this.getName())
                && subTask.getDescription().equals(this.getDescription())
                && subTask.getStatus().equals(this.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getId(), getStatus(), getParentId());
    }
}
