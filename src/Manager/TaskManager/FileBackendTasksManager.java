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
import java.time.LocalDateTime;


public class FileBackendTasksManager extends InMemoryTaskManager {

    private final String path;

    public FileBackendTasksManager(String path) {
        super(Managers.getDefaultHistory());
        this.path = path;
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
    public void createSubTask(int id, SubTask subTask) {
        super.createSubTask(id, subTask);
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
    public void updateSubTask(int id, SubTask subTask, int oldId) {
        super.updateSubTask(id, subTask, oldId);
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

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }


    private void save () {
        Path pathToSave = Paths.get(path);
        try (Writer fileWriter = new FileWriter(path)) {

            if (Files.notExists(pathToSave)) {
                Files.createFile(pathToSave);
            }

            fileWriter.write("id,type,name,status,description,startTime,duration,endTime,epic\n");

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

    public static FileBackendTasksManager loadFromFile(String path){
        FileBackendTasksManager fileBackendTasksManager = new FileBackendTasksManager(path);
        try (FileReader reader = new FileReader(path); BufferedReader buffer = new BufferedReader(reader)) {
            String line = buffer.readLine();
            while (buffer.ready()) {
                line = buffer.readLine();
                if (!line.equals("")) {
                    Task task = fromString(line);
                    if (task instanceof Epic) {
                        fileBackendTasksManager.getStorage().getEpics().put(task.getId(), (Epic) task);
                    } else if (task instanceof SubTask) {
                        fileBackendTasksManager.getStorage().getSubTasks().put(task.getId(), (SubTask) task);
                        Epic epic =  fileBackendTasksManager.getStorage().getEpics().get(((SubTask) task).getParentId());
                        epic.getSubTasks().add(task.getId());
                    } else {
                        fileBackendTasksManager.getStorage().getTasks().put(task.getId(), task);
                    }
                } else {
                    line = buffer.readLine();
                    if (line != null) {
                        historyFromString(line, fileBackendTasksManager);
                    }
                }
            }
            return fileBackendTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException("IO ошибка");
        }
    }

    private static String historyToString(){
        StringBuilder line = new StringBuilder();

        if (inMemoryHistoryManager.getTasks().size()>=1) {
            for (int i = 0; i < inMemoryHistoryManager.getTasks().size() - 1; i++) {
                line.append(inMemoryHistoryManager.getTasks().get(i).getId()).append(", ");
            }
            line.append(inMemoryHistoryManager.getTasks().get(inMemoryHistoryManager.getTasks().size() - 1).getId());
        } else {
            line.append("");
        }
        return line.toString();
    }

    private static void historyFromString(String value, FileBackendTasksManager fileBackendTasksManager){
        String[] lines = value.split(", ");
        for (String id : lines){
            if (fileBackendTasksManager.getStorage().getTasks().containsKey(Integer.parseInt(id))){
                inMemoryHistoryManager.add(fileBackendTasksManager.getStorage().getTasks().get(Integer.parseInt(id)));
            } else if (fileBackendTasksManager.getStorage().getEpics().containsKey(Integer.parseInt(id))) {
                inMemoryHistoryManager.add(fileBackendTasksManager.getStorage().getEpics().get(Integer.parseInt(id)));
            } else if (fileBackendTasksManager.getStorage().getSubTasks().containsKey(Integer.parseInt(id))){
                inMemoryHistoryManager.add(fileBackendTasksManager.getStorage().getSubTasks().get(Integer.parseInt(id)));
            }
        }
    }

    private String toString(Task task){
        if (task instanceof Epic){
            return task.getId() + ", " + TaskType.EPIC + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + ", " + task.getStartTime() + ", " + task.getDuration() + ", " + task.getEndTime() + "\n";
        } else if (task instanceof SubTask) {
            return task.getId() + ", " + TaskType.SUBTASK + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + ", " + task.getStartTime() + ", " + task.getDuration() + ", " + task.getEndTime() + ", " + ((SubTask) task).getParentId() + "\n";
        }
        return task.getId() + ", " + TaskType.TASK + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + ", " + task.getStartTime() + ", " + task.getDuration() + ", " + task.getEndTime() + "\n";
    }

    private static Task fromString(String value){
        String[] lines = value.split(", ");
        switch (Enum.valueOf(TaskType.class, lines[1])){
            case TASK :
                return new Task(lines[2], lines[4], TaskStatus.valueOf(lines[3]), LocalDateTime.parse(lines[5]), Long.parseLong(lines[6]));
            case EPIC:
                return new Epic(lines[2], lines[4], TaskStatus.valueOf(lines[3]), LocalDateTime.parse(lines[5]), Long.parseLong(lines[6]));
            case SUBTASK:
                return new SubTask(lines[2], lines[4], TaskStatus.valueOf(lines[3]), LocalDateTime.parse(lines[5]), Long.parseLong(lines[6]), Integer.parseInt(lines[8]));
        }
        return null;
    }
}