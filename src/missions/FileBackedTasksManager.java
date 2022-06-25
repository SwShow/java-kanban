package missions;
import challenges.Epic;
import challenges.SubTask;
import challenges.Task;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final String fileName;
    public FileBackedTasksManager(String filename) {
        this.fileName = filename;
        if (Files.exists(Paths.get(fileName))) {
            try {
                Files.delete(Paths.get(fileName));
            } catch (IOException exception) {
                System.out.println("Ошибка при попытке удаления существующего файла для пересоздания");
                exception.printStackTrace();
            }
        } else {
            try {
                Files.createFile(Paths.get(fileName));
            } catch (IOException exception) {
                System.out.println("Ошибка при попытке создания файла");
                exception.printStackTrace();
            }
        }
    }
    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }
    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }
    @Override
    public List<SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    public String taskToString(Task task) {
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
    static String historyToString() throws IOException {
        List<Task> history = new ArrayList<>(InMemoryTaskManager.historyManager.getHistory());
        StringBuilder line = new StringBuilder();
        if (history.isEmpty()) {
            System.out.println("История просмотров пустая");
            return line.toString();
        }
        for (int i = 0; i < history.size(); i++) {
            line.append((history.get(i)) + ",");
        }
        return line.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (value.isEmpty()) {
            System.out.println("Передана пустая строка");
            return Collections.emptyList();
        }
        String[] line = value.split(",");
        for (String str : line) {
            history.add(Integer.parseInt(str));
        }
        return history;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    @Override
    public String toString() {
        return super.toString();
    }
    @Override
    public List<Task> getHistory() throws IOException {
        return super.getHistory();
    }

    @Override
    public void save() {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("id,type,name,status,description,epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(taskToString(task) + "\n");
            }
            for (Epic task : getEpics()) {
                writer.write(taskToString(task) + "\n");
            }
            for (SubTask task : getSubTasks()) {
                writer.write(taskToString(task) + "\n");
            }
            writer.write("\n");
            writer.write(historyToString());
            writer.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }
    @Override
    public void addSubTask(int idEpic, SubTask task) throws IOException {
        super.addSubTask(idEpic, task);
        save();
    }
    @Override
    public void updateTask(int idTask, Task task) throws IOException {
        super.updateTask(idTask, task);
        save();
    }
    @Override
    public void updateEpic(int idEpic, Epic epic) throws IOException {
        super.updateEpic(idEpic, epic);
        save();
    }

    @Override
    public void removeTask(int idTask) throws IOException {
        super.removeTask(idTask);
        save();
    }

    @Override
    public void removeEpic(int idEpic) throws IOException {
        super.removeEpic(idEpic);
        save();
    }


}