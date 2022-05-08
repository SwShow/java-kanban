

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    static HashMap <Integer, Task> Tasks = new HashMap<>();
    static int id;
    public static void createTasks(Task task) {  // создать задачу
        id = setId();
        Tasks.put(id, task);
    }
    public static int setId() {  // установить идентификатор
        id ++;
        return id;
    }
    static HashMap<Integer, Epic> Epics = new HashMap<>();  //  мапа эпиков
    static HashMap<Integer, SubTask> SubTasks = new HashMap<>(); //  мапа сабтасков
    static HashMap<Integer, Integer> ids = new HashMap<>();  // мапа id эпиков и сабтасков

    public static void createEpic(Epic name, SubTask task) {  // создать эпик с подзадачей
        int idEpic = setId();
        Epics.put(idEpic, name);
        id = setId();
        SubTasks.put(id, task);
        ids.put(id, idEpic);
    }

    public static void addSubTask(int idEpic, SubTask task) {
        id = setId();
        SubTasks.put(id, task);
        ids.put(id, idEpic);
    }

    public static Task findOfId(int id) {   // найти задачу
        return Tasks.get(id);
    }
    public static void updateTask(int id, Task task) {  // заменить задачу
        Tasks.replace(id, task);
    }
    public static void removeTask(int id) {  // удалить задачу
        Tasks.remove(id);
    }
    public static Task findValOfId(int id) {  // найти подзадачу
        return SubTasks.get(id);
    }
    public static void changeEpic(int id, Epic task) {
        Epics.replace(id, task);
    }
    public static void changeSubTask(int id, SubTask task) {  // поменять подзадачу по id
        SubTasks.replace(id, task);
        int idEpic = ids.get(5);
        findOllSubTask(idEpic);

    }
    public static String getStatus(int id) {
        return Epic.status;
    }
    public static Task findOfIdEpic(int id) {   // найти задачу
        return Epics.get(id);
    }

    public static void printEpic(int id) {  // найти эпик с подзадачами по id эпика
        System.out.println(findOfIdEpic(id));
        for (int k : ids.keySet()) {
            if (ids.get(k) == id) {
                int key = k;
                System.out.println(findValOfId(key));
            }
        }
    }
    public static void findOllSubTask(int id) {
        ArrayList<String> statuses = new ArrayList<>();
        for (int k : ids.keySet()) {
            if (ids.get(k) == id) {
                int key = k;
                statuses.add(getStatus(key));

            }

        }
        if ((statuses.contains("NEW") & !statuses.contains("IN_PROGRESS") & !statuses.contains("DONE")) |
                statuses.isEmpty()) {
            setStatus("NEW", id);
        } else if (statuses.contains("DONE") & !statuses.contains("IN_PROGRESS") & !statuses.contains("NEW")) {
            setStatus("DONE", id);
        } else setStatus("IN_PROGRESS", id);
    }

    public static void setStatus(String stat, int id) {
        Epic found = (Epic) findOfIdEpic(id);
        found.status = stat;
    }
    public static void removeEpic(int idEpic) {
        for (int k : ids.keySet()) {
            if (ids.get(k) == idEpic) {
                int key = k;
                SubTasks.remove(key);
            }
        }
        Epics.remove(idEpic);
    }
}
