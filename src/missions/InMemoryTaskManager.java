package missions;

import challenges.Epic;
import challenges.TaskStatus;
import challenges.SubTask;
import challenges.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int id = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>(); //  мапа сабтасков

    protected final TreeSet<Task> prioritySet = new TreeSet<>();


    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void createTask(Task task) {  // создать задачу
        // проверка, что task задан корректно
        if (task == null || isTaskIntersects(task))
            return;

        //  положить в мапу задач
        int idTask = calculateId();
        task.setId(idTask);
        tasks.put(idTask, task);

        // обновить prioritySet
        prioritySet.add(task);
    }

    @Override
    public Task findTaskById(int id) {   // получение задачи по идентификатору
        Task tas = tasks.get(id);
        if (tas != null) {
            historyManager.addHistory(tas);
        }
        return tas;
    }

    @Override
    public void removeTasks() {  // удалить все задачи
        Set<Integer> keys = tasks.keySet();
        for (int id : keys) {
            historyManager.remove(id);
            prioritySet.remove(tasks.get(id));
        }
        tasks.clear();
    }

    @Override
    public void updateTask(int id, Task task) {  // заменить задачу
        // проверка, что task задан корректно
        if (!tasks.containsKey(id) || task == null || isTaskIntersects(task))
            return;

        // обновить prioritySet
        Task oldTask = tasks.get(id);
        prioritySet.remove(oldTask);
        prioritySet.add(task);

        // обновить историю
        historyManager.remove(id);
        historyManager.addHistory(task);

        // добавить в таски
        task.setId(id);
        tasks.replace(id, task);
    }   //  обновление по идентификатору

    @Override
    public void removeTask(int id) {  // удалить задачу
        if (tasks.containsKey(id)) {
            prioritySet.remove(tasks.get(id));
            historyManager.remove(id);
            tasks.remove(id);
        }
    }   //  удаление по идентификатору


    @Override
    public void createEpic(Epic name) {  // создать эпик
        int idEpic = calculateId();
        name.setId(idEpic);
        name.setStatus(TaskStatus.NEW);
        epics.put(idEpic, name);  // положили эпик в мапу со своим айди и нулевым списком
    }

    @Override
    public Epic findEpicById(int id) {   //  получение эпика по идентификатору
        Epic ep = epics.get(id);
        if (ep != null) {
            historyManager.addHistory(ep);
        }
        return ep;
    }

    @Override
    public void updateEpic(int id, Epic task) {   // обновление эпика по идентификатору
        if (!epics.containsKey(id) || task == null)
            return;

        List<Integer> idSub = findSubtasksOfIdEpic(id); //  получить список идентификаторов подзадач
        for (int i : idSub) {
            prioritySet.remove(subTasks.get(i));
            subTasks.remove(i);
            historyManager.remove(i);
        }
        historyManager.remove(id);
        epics.replace(id, task);  //  заменить эпик
        task.setId(id);
    }

    @Override
    public void removeEpics() {  // удаление всех эпиков
        ArrayList<Integer> idEp = new ArrayList<>(epics.keySet()); // найти идентификаторы эпиков
        for (int id : idEp) {  // для каждого идентификатора эпика
            removeEpic(id); // вызвать метод удаления эпика вместе с подзадачами
        }
    }


    @Override
    public void removeEpic(int idEpic) {// удалить эпик по идентификатору
        if (!epics.containsKey(idEpic))
            return;
        List<Integer> ids = findSubtasksOfIdEpic(idEpic);// найти подзадачи
        for (int id : ids) {
            prioritySet.remove(subTasks.get(id));
            subTasks.remove(id);
            historyManager.remove(id);
        }
        historyManager.remove(idEpic);
        epics.remove(idEpic);
    }

    @Override
    public void addSubTask(int idEpic, SubTask task) {  // добавить подзадачу
        if (!epics.containsKey(idEpic) || task == null || isTaskIntersects(task))
            return;

        id = calculateId();
        task.setIdEpic(idEpic);//  записали в поле сабтаска айди эпика
        task.setId(id);
        subTasks.put(id, task);//  положили сабтаск в мапу со своим айди
        Epic epic = epics.get(idEpic);
        epic.addSubTask(task); // добавить сабтаск в эпик
        updateEpicStatus(idEpic);       // вызвать метод смены статуса

        // добавили в prioritySet
        prioritySet.add(task);
    }


    @Override
    public SubTask findSubTaskById(int id) {  // найти подзадачу по идентификатору
        SubTask sub = subTasks.get(id);
        if (sub != null) {
            historyManager.addHistory(sub);
        }
        return sub;
    }

    @Override
    public void removeSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            // удалили из prioritySet
            prioritySet.remove(subTask);

            // удалить из сабтасков
            subTasks.remove(id);

            // удалить из эпика
            Epic epic = epics.get(subTask.getIdEpic());
            epic.removeSubTask(id);
            updateEpicStatus(subTask.getIdEpic());

            // удалить из истории
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubTasks() {  //удаление всех подзадач
        for (int j : epics.keySet()) {   // для каждого эпика
            Epic epic = epics.get(j);
            List<Integer> founds = epic.getIdSubtasks(); // найти массив идентификаторов подзадач
            for (int id : founds) {
                historyManager.remove(id);
                epic.removeSubTask(id);
                prioritySet.remove(subTasks.get(id));
            }
            epics.get(j).setStatus(TaskStatus.NEW);
        }
        subTasks.clear();  // удалить подзадачи
    }

    @Override
    public TaskStatus getStatusSubTask(int id) {  //  получить статус подзадачи
        if (!subTasks.containsKey(id))
            return null;
        SubTask subTask = subTasks.get(id);
        return subTask.getStatus();
    }

    @Override
    public void changeSubTask(int id, SubTask task, int idEpic) {  // поменять подзадачу по id
        if (!subTasks.containsKey(id) || task == null ||
                !epics.containsKey(idEpic) || isTaskIntersects(task))
            return;

        // удалить связь между эпиком и старой подзадачей
        SubTask oldSubTask = subTasks.get(id);
        int oldEpicId = oldSubTask.getIdEpic();
        Epic oldEpic = epics.get(oldEpicId);
        oldEpic.removeSubTask(id);
        updateEpicStatus(oldEpicId);

        // удалить старую подзадачу из истории
        historyManager.remove(id);

        // удалить старую подзадачу из prioritySet
        prioritySet.remove(oldSubTask);

        // сделать замену
        subTasks.replace(id, task);
        task.setId(id);

        // добавить связь между эпиком и новой подзадачей
        task.setIdEpic(idEpic);
        Epic epic = epics.get(idEpic);
        epic.addSubTask(task);
        updateEpicStatus(idEpic);

        // добавить новую подзадачу в prioritySet
        prioritySet.add(task);
    }

    @Override
    public List<Integer> findSubtasksOfIdEpic(int epicId) { //  найти массив идентификаторов подзадач
        if (!epics.containsKey(epicId))
            return null;

        Epic epic = epics.get(epicId);
        return epic.getIdSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritySet);
    }
    private int calculateId() {  // установить идентификатор
        id++;
        return id;
    }

    private void updateEpicStatus(int idEpic) {    // смена статус
        Epic epic = epics.get(idEpic);
        Set<TaskStatus> statuses = new HashSet<>();
        List<Integer> ids = epic.getIdSubtasks();
        for (int id : ids) {
            statuses.add(getStatusSubTask(id));
        }
        if ((statuses.contains(TaskStatus.NEW) & !statuses.contains(TaskStatus.IN_PROGRESS) &
                !statuses.contains(TaskStatus.DONE)) |
                statuses.isEmpty()) {
            TaskStatus status = TaskStatus.NEW;
            epic.setStatus(status);
        } else if (statuses.contains(TaskStatus.DONE) & !statuses.contains(TaskStatus.IN_PROGRESS) &
                !statuses.contains(TaskStatus.NEW)) {
            TaskStatus status = TaskStatus.DONE;
            epic.setStatus(status);
        } else {
            TaskStatus status = TaskStatus.IN_PROGRESS;
            epic.setStatus(status);
        }
    }


    private boolean isTasksIntersects(Task task1, Task task2) {
        LocalDateTime task1StartTime = task1.getStartTime();
        LocalDateTime task1EndTime = task1.getEndTime();
        LocalDateTime task2StartTime = task2.getStartTime();
        LocalDateTime task2EndTime = task2.getEndTime();

        return task1StartTime != null && task2StartTime != null &&
                task1StartTime.isBefore(task2EndTime) && task2StartTime.isBefore(task1EndTime);
    }

    private boolean isTaskIntersects(Task task) {
        for (Task taskInManager : tasks.values()) {
            if (isTasksIntersects(task, taskInManager))
                return true;
        }

        for (SubTask taskInManager : subTasks.values()) {
            if (isTasksIntersects(task, taskInManager))
                return true;
        }

        return false;
    }
}
