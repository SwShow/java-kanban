package Missions;

import Challenges.Epic;
import Challenges.SubTask;
import Challenges.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class Manager {
    public int id;

    private int calculateId() {  // установить идентификатор
        id++;
        return id;
    }

    public HashMap<Integer, Task> tasks = new HashMap<>();

    public void createTask(Task task) {  // создать задачу
        id = calculateId();
        tasks.put(id, task);  //  положить в мапу задач
    }

    public Task findTaskById(int id) {   // найти задачу
        return tasks.get(id);
    }  // получение задачи по идентификатору

    public void removeTasks() {  // удалить все задачи
        tasks.clear();
    }

    public void updateTask(int id, Task task) {  // заменить задачу
        tasks.replace(id, task);
    }   //  обновление по идентификатору

    public void removeTask(int id) {  // удалить задачу
        tasks.remove(id);
    }   //  удаление по идентификатору


    public HashMap<Integer, Epic> epics = new HashMap<>();

    public void createEpic(Epic name) {  // создать эпик
        int idEpic = calculateId();
        epics.put(idEpic, name);  // положили эпик в мапу со своим айди и нулевым списком
    }

    public Epic findEpicById(int id) {   // найти задачу
        return epics.get(id);
    }  //  получение эпика по идентификатору

    public void updateEpic(int id, Epic task) {
        epics.replace(id, task);
    }  // обновление эпика по идентификатору

    public void removeEpics() {  // удаление всех эпиков
        ArrayList<Integer> idEp =new ArrayList<>();
        for (int ids : epics.keySet()) {  // найти идентификаторы эпиков
            idEp.add(ids);
        }
        System.out.println("айдиэпик" + idEp);
        for (int id : idEp) {
            removeEpic(id);
        }
    }

    public void removeEpic(int idEpic) {// удалить эпик по идентификатору
        HashMap<Integer, SubTask> founds = findSubTasksOfIdEpic(idEpic);  // найти подзадачи
        for (int ids : founds.keySet()) {
            subTasks.remove(ids);
            System.out.println(subTasks);
        }
        epics.remove(idEpic);
    }

    public HashMap<Integer, SubTask> subTasks = new HashMap<>(); //  мапа сабтасков

    public void addSubTask(int idEpic, SubTask task) {
        id = calculateId();
        task.setIdEpic(idEpic);  //  записали в поле сабтаска айди эпика
        subTasks.put(id, task);//  положили сабтаск в мапу со своим айди
        ArrayList<Integer> ids = getIdSubTask(idEpic);
        ids.add(id); // положить айди сабтаска в лист эпика
        findOllStatusSubTask(idEpic);       // вызвать метод смены статуса
    }

    public void setStatus(String status, int idEpic) {
        Epic find = findEpicById(idEpic);
        find.setStatus(status);
    }

    public SubTask findValOfId(int id) {  // найти подзадачу по идентификатору
        return subTasks.get(id);
    }

    public void removeSubTask() {  //удаление всех подзадач
        subTasks.clear();
    }

    public String getStatusSubTask(int id) {  //  получить статус подзадачи
        SubTask subTask = subTasks.get(id);
        return subTask.getStatus();
    }

    public void changeSubTask(int id, SubTask task, int idEpic) {  // поменять подзадачу по id
        subTasks.replace(id, task);
        task.setIdEpic(idEpic);
        findOllStatusSubTask(idEpic);  // поменять статус
    }

    public ArrayList<Integer> getIdSubTask(int id) {
        Epic epic = findEpicById(id);
        return epic.idSubTasks;
    }

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