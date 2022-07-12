package missions;

import challenges.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static challenges.MyEnum.*;
import static challenges.TypeTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String fileName;

    public FileBackedTasksManager(File fileName) {
        this.fileName = fileName.getAbsolutePath();
        if (!Files.exists(Paths.get(fileName.toURI()))) {
            try {
                Files.createFile(Paths.get(fileName.toURI()));
            } catch (IOException exception) {
                throw new ManagerSaveException("Ошибка при попытке создания файла");
            }
        }
    }

    public static FileBackedTasksManager loadFromFile(File fileName) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileName);
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
                    case TASK:
                        fileBackedTasksManager.tasks.put(task.getId(), task);
                        break;
                    case EPIC:
                        Epic epic = (Epic) task;
                        fileBackedTasksManager.epics.put(epic.getIdEpic(), epic);
                        break;
                    case SUBTASK:
                        SubTask subTask = (SubTask) task;
                        fileBackedTasksManager.subTasks.put(subTask.getIdSubTask(), subTask);
                        break;
                }

                fileBackedTasksManager.id++; // обновили максимальное значение id
            }
            // восстанавливаем связь между epic и subtask
            for (SubTask subTask: fileBackedTasksManager.subTasks.values()) {
                Epic epic = fileBackedTasksManager.epics.get(subTask.getIdEpic());
                epic.getIdSubTasks().add(subTask.getIdSubTask());
            }
            // восстанавливаем историю
            List<Integer> historyIds = fromString(fromFile.get(size - 1));
            for (int id: historyIds) {
                Task task;
                if (fileBackedTasksManager.tasks.containsKey(id)) {
                    task = fileBackedTasksManager.tasks.get(id);
                }
                else if (fileBackedTasksManager.subTasks.containsKey(id)) {
                    task = fileBackedTasksManager.subTasks.get(id);
                }
                else {
                    task = fileBackedTasksManager.epics.get(id);
                }
                fileBackedTasksManager.historyManager.addHistory(task);
            }

        }
        return fileBackedTasksManager;
    }


    static Task taskFromString(String value) {
        String[] line = value.split(",");

        int id = Integer.parseInt(line[0]);
        TypeTask type = TypeTask.valueOf(line[1]);
        String name = line[2];
        MyEnum status = MyEnum.valueOf(line[3]);
        String description = line[4];

        Task task = null;
        switch(type) {
            case TASK:
                task = new Task(name, description, status);
                break;
            case EPIC:
                Epic epicTask = new Epic(name, description, status);
                epicTask.setIdEpic(id);
                task = epicTask;
                break;
            case SUBTASK:
                SubTask subTask = new SubTask(name, description, status);
                int epicId = Integer.parseInt(line[5]);
                subTask.setIdEpic(epicId);
                subTask.setIdSubTask(id);
                task = subTask;
                break;
        }
        task.setId(id);

        return task;
    }


    static String toString(HistoryManager manager) {  // 3. сохранение менеджера в csv
        List<String> history = new ArrayList<>();
        if (manager == null) {
            System.out.println("История просмотров пустая");
        }
        assert manager != null;
        List<Task> list = manager.getHistory();
        for (Task task : list) {
            int id = 0;
            switch (task.getType()) {
                case EPIC:
                    id = ((Epic) task).getIdEpic();
                    break;
                case TASK:
                    id = task.getId();
                    break;
                case SUBTASK:
                    id = ((SubTask) task).getIdSubTask();
                    break;
            }
            history.add(String.valueOf(id));
        }
        return String.join(",", history);
    }

    static List<Integer> fromString(String value) {  // 4. восстановление менеджера
        List<Integer> id = new ArrayList<>();
        if (value.isEmpty()) {
            System.out.println("Передана пустая строка");
        } else {
            String[] line = value.split(",");
            for (String str : line) {
                int j = Integer.parseInt(str);
                id.add(j);
            }
        }
        return id;
    }

    static String toString(Task task) {  // 1. сохранение задачи в строку
        String line = "";
        switch (task.getType()) {
            case TASK:
                line = String.join(",",
                        Integer.toString(task.getId()),
                        task.getType().toString(),
                        task.getName(),
                        task.getStatus().toString(),
                        task.getDescription());
                break;
            case EPIC:
                Epic epic = (Epic) task;
                line = String.join(",",
                        Integer.toString(epic.getIdEpic()),
                        epic.getType().toString(),
                        epic.getName(),
                        epic.getStatus().toString(),
                        epic.getDescription());
                break;
            case SUBTASK:
                SubTask subtask = (SubTask) task;
                line = String.join(",",
                        Integer.toString(subtask.getIdSubTask()),
                        subtask.getType().toString(),
                        subtask.getName(),
                        subtask.getStatus().toString(),
                        subtask.getDescription(),
                        Integer.toString(subtask.getIdEpic()));
                break;
            default:
                System.out.println("Не определен тип задачи");
        }
        return line;
    }

    public void save()  {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("id,type,name,status,description,epic" + "\n");
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
            String history =  toString(historyManager);
            writer.write(history);

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

        // создаем менеджер
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileName);
        // создаем задачу 1
        Task data = new Task("посмотреть фильм", "интересный", NEW);
        // сохраняем задачу 1
        fileBackedTasksManager.createTask(data);
        // создаем задачу 2
        Task dinner = new Task("сходить в кафе", "изучить меню", NEW);
        // сохраняем задачу 2
        fileBackedTasksManager.createTask(dinner);
        // выполняем поиск задачи 2
        Task task2 = fileBackedTasksManager.findTaskById(dinner.getId());
        // выполняем поиск задачи 1
        Task task1 = fileBackedTasksManager.findTaskById(data.getId());
        // восстанавливаем менеджер из файла
        FileBackedTasksManager restored = FileBackedTasksManager.loadFromFile(fileName);

        // выводим исходный
        System.out.println("Исходный менеджер");
        printManagerInfo(fileBackedTasksManager);
        System.out.println();

        // выводим загруженный
        System.out.println("Загруженный");
        printManagerInfo(restored);
        System.out.println();

        // добавляем эпик и сабтаски
        Epic shopping = new Epic("сходить в магазин", "Ашан", NEW);
        SubTask Shop1 = new SubTask("купить мыло", "душистое", NEW);
        SubTask Shop2 = new SubTask( "купить шампунь", "для нормальных волос", DONE);
        fileBackedTasksManager.createEpic(shopping);
        fileBackedTasksManager.addSubTask(shopping.getIdEpic(), Shop1);
        fileBackedTasksManager.addSubTask(shopping.getIdEpic(), Shop2);
        Epic epic1 = fileBackedTasksManager.findEpicById(shopping.getIdEpic());
        SubTask subtask1 = fileBackedTasksManager.findSubTaskById(Shop1.getIdSubTask());
        SubTask subtask2 = fileBackedTasksManager.findSubTaskById(Shop2.getIdSubTask());

        // восстанавливаем менеджер из файла
        FileBackedTasksManager restored2 = FileBackedTasksManager.loadFromFile(fileName);

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
        System.out.println("id,type,name,status,description,epic");
        for (Task task : taskManager.getTasks())
            System.out.println(task);
        for (Epic task : taskManager.getEpics())
            System.out.println(task);
        for (SubTask task : taskManager.getSubTasks())
            System.out.println(task);
        List<String> historyIds = new ArrayList<>();
        for (Task task: taskManager.getHistory()) {
            int id = 0;
            switch (task.getType()) {
                case EPIC:
                    id = ((Epic) task).getIdEpic();
                    break;
                case TASK:
                    id = task.getId();
                    break;
                case SUBTASK:
                    id = ((SubTask) task).getIdSubTask();
                    break;
            }
            historyIds.add(String.valueOf(id));
        }
        System.out.println();
        System.out.println(String.join(",", historyIds));
    }

}