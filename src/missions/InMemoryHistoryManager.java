package missions;
import challenges.Epic;
import challenges.SubTask;
import challenges.Task;
import java.io.IOException;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    TaskManager manager = Managers.getDefault();
    public final Map<Integer, Node> customLinkedList = new HashMap<>();
    public List<Task> listTask = new ArrayList();
    public static List<Integer> ids = new ArrayList<>();

    public Node head;
    transient Node tail = null;
    int size = 0;

    public static List<Integer> getIds() {
        return ids;
    }

    public void linkLast(Task task) throws IOException {
        int id = getIdOllTasks(task);
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

    public void removeNode(Task task) throws IOException {
        int id = getIdOllTasks(task);
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
        public void addHistory (Task task) throws IOException {
            if (task != null) {
                linkLast(task);
            }
        }

        @Override
        public void remove ( int id) throws IOException {
            Node node = customLinkedList.get(id);
            if (node != null)
                removeNode(node.data);
        }

        @Override
        public List<Task> getHistory () throws IOException {
            return getList();
        }

        public List<Task> getList () throws IOException {
        if (head != null) {
            Node node = head.next;
            while (node != null) {
                listTask.add(node.data);
                Task task = node.data;
                int id = getIdOllTasks(task);
                ids.add(id);
                node = node.next;
            }
            return listTask;
        }
        return listTask;
        }

        public int getIdOllTasks(Task task) throws IOException {
            int id = 0;
            if (task instanceof Epic) {
                id = manager.getIdEpic((Epic) task);
            } else if (task instanceof SubTask) {
                id = manager.getIdSubTask((SubTask) task);
            } else {
                id = task.getId();
            }
            return id;
        }

        public static class Node {
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



