package Missions;

import Challenges.Epic;
import Challenges.SubTask;
import Challenges.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    public int id;

    @Override
    public int calculateId() {  // установить идентификатор
        id++;
        return id;
    }

    static LinkedList<Task> history = new LinkedList<>();

    public HashMap<Integer, Task> tasks = new HashMap<>();

    @Override
    public void createTask(Task task) {  // создать задачу
        id = calculateId();
        tasks.put(id, task);  //  положить в мапу задач
    }

    @Override
    public Task findTaskById(int id) {   // найти задачу
        addHistory(tasks.get(id));
        return tasks.get(id);
    }  // получение задачи по идентификатору

    @Override
    public void removeTasks() {  // удалить все задачи
        tasks.clear();
    }

    @Override
    public void updateTask(int id, Task task) {  // заменить задачу
        tasks.replace(id, task);
    }   //  обновление по идентификатору

    @Override
    public void removeTask(int id) {  // удалить задачу
        tasks.remove(id);
    }   //  удаление по идентификатору


    public HashMap<Integer, Epic> epics = new HashMap<>();

    @Override
    public void createEpic(Epic name) {  // создать эпик
        int idEpic = calculateId();
        epics.put(idEpic, name);  // положили эпик в мапу со своим айди и нулевым списком
    }

    @Override
    public Epic findEpicById(int id) {   // найти задачу
        addHistory(epics.get(id));
        return epics.get(id);
    }  //  получение эпика по идентификатору

    @Override
    public void updateEpic(int id, Epic task) {
        epics.replace(id, task);
    }  // обновление эпика по идентификатору

    @Override
    public void removeEpics() {  // удаление всех эпиков
        ArrayList<Integer> idEp = new ArrayList<>();
        for (int ids : epics.keySet()) {  // найти идентификаторы эпиков
            idEp.add(ids);
        }
        for (int id : idEp) {
            removeEpic(id);
        }
    }

    @Override
    public void removeEpic(int idEpic) {// удалить эпик по идентификатору
        HashMap<Integer, SubTask> founds = findSubTasksOfIdEpic(idEpic);  // найти подзадачи
        for (int ids : founds.keySet()) {
            subTasks.remove(ids);
        }
        epics.remove(idEpic);
    }

    public HashMap<Integer, SubTask> subTasks = new HashMap<>(); //  мапа сабтасков

    @Override
    public void addSubTask(int idEpic, SubTask task) {
        id = calculateId();
        task.setIdEpic(idEpic);  //  записали в поле сабтаска айди эпика
        subTasks.put(id, task);//  положили сабтаск в мапу со своим айди
        ArrayList<Integer> ids = getIdSubTask(idEpic);
        ids.add(id); // положить айди сабтаска в лист эпика
        findOllStatusSubTask(idEpic);       // вызвать метод смены статуса
    }

    @Override
    public void setStatus(String status, int idEpic) {
        Epic find = findEpicById(idEpic);
        find.setStatus(status);
    }

    @Override
    public SubTask findValOfId(int id) {  // найти подзадачу по идентификатору
        addHistory(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void removeSubTask() {  //удаление всех подзадач
        subTasks.clear();
    }

    @Override
    public String getStatusSubTask(int id) {  //  получить статус подзадачи
        SubTask subTask = subTasks.get(id);
        return subTask.getStatus();
    }

    @Override
    public void changeSubTask(int id, SubTask task, int idEpic) {  // поменять подзадачу по id
        subTasks.replace(id, task);
        task.setIdEpic(idEpic);
        findOllStatusSubTask(idEpic);  // поменять статус
    }

    @Override
    public ArrayList<Integer> getIdSubTask(int id) {
        Epic epic = findEpicById(id);
        return epic.idSubTasks;
    }

    @Override
    public HashMap<Integer, SubTask> findSubTasksOfIdEpic(int idEpic) {  // найти подзадачи по id эпика
        HashMap<Integer, SubTask> foundSubTasks = new HashMap<>();
        ArrayList<Integer> ids = getIdSubTask(idEpic);  //  лист идентификаторов подзадач
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            SubTask founds = subTasks.get(id);
            foundSubTasks.put(id, founds);
        }
        return foundSubTasks;
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return history;
    }

    public void addHistory(Task task) {
        if (history.size() >= 10) {
            history.removeFirst();
        }
        history.addLast(task);
    }

}
