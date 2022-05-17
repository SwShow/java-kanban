package Missions;

import Challenges.Epic;
import Challenges.SubTask;
import Challenges.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public interface TaskManager {

    public int calculateId();  // установить идентификатор
    public void createTask(Task task);  // создать задачу
    public Task findTaskById(int id);  // найти задачу
    public void removeTasks(); // удалить все задачи
    public void updateTask(int id, Task task);  // заменить задачу
    public void removeTask(int id);  // удалить задачу
    public void createEpic(Epic name);  // создать эпик
    public Epic findEpicById(int id);  // найти задачу
    public void updateEpic(int id, Epic task); // обновить эпик по идентификатору
    public void removeEpics();  // удаление всех эпиков
    public void removeEpic(int idEpic);  // удалить эпик по идентификатору
    public void addSubTask(int idEpic, SubTask task);  // добавить подзадачу
    public void setStatus(String status, int idEpic);
    public SubTask findValOfId(int id);  // найти подзадачу по идентификатору
    public void removeSubTask();  //удаление всех подзадач
    public String getStatusSubTask(int id); //  получить статус подзадачи
    public void changeSubTask(int id, SubTask task, int idEpic);  // поменять подзадачу по id
    public ArrayList<Integer> getIdSubTask(int id);
    public HashMap<Integer, SubTask> findSubTasksOfIdEpic(int idEpic);  // найти подзадачи по id эпика
    public void findOllStatusSubTask(int idEpic);   // смена статуса
    public List<Task> getHistory();
}