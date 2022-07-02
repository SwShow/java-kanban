package missions;

import challenges.Epic;
import challenges.MyEnum;
import challenges.SubTask;
import challenges.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;


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

    //SubTask findValOfId(int id);  // найти подзадачу по идентификатору

    SubTask findSubTaskById(int id);

    void removeSubTask();  //удаление всех подзадач

    MyEnum getStatusSubTask(int id); //  получить статус подзадачи

    void changeSubTask(int id, SubTask task, int idEpic);  // поменять подзадачу по id

    List<Integer> getIdSubTask(int id);

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<Task> getTasks();

    void findSubTasksOfIdEpic(int id);

    List<Task> getHistory();

    int getIdSubTask(SubTask task);

    int getIdEpic(Epic task);


}