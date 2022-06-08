package Missions;

import Challenges.Epic;
import Challenges.MyEnum;
import Challenges.SubTask;
import Challenges.Task;

import java.util.ArrayList;


public interface TaskManager {


    void createTask(Task task);  // создать задачу

    Task findTaskById(int id);  // найти задачу

    void removeTasks(); // удалить все задачи

    void updateTask(int id, Task task);  // заменить задачу

    void removeTask(int id);  // удалить задачу

    void createEpic(Epic name);  // создать эпик

    Epic findEpicById(int id);  // найти задачу

    void updateEpic(int id, Epic task); // обновить эпик по идентификатору

    void removeEpics();  // удаление всех эпиков

    void removeEpic(int idEpic);  // удалить эпик по идентификатору

    void addSubTask(int idEpic, SubTask task);  // добавить подзадачу

    SubTask findValOfId(int id);  // найти подзадачу по идентификатору

    void removeSubTask();  //удаление всех подзадач

    MyEnum getStatusSubTask(int id); //  получить статус подзадачи

    void changeSubTask(int id, SubTask task, int idEpic);  // поменять подзадачу по id

    ArrayList<Integer> getIdSubTask(int id);

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getSubTasks();

    ArrayList<Task> getTasks();

    void findSubTasksOfIdEpic(int i);

}