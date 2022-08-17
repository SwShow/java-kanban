package mission;

import challenges.Epic;
import challenges.SubTask;
import challenges.Task;
import missions.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static challenges.TaskStatus.DONE;
import static challenges.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    protected File storageFile = new File("resources/history.csv");

    protected FileBackedTasksManager getInstance() {
        return new FileBackedTasksManager(storageFile);
    }

    @Test
    void emptyRestore() {
        taskManager.save();
        FileBackedTasksManager loadedManager = new FileBackedTasksManager(storageFile);
        loadedManager.load();

        assertTrue(loadedManager.getTasks().isEmpty(), "Список задач должен быть пуст");
        assertTrue(loadedManager.getEpics().isEmpty(), "Список эпиков должен быть пуст");
        assertTrue(loadedManager.getSubTasks().isEmpty(), "Список сабтасков должен быть пуст");
        assertTrue(loadedManager.getHistory().isEmpty(), "История должена быть пуста");
        assertTrue(loadedManager.getPrioritizedTasks().isEmpty(), "Список приоритетов должен быть пуст");
    }

    @Test
    void epicWithoutSubtasksRestore() {
        taskManager.createEpic(epic);
        FileBackedTasksManager loadedManager = new FileBackedTasksManager(storageFile);
        loadedManager.load();
        Epic loadedEpic = loadedManager.findEpicById(epic.getId());

        assertEquals(epic, loadedEpic, "Загруженный эпик не равен начальному");
        assertTrue(loadedManager.getSubTasks().isEmpty(), "Список сабтасков должен быть пуст");
        assertTrue(loadedManager.getPrioritizedTasks().isEmpty(), "Список приоритетов должен быть пуст");
    }

    @Test
    void restore() {
        // заполняем трекер данными
        Task data = new Task("посмотреть фильм", "интересный", NEW,
                startTime, duration);
        taskManager.createTask(data);
        Task dinner = new Task("сходить в кафе", "изучить меню", NEW,
                startTime, duration);
        taskManager.createTask(dinner);
        taskManager.findTaskById(dinner.getId());
        taskManager.findTaskById(data.getId());
        Epic shopping = new Epic("сходить в магазин", "Ашан", NEW);
        SubTask Shop1 = new SubTask("купить мыло", "душистое", NEW);
        SubTask Shop2 = new SubTask( "купить шампунь", "для нормальных волос", DONE);
        taskManager.createEpic(shopping);
        taskManager.addSubTask(shopping.getId(), Shop1);
        taskManager.addSubTask(shopping.getId(), Shop2);
        taskManager.findEpicById(shopping.getId());
        taskManager.findSubTaskById(Shop1.getId());
        taskManager.findSubTaskById(Shop2.getId());

        // восстанавливаем
        FileBackedTasksManager loadedManager = new FileBackedTasksManager(storageFile);
        loadedManager.load();

        // проверяем, что данные сохранились
        List<Task> tasks = taskManager.getTasks();
        List<Task> loadedTasks = loadedManager.getTasks();
        assertTrue(testListEquality(tasks, loadedTasks), "Списки задач не совпадают");

        List<SubTask> subTasks = taskManager.getSubTasks();
        List<SubTask> loadedSubtasks = loadedManager.getSubTasks();
        assertTrue(testListEquality(subTasks, loadedSubtasks), "Списки подзадач не совпадают");

        List<Epic> epics = taskManager.getEpics();
        List<Epic> loadedEpics = loadedManager.getEpics();
        assertTrue(testListEquality(epics, loadedEpics), "Списки эпиков не совпадают");

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        List<Task> loadedPrioritizedTasks = loadedManager.getPrioritizedTasks();
        assertEquals(prioritizedTasks, loadedPrioritizedTasks);

        List<Task> history = taskManager.getHistory();
        List<Task> loadedHistory = loadedManager.getHistory();
        assertEquals(history.size(), loadedHistory.size(), "Размер историй не совпадает");
        for (int i = 0; i < history.size(); i++) {
            assertEquals(history.get(i), loadedHistory.get(i), "История не совпадает");
        }
    }

    private <T> boolean testListEquality(List<T> list1, List<T> list2) {
        return (list1.size() == list2.size()) &&
                list1.containsAll(list2) &&
                list2.containsAll(list1);
    }
}
