package missions;

import challenges.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static challenges.MyEnum.*;
import static challenges.TypeTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final List<Object> history = new ArrayList<>();
    public String fileName = "resources/history.csv";

    public FileBackedTasksManager(File fileName) {
        if (Files.exists(Paths.get(String.valueOf(fileName)))) {
        } else {
            try {
                Files.createFile(Paths.get(String.valueOf(fileName)));
            } catch (IOException exception) {
                System.out.println("Ошибка при попытке создания файла");
                exception.printStackTrace();
            }
        }
    }

    public static FileBackedTasksManager loadFromFile(File fileName) throws ManagerSaveException {
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
        if (fromFile.isEmpty()) {
            System.out.println("История пуста");
        } else {
            int size = fromFile.size();
            for (int i = 1; i < (size - 2); i++) {
                String value = fromFile.get(i);
                Task task = fileBackedTasksManager.taskFromString(value);
                history.add(task);
            }
            List<Integer> id = fromString(fromFile.get(size - 1));
            history.add(id);
        }
        return fileBackedTasksManager;
    }


    public Task taskFromString(String value) {
        String[] line = value.split(",");
        TypeTask type = TypeTask.valueOf(line[1]);
        MyEnum stat = MyEnum.valueOf(line[3]);
        Task task = new Task(type, line[2], line[4], stat);
        return task;
    }


    public static void toString(List<Object> history) {
        if (history.isEmpty()) {
            System.out.println("История просмотров пустая");
        }
        for (Object val : history) {
            System.out.println(val);
        }

    }

    static List<Integer> fromString(String value) {
        List<Integer> id = new ArrayList<>();
        if (value.isEmpty()) {
            System.out.println("Передана пустая строка");
            return Collections.emptyList();
        } else {
            String[] line = value.split(",");
            for (String str : line) {
                int j = Integer.parseInt(str);
                id.add(j);
            }
        }
        return id;
    }

    public String toString(Task task) {
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
                        subtask.getDescription());
                break;
            default:
                System.out.println("Не определен тип задачи");
        }
        return line;
    }

    public void save() throws ManagerSaveException {
        try {
            FileWriter writer = new FileWriter(fileName);
            List<Integer> id = new ArrayList<>();
            writer.write("id,type,name,status,description,epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(toString(task) + "\n");
                id.add(getIdOllTasks(task));
            }
            for (Epic task : getEpics()) {
                writer.write(toString(task) + "\n");
                id.add(getIdOllTasks(task));
            }
            for (SubTask task : getSubTasks()) {
                writer.write(toString(task) + "\n");
                id.add(getIdOllTasks(task));
            }
            writer.write("\n");
            for (int j : id) {
                writer.write(j + ",");
            }
            writer.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка записи файла.");

        }
    }


    @Override
    public void createEpic(Epic epic) throws ManagerSaveException {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        super.createTask(task);
        save();
    }

    @Override
    public void addSubTask(int idEpic, SubTask task) throws ManagerSaveException {
        super.addSubTask(idEpic, task);
        save();
    }

    @Override
    public void updateTask(int idTask, Task task) throws ManagerSaveException {
        super.updateTask(idTask, task);
        save();
    }

    @Override
    public void updateEpic(int idEpic, Epic epic) throws ManagerSaveException {
        super.updateEpic(idEpic, epic);
        save();
    }

    @Override
    public void removeTask(int idTask) throws ManagerSaveException {
        super.removeTask(idTask);
        save();
    }

    @Override
    public void removeEpic(int idEpic) throws ManagerSaveException {
        super.removeEpic(idEpic);
        save();
    }


    public static void main(String[] args) throws ManagerSaveException {
        FileBackedTasksManager manager = (FileBackedTasksManager) Managers.getDefault();
        File fileName = new File("resources/history.csv");
        loadFromFile(fileName);
        toString(history);


        Task data = new Task(TASK, "посмотреть фильм", "интересный", NEW);
        manager.createTask(data);
        Task dinner = new Task(TASK, "сходить в кафе", "изучить меню", NEW);
        manager.createTask(dinner);
        Task drink = new Task(TASK, "Coffee", "Cappuccino", NEW);
        manager.createTask(drink);
        Task go = new Task(TASK, "open the door", "close the door", NEW);
        manager.updateTask(2, go);
        Epic shopping = new Epic(EPIC, "сходить в магазин", "Ашан", NEW);
        SubTask Shop1 = new SubTask(SUBTASK, "купить мыло", "душистое", NEW);
        SubTask Shop2 = new SubTask(SUBTASK, "купить шампунь", "для нормальных волос", DONE);
        manager.createEpic(shopping);
        manager.addSubTask(4, Shop1);
        manager.addSubTask(4, Shop2);
        Epic holiday = new Epic(EPIC, "поход в ресторан", "Макдональдс", NEW);
        SubTask hol = new SubTask(SUBTASK, "позвать друзей", "обсудить меню", NEW);
        manager.createEpic(holiday);
        manager.addSubTask(7, hol);
        SubTask holy = new SubTask(SUBTASK, "купить платье", "вечернее", IN_PROGRESS);
        manager.changeSubTask(5, holy, 4);
        manager.findSubTasksOfIdEpic(4);
        Epic programming = new Epic(EPIC, "сесть за стол", "налить кофе", NEW);
        manager.updateEpic(7, programming);
        SubTask St3 = new SubTask(SUBTASK, "wash the car", "on the way", IN_PROGRESS);
        manager.addSubTask(7, St3);

        manager.findTaskById(3);
        manager.findEpicById(4);
        manager.findSubTaskById(6);
        manager.findSubTaskById(5);
        manager.findTaskById(3);
        manager.findSubTaskById(9);
        manager.findTaskById(1);
    }

}