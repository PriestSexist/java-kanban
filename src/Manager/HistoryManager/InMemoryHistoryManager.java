package Manager.HistoryManager;

import Manager.TaskManager.InMemoryTaskManager;
import Tasks.Task;
import Storage.Node;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private Node first = null;
    private Node prev = null;
    private int size = 0;

    private InMemoryTaskManager inMemoryTaskManager;

    public InMemoryHistoryManager(InMemoryTaskManager inMemoryTaskManager) {
        this.inMemoryTaskManager = inMemoryTaskManager;
    }

    public void add(Task task) {
        int numb = 10;
        if (size >= numb && !inMemoryTaskManager.storage.getHistoryMap().containsKey(task.getId())) {
            removeNode(first);
            linkLast(task);
        } else if (inMemoryTaskManager.storage.getHistoryMap().containsKey(task.getId())) {
            remove(task.getId());
            linkLast(task);
        } else if (size < numb) {
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(inMemoryTaskManager.storage.getHistoryMap().get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkLast(Task task) {

        Node node = new Node(task, null, prev);

        if (prev != null) {
            inMemoryTaskManager.storage.getHistoryMap().get(prev.getData().getId()).setNext(node);
        } else {
            first = node;
        }

        prev = node;
        inMemoryTaskManager.storage.getHistoryMap().put(task.getId(), node);
        size++;
    }

    public ArrayList<Task> getTasks(){
        if (prev != null){
            ArrayList<Task> tasks = new ArrayList<>();
            Node newPrev = prev;
            for (int i = 0; i < size; i++){
                tasks.add(newPrev.getData());
                newPrev = newPrev.getPrev();
            }
            return tasks;
        }
        return null;
    }

    public void removeNode(Node node) {

        Node prevNode = node.getPrev();
        Node nextNode = node.getNext();

        if (prevNode != null && nextNode != null){
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        } else if (prevNode != null) {
            prevNode.setNext(null);
            prev = prevNode;
        } else if (nextNode != null){
            nextNode.setPrev(null);
            first = nextNode;
        } else {
            prev = null;
        }

        inMemoryTaskManager.storage.getHistoryMap().remove(node.getData().getId());
        size--;
    }
}
