import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SubTask extends Task{

    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int parentId;

    public SubTask(String name, String description, int parentId, String status) {
        super(name, description, status);
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public SubTask() {}

    HashMap<Integer, SubTask> getAllSubTasks() {
        if (!subTasks.isEmpty()) {
            System.out.println("Вот найденные подзадачи: ");
            for (Integer key : subTasks.keySet()) {
                System.out.println(subTasks.get(key));
            }
            return subTasks;
        } else {
            System.out.println("Никаких подзадач не найдено. Может вы их не добавили?");
            return null;
        }
    }

    @Override
    void deleteAllTasks() {
        subTasks.clear();
        System.out.println("Все задачи удалены!");
    }

    SubTask getSubTask(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(subTasks.get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        return subTasks.get(id);
    }

    void createSubTask(SubTask subTask) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    void updateSubTask(SubTask subTask, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (subTasks.containsKey(oldId)) {
            subTasks.remove(oldId);
            subTasks.put(subTask.getId(), subTask);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

    void deleteSubTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (subTasks.containsKey(id)) {
            subTasks.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
    }

    void deleteConnectedSubTasks(int id) {
        ArrayList<Integer> keys = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            if (subTasks.get(key).parentId == id) {
                keys.add(key);
            }
        }
        for (Integer key : keys) {
            subTasks.remove(key);
        }
    }

    ArrayList<SubTask> subTasksOfEpic (int id) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            if (subTasks.get(key).getParentId() == id)
                subTaskArrayList.add(subTasks.get(key));

        }
        System.out.println(subTaskArrayList);
        return subTaskArrayList;
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
