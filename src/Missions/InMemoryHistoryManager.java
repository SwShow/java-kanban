package Missions;

import Challenges.Task;

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
        int id = task.getId();
        removeNode(id);
        Node newNode = new Node(tail, task, null);
        tail = newNode;
        tail.next = newNode;
        tail = newNode;
        customLinkedList.put(task.getId(), newNode);
        System.out.println(customLinkedList);
    }


    @Override
    public void addHistory(Task task) {
        if (task != null) {
            linkLast(task);
        }
    }


    private void removeNode(int id) {
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


    @Override
    public void remove(int id) {
        removeNode(id);
        customLinkedList.remove(id);
    }


    @Override
    public List<Task> getHistory() {
        return getList();
    }

    public List<Task> getList() {
        List<Task> listTask = new ArrayList();
        Node node = tail;
        while (node != null) {
            listTask.add(node.data);
            node = node.prev;
        }
        return listTask;
    }
}