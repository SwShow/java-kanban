package missions;

import challenges.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> customLinkedList = new HashMap<>();
    private Node head;
    private Node tail;

    public void linkLast(Task task) {
        removeNode(task);
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        int id = task.getIdTask();
        customLinkedList.put(id, newNode);
    }

    private void removeNode(Task task) {
        int id = task.getIdTask();
        final Node node = customLinkedList.get(id);
        if (node != null) {
            Node prevNode = node.prev;
            Node nextNode = node.next;
            if (prevNode == null) {
                head = nextNode;
            } else {
                prevNode.next = nextNode;
                node.prev = null;
            }
            if (nextNode == null) {
                tail = prevNode;
            } else {
                nextNode.prev = prevNode;
                node.next = null;
            }
            node.data = null;
            customLinkedList.remove(id);
        }
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
        return getList();
    }

    public List<Task> getList() {
        List<Task> listTask = new ArrayList();
        Node node = head;
        while (node != null) {
            listTask.add(node.data);
            node = node.next;
        }
        return listTask;
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
}
