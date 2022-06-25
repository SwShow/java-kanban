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

    Task findTaskById(int id) throws IOException;  // найти задачу

    void removeTasks() throws IOException; // удалить все задачи

    void updateTask(int id, Task task) throws IOException;  // заменить задачу

    void removeTask(int id) throws IOException;  // удалить задачу

    void createEpic(Epic name);  // создать эпик

    Epic findEpicById(int id) throws IOException;  // найти задачу

    void updateEpic(int id, Epic task) throws IOException; // обновить эпик по идентификатору

    void removeEpics() throws IOException;  // удаление всех эпиков

    void removeEpic(int idEpic) throws IOException;  // удалить эпик по идентификатору

    void addSubTask(int idEpic, SubTask task) throws IOException;  // добавить подзадачу

    //SubTask findValOfId(int id);  // найти подзадачу по идентификатору

    SubTask findSubTaskById(int id) throws IOException;

    void removeSubTask() throws IOException;  //удаление всех подзадач

    MyEnum getStatusSubTask(int id); //  получить статус подзадачи

    void changeSubTask(int id, SubTask task, int idEpic) throws IOException;  // поменять подзадачу по id

    List<Integer> getIdSubTask(int id) throws IOException;

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<Task> getTasks();

    void findSubTasksOfIdEpic(int id) throws IOException;

    List<Task> getHistory() throws IOException;

    int getIdSubTask(SubTask task) throws IOException;

    int getIdEpic(Epic task);


}