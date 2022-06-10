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

    private static class Node {
        Node prev;
        Node next;
        Task data;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    public void linkLast(Task task) {
        removeNode(task);
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        if (oldTail == null) {
            head = newNode;
        }
        tail = newNode;
        customLinkedList.put(task.getId(), newNode);
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
        System.out.println(customLinkedList);
        List<Task> listTask = new ArrayList();
        Node node = head;
        while (node != null) {
            listTask.add(node.data);
            node = node.next;
        }
        return listTask;
    }

    private void removeNode(Task task) {
        int id = task.getId();
        final Node node = customLinkedList.remove(id);

        if (node != null) {
            Node prevNode = node.prev;
            Node nextNode = node.next;
            if (prevNode != null) {
                prevNode.next = nextNode;
                node.prev = null;
            } else {
                head = node.next;
            }
            if (nextNode != null) {
                nextNode.prev = prevNode;
                node.next = null;
            } else {
                tail = node.prev;
            }
            node.data = null;
        }
    }
}