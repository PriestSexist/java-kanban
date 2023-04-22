package Manager.TaskManager;

import Manager.Exceptions.ManagerSaveException;
import Manager.Managers;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String path;

    public FileBackedTasksManager(String path) {
        super(Managers.getDefaultHistory());
        this.path = path;
    }

    @Override
    public HashMap<Integer, Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void addingSubTaskToEpic(int id, SubTask subTask) {
        super.addingSubTaskToEpic(id, subTask);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task, int oldId) {
        super.updateTask(task, oldId);
        save();
    }

    @Override
    public void updateEpic(Epic epic, int oldId) {
        super.updateEpic(epic, oldId);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask, int oldId) {
        super.updateSubTask(subTask, oldId);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    private void save () {
        Path pathToSave = Paths.get(path);
        try (Writer fileWriter = new FileWriter(path)) {

            if (Files.notExists(pathToSave)) {
                Files.createFile(pathToSave);
            }

            fileWriter.write("id,type,name,status,description,epic\n");

            for (Integer key : getStorage().getTasks().keySet()) {
                fileWriter.write(toString(getStorage().getTasks().get(key)));
            }
            for (Integer key : getStorage().getEpics().keySet()) {
                fileWriter.write(toString(getStorage().getEpics().get(key)));
                for (Integer i : getStorage().getEpics().get(key).getSubTasks()) {
                    fileWriter.write(toString(getStorage().getSubTasks().get(i)));
                }
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString());

        } catch (IOException e) {
            throw new ManagerSaveException("IO ошибка");
        }
    }

    public static FileBackedTasksManager loadFromFile(String path){
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("C:\\test.txt");
        try (FileReader reader = new FileReader(path); BufferedReader buffer = new BufferedReader(reader)) {
            String line = buffer.readLine();
            while (buffer.ready()) {
                line = buffer.readLine();
                if (!line.equals("")) {
                    Task task = fromString(line);
                    // switch case? Не совсем понимаю, как switch case можно использовать с instanceof
                    if (task instanceof Epic) {
                        fileBackedTasksManager.getStorage().getEpics().put(task.getId(), (Epic) task);
                    } else if (task instanceof SubTask) {
                        fileBackedTasksManager.getStorage().getSubTasks().put(task.getId(), (SubTask) task);
                        Epic epic =  fileBackedTasksManager.getStorage().getEpics().get(((SubTask) task).getParentId());
                        epic.getSubTasks().add(task.getId());
                    } else {
                        fileBackedTasksManager.getStorage().getTasks().put(task.getId(), task);
                    }
                } else {
                    line = buffer.readLine();
                    historyFromString(line, fileBackedTasksManager);
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException("IO ошибка");
        }
    }

    private static String historyToString(){
        StringBuilder line = new StringBuilder();
        for (Task task : inMemoryHistoryManager.getHistory()) {
            line.append(task.getId()).append(", ");
        }
        return line.toString();
    }

    private static void historyFromString(String value, FileBackedTasksManager fileBackedTasksManager){
        String[] lines = value.split(", ");
        for (String id : lines){
            //switch case? Не совсем понимаю, как использовать тут switch case, где я проверяю наличие ключа в мапе
            if (fileBackedTasksManager.getStorage().getTasks().containsKey(Integer.parseInt(id))){
                inMemoryHistoryManager.add(fileBackedTasksManager.getStorage().getTasks().get(Integer.parseInt(id)));
            } else if (fileBackedTasksManager.getStorage().getEpics().containsKey(Integer.parseInt(id))) {
                inMemoryHistoryManager.add(fileBackedTasksManager.getStorage().getEpics().get(Integer.parseInt(id)));
            } else {
                inMemoryHistoryManager.add(fileBackedTasksManager.getStorage().getSubTasks().get(Integer.parseInt(id)));
            }
        }
    }

    private String toString(Task task){
        if (task instanceof Epic){
            return task.getId() + ", " + TaskType.EPIC + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + "\n";
        } else if (task instanceof SubTask) {
            return task.getId() + ", " + TaskType.SUBTASK + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + ", " + ((SubTask) task).getParentId() + "\n";
        }
        return task.getId() + ", " + TaskType.TASK + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + "\n";
    }

    private static Task fromString(String value){
        String[] lines = value.split(", ");
        Task task = null;
        switch (Enum.valueOf(TaskType.class, lines[1])){
            case TASK :
                task = new Task(lines[2], lines[4], TaskStatus.valueOf(lines[3]));
                break;
            case EPIC:
                task = new Epic(lines[2], lines[4], TaskStatus.valueOf(lines[3]));
                break;
            case SUBTASK:
                task = new SubTask(lines[2], lines[4], Integer.parseInt(lines[5]), TaskStatus.valueOf(lines[3]));
                break;
        }
        return task;
    }
}