

import java.util.ArrayList;
import java.util.HashMap;


public class Manager {
    int id;

    public int calculateId() {  // установить идентификатор
        id++;
        return id;
    }

    HashMap<Integer, Task> Tasks = new HashMap<>();

    public void createTask(Task task) {  // создать задачу
        id = calculateId();
        Tasks.put(id, task);  //  положить в мапу задач
    }

    public Task findTaskById(int id) {   // найти задачу
        return Tasks.get(id);
    }  // получение задачи по идентификатору

    public void removeTasks(HashMap<Integer, Task> Tasks) {  // удалить все задачи
        Tasks.clear();
    }

    public void updateTask(int id, Task task) {  // заменить задачу
        Tasks.replace(id, task);
    }   //  обновление по идентификатору

    public void removeTask(int id) {  // удалить задачу
        Tasks.remove(id);
    }   //  удаление по идентификатору


    HashMap<Integer, Epic> Epics = new HashMap<>();

    void createEpic(Epic name) {  // создать эпик
        int idEpic = calculateId();
        Epics.put(idEpic, name);  // положили эпик в мапу со своим айди и нулевым списком
    }

    public Epic findEpicById(int id) {   // найти задачу
        return Epics.get(id);
    }  //  получение эпика по идентификатору

    public void updateEpic(int id, Epic task) {
        Epics.replace(id, task);
    }  // обновление эпика по идентификатору

    public void removeEpics(HashMap<Integer, Epic> Epics) {  // удаление всех эпиков
        Epics.clear();
    }

    public void removeEpic(int id) {  // удалить задачу
        Epics.remove(id);
    }   //  удаление по идентификатору

    HashMap<Integer, SubTask> SubTasks = new HashMap<>(); //  мапа сабтасков

    void addSubTask(int idEpic, SubTask task) {
        id = calculateId();
        task.idEpic = idEpic;  //  записали в поле сабтаска айди эпика
        SubTasks.put(id, task);//  положили сабтаск в мапу со своим айди
        ArrayList<Integer> ids = getIdSubTask(idEpic);
        ids.add(id); // положить айди сабтаска в лист эпика
        findOllStatusSubTask(idEpic);       // вызвать метод смены статуса
    }

    public void setStatus(String status, int idEpic) {
        Epic find = findEpicById(idEpic);
        find.status = status;
    }

    public SubTask findValOfId(int id) {  // найти подзадачу
        return SubTasks.get(id);
    }  // получение подзадачи по идентификатору

    public void removeSubTask(HashMap<Integer, SubTask> SubTasks) {  //удаление всех подзадач
        SubTasks.clear();
    }

    public String getStatusSubTask(int id) {  //  получить статус подзадачи
        SubTask subTask = SubTasks.get(id);
        return subTask.status;
    }

    public void changeSubTask(int id, SubTask task, int idEpic) {  // поменять подзадачу по id
        SubTasks.replace(id, task);
        task.idEpic = idEpic;
        findOllStatusSubTask(idEpic);  // поменять статус
    }

    public ArrayList<Integer> getIdSubTask(int id) {
        Epic epic = findEpicById(id);
        return epic.idSubTasks;
    }

    public void findSubTasksOfIdEpic(int idEpic) {  // найти подзадачи по id эпика
        ArrayList<Integer> ids = getIdSubTask(idEpic);
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            System.out.println(SubTasks.get(id));
        }
    }

    public void findOllStatusSubTask(int idEpic) {    // смена статуса
        ArrayList<String> statuses = new ArrayList<>();
        ArrayList<Integer> ids = getIdSubTask(idEpic);
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            String stat = getStatusSubTask(id);
            statuses.add(stat);
        }
        if ((statuses.contains("NEW") & !statuses.contains("IN_PROGRESS") & !statuses.contains("DONE")) |
                statuses.isEmpty()) {
            setStatus("NEW", idEpic);
        } else if (statuses.contains("DONE") & !statuses.contains("IN_PROGRESS") & !statuses.contains("NEW")) {
            setStatus("DONE", idEpic);
        } else setStatus("IN_PROGRESS", idEpic);
    }

}
