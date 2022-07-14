package missions;
import challenges.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> customLinkedList = new HashMap<>();
    private Node head;
    private transient Node tail = null;
    private int size = 0;


    private void linkLast(Task task) {
        int id = task.getId();
        if (customLinkedList.containsKey(id)) {
            removeNode(task);
        }
        Node l = tail;
        final Node newNode = new Node(l, task, null);
        if (l == null) {
            head = newNode;
        } else {
            l.next = newNode;
        }
        tail = newNode;
        customLinkedList.put(id, newNode);
        size++;
    }

    private void removeNode(Task task) {
        int id = task.getId();
        Node node = customLinkedList.get(id);
        final Node next = node.next;
        final Node prev = node.prev;
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        size--;
    }

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        Node node = customLinkedList.get(id);
        if (node != null)
            removeNode(node.data);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> listTask = new ArrayList<>();
        if (head != null) {
            Node node = head;
            while (node != null) {
                Task task = node.data;
                listTask.add(task);
                node = node.next;
            }
        }
        return listTask;
    }

    public int getSize() {
        return size;
    }

    private static class Node {
        Node prev;
        Node next;
        Task data;

        public Node(Node prev, Task data, Node next) {
            this.prev = prev;
            this.data = data;
            this.next = next;

        }
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "listTask=" + getHistory() +
                '}';
    }
}



