import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task{

    HashMap<Integer, Epic> epics = new HashMap<>();
    private ArrayList<Integer> subTasks;


    public Epic(String name, String description, String status) {
        super(name, description, status);
        subTasks = new ArrayList<>();
    }

    public Epic() {}

    HashMap<Integer, Epic> getAllEpics() {

        if (!epics.isEmpty()) {
            System.out.println("Вот найденные эпики: ");
            for (Integer key : epics.keySet()) {
                System.out.println(epics.get(key));
            }
            return epics;
        } else {
            System.out.println("Никаких эпиков не найдено. Может вы их не добавили?");
            return null;
        }
    }

    @Override
    void deleteAllTasks() {
        epics.clear();
    }

    Epic getEpic(int id) {
        System.out.println("Начинаю поиск задачи по представленном идентификатору...");
        System.out.println(epics.get(id));
        System.out.println("Идентификатор найден. Возвращаю его");
        return epics.get(id);
    }

    void createEpic(Epic epic) {
        System.out.println("Начинаю заносить задачу в программу");
        if (!epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            System.out.println("Задача успешно занесена в программу");
        } else {
            System.out.println("Ой-ой. Похоже данный идентификатор уже занят. Попробуйте изменить входные данные");
        }
    }

    void addingSubTaskToEpic(Epic epic, SubTask subTask){
        epic.subTasks.add(subTask.getId());
    }

    void updateEpic(Epic epic, int oldId) {
        System.out.println("Начинаю поиск задачи для обновления");
        if (epics.containsKey(oldId)) {
            epics.remove(oldId);
            epics.put(epic.getId(), epic);
            System.out.println("Изменения занесены");
        } else {
            System.out.println("Хм... Что-то мы не нашли объект для замены. Может вы ввели не тот идентификатор?");
        }
    }

    void updatingSubTaskToEpic(Epic epic, SubTask subTask, int oldId){
        epic.subTasks.remove(Integer.valueOf(oldId));
        epic.subTasks.add(subTask.getId());
    }

    @Override
    void deleteTask(int id) {
        System.out.println("Начинаю поиск элемента для удаления");
        if (epics.containsKey(id)) {
            epics.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Элемент для удаления не найден. Попробуйте ввести другой идентификатор");
        }
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
