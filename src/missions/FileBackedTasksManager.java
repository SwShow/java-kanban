package missions;

import challenges.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static challenges.TaskStatus.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String fileName;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String nullDateString = "";

    public FileBackedTasksManager(File fileName) {
      this.fileName = fileName.getAbsolutePath();

    }
    public void load() {
        List<String> fromFile = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader buffer = new BufferedReader(fileReader)) {
            while (buffer.ready()) {
                fromFile.add(buffer.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }

        if (!fromFile.isEmpty()) {
            int size = fromFile.size();
            // восстанавливаем таски, эпики и сабтаски
            for (int i = 1; i < (size - 2); i++) {
                String value = fromFile.get(i);
                Task task = taskFromString(value); // распарсили строку в задание
                switch (task.getType()) { // в зависимости от типа задания добавили в нужную таблицу
                    case TASK -> tasks.put(task.getId(), task);
                    case EPIC -> {
                        Epic epic = (Epic) task;
                        epics.put(epic.getId(), epic);
                    }
                    case SUBTASK -> {
                        SubTask subTask = (SubTask) task;
                        subTasks.put(subTask.getId(), subTask);
                    }
                }

                id++; // обновили максимальное значение id
            }
            // восстанавливаем связь между epic и subtask
            for (SubTask subTask: subTasks.values()) {
                Epic epic = epics.get(subTask.getIdEpic());
                epic.addSubTask(subTask);
            }
            // восстанавливаем prioritySet
            prioritySet.addAll(subTasks.values());
            prioritySet.addAll(tasks.values());
            // восстанавливаем историю
            List<Integer> historyIds = fromString(fromFile.get(size - 1));
            for (int id: historyIds) {
                Task task;
                if (tasks.containsKey(id)) {
                    task = tasks.get(id);
                }
                else if (subTasks.containsKey(id)) {
                    task = subTasks.get(id);
                }
                else {
                    task = epics.get(id);
                }
                historyManager.addHistory(task);
            }

        }
    }


    static Task taskFromString(String value) {
        String[] line = value.split(",");

        int id = Integer.parseInt(line[0]);
        TypeTask type = TypeTask.valueOf(line[1]);
        String name = line[2];
        TaskStatus status = TaskStatus.valueOf(line[3]);
        String description = line[4];
        LocalDateTime startTime = null;
        if (!line[5].equals(nullDateString))
            startTime = LocalDateTime.parse(line[5], formatter);
        long duration =  Long.parseLong(line[6]);

        Task task = null;
        switch(type) {
            case TASK:
                task = new Task(name, description, status, startTime, duration);
                break;
            case EPIC:
                task = new Epic(name, description, status);
                break;
            case SUBTASK:
                SubTask subTask = new SubTask(name, description, status, startTime, duration);
                int epicId = Integer.parseInt(line[7]);
                subTask.setIdEpic(epicId);
                task = subTask;
                break;
        }
        task.setId(id);

        return task;
    }


    static String toString(HistoryManager manager) {  // 3. сохранение менеджера в csv
        if (manager == null)
            return null;
        List<String> history = new ArrayList<>();
        List<Task> list = manager.getHistory();
        for (Task task : list) {
            int id = task.getId();
            history.add(String.valueOf(id));
        }
        return String.join(",", history);
    }

    static List<Integer> fromString(String value) {  // 4. восстановление менеджера
        List<Integer> id = new ArrayList<>();
        if (!value.isEmpty()) {
            String[] line = value.split(",");
            for (String str : line) {
                int j = Integer.parseInt(str);
                id.add(j);
            }
        }
        return id;
    }

    static String toString(Task task) {  // 1. сохранение задачи в строку
        // обрабатываем случай null startDate
        String stringStartDate = nullDateString;
        if (task.getStartTime() != null)
            stringStartDate = task.getStartTime().format(formatter);

        // сохраняем данные в строку
        String line = String.join(",",
                String.valueOf(task.getId()),
                task.getType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                stringStartDate,
                String.valueOf(task.getDuration())
        );

        //  случае сабтаска добавляем id эпика
        if (task.getType().equals(TypeTask.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            line += "," + subTask.getIdEpic();
        }

        return line;
    }

    public void save(){
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("id,type,name,status,description,startDate,duration,epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic task : getEpics()) {
                writer.write(toString(task) + "\n");
            }
            for (SubTask task : getSubTasks()) {
                writer.write(toString(task) + "\n");
            }
            writer.write("\n");
            String history = toString(historyManager);
            writer.write(history);
            writer.write("\n");

            writer.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка записи файла.");
        }
    }


    @Override
    public void createEpic(Epic epic)  {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createTask(Task task)  {
        super.createTask(task);
        save();
    }

    @Override
    public void addSubTask(int idEpic, SubTask task) {
        super.addSubTask(idEpic, task);
        save();
    }

    @Override
    public void updateTask(int idTask, Task task)  {
        super.updateTask(idTask, task);
        save();
    }

    @Override
    public void updateEpic(int idEpic, Epic epic)  {
        super.updateEpic(idEpic, epic);
        save();
    }

    @Override
    public void removeTask(int idTask)  {
        super.removeTask(idTask);
        save();
    }

    @Override
    public void removeEpic(int idEpic)  {
        super.removeEpic(idEpic);
        save();
    }

    @Override
    public Task findTaskById(int id) {
        Task task = super.findTaskById(id);
        save();
        return task;
    }

    @Override
    public SubTask findSubTaskById(int id) {
        SubTask subTask = super.findSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public Epic findEpicById(int id) {
        Epic epic = super.findEpicById(id);
        save();
        return epic;
    }

    public static void main(String[] args) {

        File fileName = new File("resources/history.csv");

        LocalDateTime startTime = LocalDateTime.of(2022, 7, 14, 0, 0);
        long duration = 2000;

        // создаем менеджер
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileName);
        // создаем задачу 1
        Task data = new Task("посмотреть фильм", "интересный", NEW,
                startTime, duration);
        // сохраняем задачу 1
        fileBackedTasksManager.createTask(data);
         startTime = LocalDateTime.of(2022, 9, 14, 0, 0);

        // создаем задачу 2
        Task dinner = new Task("сходить в кафе", "изучить меню", NEW,
                startTime, duration);
        // сохраняем задачу 2
        fileBackedTasksManager.createTask(dinner);
        // выполняем поиск задачи 2
        fileBackedTasksManager.findTaskById(dinner.getId());
        // выполняем поиск задачи 1
        fileBackedTasksManager.findTaskById(data.getId());
        // восстанавливаем менеджер из файла
        FileBackedTasksManager restored =  new FileBackedTasksManager(fileName);
        restored.load();

        // выводим исходный
        System.out.println("Исходный менеджер");
        printManagerInfo(fileBackedTasksManager);
        System.out.println();

        // выводим загруженный
        System.out.println("Загруженный");
        printManagerInfo(restored);
        System.out.println();
        startTime = LocalDateTime.of(2022, 10, 14, 0, 0);
        // добавляем эпик и сабтаски
        Epic shopping = new Epic("сходить в магазин", "Ашан", NEW);
        SubTask Shop1 = new SubTask("купить мыло", "душистое", NEW,
                startTime, duration);
        SubTask Shop2 = new SubTask( "купить шампунь", "для нормальных волос", DONE,
                startTime, duration);
        fileBackedTasksManager.createEpic(shopping);
        fileBackedTasksManager.addSubTask(shopping.getId(), Shop1);
        fileBackedTasksManager.findSubTaskById(Shop1.getId());
        fileBackedTasksManager.addSubTask(shopping.getId(), Shop2);
        fileBackedTasksManager.findEpicById(shopping.getId());
        fileBackedTasksManager.findSubTaskById(Shop2.getId());

        // восстанавливаем менеджер из файла
        FileBackedTasksManager restored2 =  new FileBackedTasksManager(fileName);
        restored.load();

        // выводим исходный
        System.out.println("Исходный менеджер");
        printManagerInfo(fileBackedTasksManager);
        System.out.println();

        // выводим загруженный
        System.out.println("Загруженный");
        printManagerInfo(restored2);
        System.out.println();
    }

    private static void printManagerInfo(TaskManager taskManager) {
        System.out.println("id,type,name,status,description,startDate,duration,epic");
        for (Task task : taskManager.getTasks())
            System.out.println(toString(task));
        for (Epic task : taskManager.getEpics())
            System.out.println(toString(task));
        for (SubTask task : taskManager.getSubTasks())
            System.out.println(toString(task));
        List<String> historyIds = new ArrayList<>();
        for (Task task: taskManager.getHistory()) {
            int id = task.getId();
            historyIds.add(String.valueOf(id));
        }
        System.out.println();
        System.out.println(String.join(",", historyIds));
    }

}