package Manager.TaskManager;

import Manager.Exceptions.ManagerSaveException;
import Manager.HistoryManager.HistoryManager;
import Storage.TaskStatus;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileBackedTasksManager extends InMemoryTaskManager {

    String path;

    public FileBackedTasksManager(HistoryManager historyManager, String path) {
        super(historyManager);
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

    private void save () {
        Path pathToSave = Paths.get(path);
        try {
            try (Writer fileWriter = new FileWriter(path)) {

                if (Files.notExists(pathToSave)) {
                    Files.createFile(pathToSave);
                }

                fileWriter.write("id,type,name,status,description,epic\n");

                for (Integer key : storage.getTasks().keySet()) {
                    fileWriter.write(toString(storage.getTasks().get(key)));
                }
                for (Integer key : storage.getEpics().keySet()) {
                    fileWriter.write(toString(storage.getEpics().get(key)));
                    for (Integer i : storage.getEpics().get(key).getSubTasks()) {
                        fileWriter.write(toString(storage.getSubTasks().get(i)));
                    }
                }

                fileWriter.write("\n");


                fileWriter.write(historyToString(inMemoryHistoryManager));

            } catch (IOException e) {
                throw new ManagerSaveException("IO ошибка");
            }
        } catch (ManagerSaveException exception){
            System.out.println(exception.getDetailMessage());
        }
    }

    public void loadFromFile(String path){
        try {
            try (FileReader reader = new FileReader(path); BufferedReader buffer = new BufferedReader(reader)) {
                String line = buffer.readLine();
                while (buffer.ready()) {
                    line = buffer.readLine();
                    if (!line.equals("")) {
                        Task task = fromString(line);
                        if (task instanceof Epic) {
                            storage.getEpics().put(task.getId(), (Epic) task);
                        } else if (task instanceof SubTask) {
                            storage.getSubTasks().put(task.getId(), (SubTask) task);
                            super.addingSubTaskToEpic(((SubTask) task).getParentId(), (SubTask) task);
                        } else {
                            storage.getTasks().put(task.getId(), task);
                        }
                    } else {
                        line = buffer.readLine();
                        historyFromString(line);
                    }
                }
            } catch (IOException e) {
                throw new ManagerSaveException("IO ошибка");
            }
        } catch (ManagerSaveException exception){
            System.out.println(exception.getDetailMessage());
        }
    }

    private String historyToString(HistoryManager manager){
        StringBuilder line = new StringBuilder();
        for (Task task : manager.getHistory()) {
            line.append(task.getId()).append(", ");
        }
        return line.toString();
    }

    private void historyFromString(String value){
        String[] lines = value.split(", ");
        for (String id : lines){
            if (storage.getTasks().containsKey(Integer.parseInt(id))){
                inMemoryHistoryManager.add(storage.getTasks().get(Integer.parseInt(id)));
            } else if (storage.getEpics().containsKey(Integer.parseInt(id))) {
                inMemoryHistoryManager.add(storage.getEpics().get(Integer.parseInt(id)));
            } else {
                inMemoryHistoryManager.add(storage.getSubTasks().get(Integer.parseInt(id)));
            }
        }
    }

    private String toString(Task task){
        String string =  task.getId() + ", ";
        if (task instanceof Epic){
            return string + TaskType.EPIC + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + "\n";
        } else if (task instanceof SubTask) {
            return string + TaskType.SUBTASK + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + ", " + ((SubTask) task).getParentId() + "\n";
        } else {
            return string + TaskType.TASK + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription() + "\n";
        }
    }

    private Task fromString(String value){
        String[] lines = value.split(", ");
        if (lines[1].equals(TaskType.TASK.toString())){
            return new Task(lines[2], lines[4], TaskStatus.valueOf(lines[3]));
        } else if (lines[1].equals(TaskType.EPIC.toString())) {
            return new Epic(lines[2], lines[4], TaskStatus.valueOf(lines[3]));
        } else {
            return new SubTask(lines[2], lines[4], Integer.parseInt(lines[5]), TaskStatus.valueOf(lines[3]));
        }
    }
}