package missions;

import challenges.Epic;
import challenges.MyEnum;
import challenges.SubTask;
import challenges.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int id = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>(); //  мапа сабтасков


    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getSubTasks() {
        List<SubTask> subTask = new ArrayList<>();
        if (!subTasks.isEmpty())
            for (Integer key : subTasks.keySet()) {
                SubTask subTask1 = subTasks.get(key);
                subTask.add(subTask1);
            }
        return subTask;
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {  // создать задачу
        int idTask = calculateId();
        task.setId(idTask);
        tasks.put(idTask, task);  //  положить в мапу задач
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
        }
        tasks.clear();
    }

    @Override
    public void updateTask(int id, Task task) throws ManagerSaveException {  // заменить задачу
        historyManager.remove(id);
        task.setId(id);
        tasks.replace(id, task);
        historyManager.addHistory(task);
    }   //  обновление по идентификатору

    @Override
    public void removeTask(int id) throws ManagerSaveException {  // удалить задачу
        historyManager.remove(id);
        tasks.remove(id);
    }   //  удаление по идентификатору


    @Override
    public void createEpic(Epic name) throws ManagerSaveException {  // создать эпик
        int idEpic = calculateId();
        name.setIdEpic(idEpic);
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
    public void updateEpic(int id, Epic task) throws ManagerSaveException {   // обновление эпика по идентификатору
        List<Integer> idSub = getIdSubTask(id); //  получить список идентификаторов подзадач
        for (int i : idSub) {
            subTasks.remove(i);
        }
        idSub.clear();  //  очистить список идентификаторов подзадач
        historyManager.remove(id);
        epics.replace(id, task);  //  заменить эпик
        task.setIdEpic(id);
    }

    @Override
    public void removeEpics() throws ManagerSaveException {  // удаление всех эпиков
        ArrayList<Integer> idEp = new ArrayList<>(epics.keySet()); // найти идентификаторы эпиков
        for (int id : idEp) {  // для каждого идентификатора эпика
            removeEpic(id); // вызвать метод удаления эпика вместе с подзадачами
        }
    }


    @Override
    public void removeEpic(int idEpic) throws ManagerSaveException {// удалить эпик по идентификатору
        List<Integer> ids = getIdSubTask(idEpic);// найти подзадачи
        for (int id : ids) {
            subTasks.remove(id);
        }
        historyManager.remove(idEpic);
        epics.remove(idEpic);
    }

    @Override
    public void addSubTask(int idEpic, SubTask task) throws ManagerSaveException {  // добавить подзадачу
        id = calculateId();
        task.setIdEpic(idEpic);//  записали в поле сабтаска айди эпика
        task.setIdSubTask(id);
        subTasks.put(id, task);//  положили сабтаск в мапу со своим айди
        List<Integer> ids = getIdSubTask(idEpic);
        ids.add(id); // положить айди сабтаска в лист эпика
        findOllStatusSubTask(idEpic);       // вызвать метод смены статуса
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
    public void removeSubTask() {  //удаление всех подзадач
        for (int j : epics.keySet()) {   // для каждого эпика
            List<Integer> founds = getIdSubTask(j); // найти массив идентификаторов подзадач
            for (int id : founds) {
                historyManager.remove(id);
            }
            founds.clear(); // удалить массив
        }
        subTasks.clear();  // удалить подзадачи
    }

    @Override
    public MyEnum getStatusSubTask(int id) {  //  получить статус подзадачи
        SubTask subTask = subTasks.get(id);
        return subTask.getStatus();
    }

    @Override
    public void changeSubTask(int id, SubTask task, int idEpic) {  // поменять подзадачу по id
        subTasks.replace(id, task);
        task.setIdSubTask(id);
        task.setIdEpic(idEpic);
        findOllStatusSubTask(idEpic);  // поменять статус
    }


    public int getIdOllTasks(Task task) {
        int id = 0;
        if (task instanceof Epic) {
            id = getIdEpic((Epic) task);
        } else if (task instanceof SubTask) {
            id = getIdSubTask((SubTask) task);
        } else {
            id = task.getId();
        }
        return id;
    }

    @Override
    public List<Integer> getIdSubTask(int id) { //  найти массив идентификаторов подзадач
        Epic epic = findEpicById(id);
        return epic.getIdSubTasks();
    }


    public void findSubTasksOfIdEpic(int idEpic) {  // найти подзадачи по id эпика
        ArrayList<SubTask> foundSubTasks = new ArrayList<>();
        List<Integer> ids = getIdSubTask(idEpic);  //  лист идентификаторов подзадач
        for (int id : ids) {
            SubTask founds = subTasks.get(id);
            foundSubTasks.add(founds);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int getIdSubTask(SubTask task) {
        return task.getIdSubTask();
    }

    @Override
    public int getIdEpic(Epic task) {
        return task.getIdEpic();
    }


    private int calculateId() {  // установить идентификатор
        id++;
        return id;
    }

    private void findOllStatusSubTask(int idEpic) {    // смена статус
        Epic epic = epics.get(idEpic);
        ArrayList<MyEnum> statuses = new ArrayList<>();
        List<Integer> ids = getIdSubTask(idEpic);
        for (int id : ids) {
            statuses.add(getStatusSubTask(id));
        }
        if ((statuses.contains(MyEnum.NEW) & !statuses.contains(MyEnum.IN_PROGRESS) & !statuses.contains(MyEnum.DONE)) |
                statuses.isEmpty()) {
            MyEnum status = MyEnum.NEW;
            epic.setStatus(status);
        } else if (statuses.contains(MyEnum.DONE) & !statuses.contains(MyEnum.IN_PROGRESS) & !statuses.contains(MyEnum.NEW)) {
            MyEnum status = MyEnum.DONE;
            epic.setStatus(status);
        } else {
            MyEnum status = MyEnum.IN_PROGRESS;
            epic.setStatus(status);
        }
    }

}
