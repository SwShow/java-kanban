package Missions;

import Challenges.Epic;
import Challenges.MyEnum;
import Challenges.SubTask;
import Challenges.Task;

import java.util.ArrayList;
import java.util.HashMap;

class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>(); //  мапа сабтасков

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());

    }

    private int calculateId() {  // установить идентификатор
        id++;
        return id;
    }

    @Override
    public void createTask(Task task) {  // создать задачу
        int idTask = calculateId();
        task.setIdTask(idTask);
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

    @Override
    public void createEpic(Epic name) {  // создать эпик
        int idEpic = calculateId();
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
        ArrayList<Integer> idSub = getIdSubTask(id); //  получить список идентификаторов подзадач
        for (int i : idSub) {
            subTasks.remove(i);
        }
        idSub.clear();  //  очистить список идентификаторов подзадач
        epics.replace(id, task);  //  заменить эпик
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
        ArrayList<Integer> ids = getIdSubTask(idEpic);// найти подзадачи
        for (int id : ids) {
            subTasks.remove(id);
        }
        epics.remove(idEpic);
    }

    @Override
    public void addSubTask(int idEpic, SubTask task) {  // добавить подзадачу
        id = calculateId();
        task.setIdEpic(idEpic);  //  записали в поле сабтаска айди эпика
        subTasks.put(id, task);//  положили сабтаск в мапу со своим айди
        ArrayList<Integer> ids = getIdSubTask(idEpic);
        ids.add(id); // положить айди сабтаска в лист эпика
        findOllStatusSubTask(idEpic);       // вызвать метод смены статуса
    }


    @Override
    public SubTask findValOfId(int id) {  // найти подзадачу по идентификатору
        SubTask sub = subTasks.get(id);
        if (sub != null) {
            historyManager.addHistory(sub);
        }
        return sub;
    }

    @Override
    public void removeSubTask() {  //удаление всех подзадач
        for (int j : epics.keySet()) {   // для каждого эпика
            ArrayList<Integer> founds = getIdSubTask(j); // найти массив идентификаторов подзадач
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
        task.setIdEpic(idEpic);
        findOllStatusSubTask(idEpic);  // поменять статус
    }

    @Override
    public ArrayList<Integer> getIdSubTask(int id) { //  найти массив идентификаторов подзадач
        Epic epic = findEpicById(id);
        return epic.getIdSubTasks();
    }


    public void findSubTasksOfIdEpic(int idEpic) {  // найти подзадачи по id эпика
        ArrayList<SubTask> foundSubTasks = new ArrayList<>();
        ArrayList<Integer> ids = getIdSubTask(idEpic);  //  лист идентификаторов подзадач
        for (int id : ids) {
            SubTask founds = subTasks.get(id);
            foundSubTasks.add(founds);
        }
    }


    private void findOllStatusSubTask(int idEpic) {    // смена статус
        Epic epic = epics.get(idEpic);
        ArrayList<MyEnum> statuses = new ArrayList<>();
        ArrayList<Integer> ids = getIdSubTask(idEpic);
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