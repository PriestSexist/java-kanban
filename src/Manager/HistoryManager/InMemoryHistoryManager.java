package Manager.HistoryManager;

import Storage.Node;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{

    private Node first = null;
    private Node last = null;
    private int size = 0;
    private static final int MAX_SIZE = 10;
    private final Map<Integer, Node> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (size >= MAX_SIZE && !historyMap.containsKey(task.getId())) {
            removeNode(first);
            linkLast(task);
        } else if (size < MAX_SIZE && !historyMap.containsKey(task.getId())) {
            linkLast(task);
        } else {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
    }

    @Override
    public void removeAll() {
        historyMap.clear();
        first = null;
        last = null;
        size = 0;
    }

    private void linkLast(Task task) {

        Node node = new Node(task, null, last);

        if (last != null) {
            historyMap.get(last.getData().getId()).setNext(node);
        } else {
            first = node;
        }

        last = node;
        historyMap.put(task.getId(), node);
        size++;
    }

    public ArrayList<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        if (last != null){
            Node newFirst = first;
            for (int i = 0; i < size; i++){
                tasks.add(newFirst.getData());
                newFirst = newFirst.getNext();
            }
            return tasks;
        }
        return tasks;
    }

    private void removeNode(Node node) {

        Node prevNode = node.getPrev();
        Node nextNode = node.getNext();

        if (prevNode != null && nextNode != null){
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        } else if (prevNode != null) {
            prevNode.setNext(null);
            last = prevNode;
        } else if (nextNode != null){
            nextNode.setPrev(null);
            first = nextNode;
        } else {
            last = null;
        }

        historyMap.remove(node.getData().getId());
        size--;
    }
}
