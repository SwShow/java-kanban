package missions;

import challenges.Epic;
import challenges.TaskStatus;
import challenges.SubTask;
import challenges.Task;
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

    SubTask findSubTaskById(int id);

    void removeSubTasks();  //удаление всех подзадач

    void removeSubTask(int id);

    TaskStatus getStatusSubTask(int id); //  получить статус подзадачи

    void changeSubTask(int id, SubTask task, int idEpic);  // поменять подзадачу по id

    List<Integer> findSubtasksOfIdEpic(int epicId);

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<Task> getTasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}