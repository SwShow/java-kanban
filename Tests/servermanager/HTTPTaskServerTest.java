package servermanager;
import challenges.Epic;
import challenges.SubTask;
import challenges.Task;
import challenges.TaskStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import handler.TaskAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HTTPTaskServerTest {
    private HTTPTaskServer taskServer;

    private KVServer kvServer;

    private final HttpClient client = HttpClient.newHttpClient();

    private final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Task.class, new TaskAdapter())
            .create();

    private final String rootPath = "http://localhost:8080/tasks";

    @BeforeEach
    public void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        taskServer = new HTTPTaskServer();
        taskServer.start();
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
        taskServer.stop();
    }

    @Test
    public void findTaskByIdTest() throws IOException, InterruptedException {
        int nonExistentId = 0;
        Task nonExistentTask = findTaskById(nonExistentId);
        assertNull(nonExistentTask);

        LocalDateTime startTime = LocalDateTime.of(1900, 10, 20, 3, 4);
        Task task = new Task("1", "2", TaskStatus.NEW, startTime, 100);
        int id = createTask(task);

        task.setId(id);
        Task foundTask = findTaskById(id);

        assertEquals(task, foundTask);
    }

    @Test
    public void getAllTasksTest() throws IOException, InterruptedException {
        List<Task> emptyList = getAllTasks();
        assertTrue(emptyList.isEmpty());

        List<Task> taskList = new ArrayList<>();
        int size = 10;
        for (int i = 0; i < size; i++) {
            Task task = new Task(String.valueOf(i), String.valueOf(i), TaskStatus.IN_PROGRESS);
            int id = createTask(task);
            task.setId(id);
            taskList.add(task);
        }

        List<Task> taskListResponse = getAllTasks();

        assertTrue(testListEquality(taskList, taskListResponse));
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        Task initialTask = new Task("1", "1", TaskStatus.IN_PROGRESS);
        int id = createTask(initialTask);

        Task toUpdateTask = new Task("2", "2", TaskStatus.DONE);
        updateTask(id, toUpdateTask);
        toUpdateTask.setId(id);

        Task updatedTask = findTaskById(id);

        assertEquals(toUpdateTask, updatedTask);
    }

    @Test
    public void removeTaskTest() throws IOException, InterruptedException {
        Task task = new Task("1", "1", TaskStatus.DONE);
        int id = createTask(task);
        removeTask(id);
        Task removedTask = findTaskById(id);

        assertNull(removedTask);
    }

    @Test
    public void removeAllTasksTest() throws IOException, InterruptedException {
        int size = 10;
        for (int i = 0; i < size; i++) {
            Task task = new Task(String.valueOf(i), String.valueOf(i), TaskStatus.IN_PROGRESS);
            createTask(task);
        }

        removeAllTasks();

        List<Task> allTasks = getAllTasks();

        assertTrue(allTasks.isEmpty());
    }

    @Test
    public void findEpicByIdTest() throws IOException, InterruptedException {
        int nonExistentId = 0;
        Epic nonExistentEpic = findEpicById(nonExistentId);
        assertNull(nonExistentEpic);

        Epic epic = new Epic("1", "2", TaskStatus.NEW);
        int id = createEpic(epic);
        epic.setId(id);

        Epic foundEpic = findEpicById(id);

        assertEquals(epic, foundEpic);
    }

    @Test
    public void getAllEpicsTest() throws IOException, InterruptedException {
        List<Epic> emptyList = getAllEpics();
        assertTrue(emptyList.isEmpty());

        List<Epic> epicList = new ArrayList<>();
        int size = 10;
        for (int i = 0; i < size; i++) {
            Epic epic = new Epic(String.valueOf(i), String.valueOf(i), TaskStatus.NEW);
            int id = createEpic(epic);
            epic.setId(id);
            epicList.add(epic);
        }

        List<Epic> epicListResponse = getAllEpics();

        assertTrue(testListEquality(epicList, epicListResponse));
    }

    @Test
    public void removeEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("1", "1", TaskStatus.DONE);
        int id = createEpic(epic);
        removeEpic(id);
        Epic removedEpic = findEpicById(id);

        assertNull(removedEpic);
    }

    @Test
    public void removeAllEpicsTest() throws IOException, InterruptedException {
        int size = 10;
        for (int i = 0; i < size; i++) {
            Epic epic = new Epic(String.valueOf(i), String.valueOf(i), TaskStatus.NEW);
            createEpic(epic);
        }

        removeAllEpics();

        List<Epic> allEpics = getAllEpics();

        assertTrue(allEpics.isEmpty());
    }

    @Test
    public void findSubTaskByIdTest() throws IOException, InterruptedException {
        int nonExistentId = 0;
        SubTask nonExistentSubTask = findSubTaskById(nonExistentId);
        assertNull(nonExistentSubTask);

        Epic epic = new Epic("1", "2", TaskStatus.NEW);
        int epicId = createEpic(epic);

        SubTask subTask = new SubTask("2", "3", TaskStatus.IN_PROGRESS);
        int subTaskId = createSubTask(subTask, epicId);

        subTask.setId(subTaskId);
        subTask.setIdEpic(epicId);

        SubTask foundSubTask = findSubTaskById(subTaskId);

        assertEquals(subTask, foundSubTask);
    }

    @Test
    public void getAllSubTasksTest() throws IOException, InterruptedException {
        List<SubTask> emptyList = getAllSubTasks();
        assertTrue(emptyList.isEmpty());

        Epic epic = new Epic("1", "1", TaskStatus.IN_PROGRESS);
        int epicId = createEpic(epic);

        List<SubTask> subTasksList = new ArrayList<>();
        int size = 10;
        for (int i = 0; i < size; i++) {
            SubTask subTask = new SubTask(String.valueOf(i), String.valueOf(i), TaskStatus.NEW);
            int id = createSubTask(subTask, epicId);
            subTask.setId(id);
            subTask.setIdEpic(epicId);
            subTasksList.add(subTask);
        }

        List<SubTask> subTaskListResponse = getAllSubTasks();

        assertTrue(testListEquality(subTasksList, subTaskListResponse));
    }

    @Test
    public void changeSubTaskTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("1", "1", TaskStatus.IN_PROGRESS);
        int idEpic1 = createEpic(epic1);

        Epic epic2 = new Epic("2", "2", TaskStatus.IN_PROGRESS);
        int idEpic2 = createEpic(epic2);

        SubTask initialSubTask = new SubTask("1", "1", TaskStatus.IN_PROGRESS);
        int id = createSubTask(initialSubTask, idEpic1);

        SubTask toUpdateSubTask = new SubTask("2", "2", TaskStatus.DONE);
        changeSubTask(id, toUpdateSubTask, idEpic2);
        toUpdateSubTask.setId(id);
        toUpdateSubTask.setIdEpic(idEpic2);

        SubTask updatedSubTask = findSubTaskById(id);

        assertEquals(toUpdateSubTask, updatedSubTask);
    }

    @Test
    public void removeSubTaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("1", "1", TaskStatus.DONE);
        int epicId = createEpic(epic);

        SubTask subTask = new SubTask("2", "2", TaskStatus.IN_PROGRESS);
        int id = createSubTask(subTask, epicId);
        removeSubTask(id);
        SubTask removedSubTask = findSubTaskById(id);

        assertNull(removedSubTask);
    }

    @Test
    public void removeAllSubTasksTest() throws IOException, InterruptedException {
        Epic epic = new Epic("1", "1", TaskStatus.DONE);
        int epicId = createEpic(epic);

        int size = 10;
        for (int i = 0; i < size; i++) {
            SubTask subTask = new SubTask(String.valueOf(i), String.valueOf(i), TaskStatus.NEW);
            createSubTask(subTask, epicId);
        }

        removeAllSubTasks();

        List<SubTask> allSubTasks = getAllSubTasks();

        assertTrue(allSubTasks.isEmpty());
    }

    @Test
    public void getEpicSubTasksTest() throws IOException, InterruptedException {
        List<SubTask> emptyList = getAllSubTasks();
        assertTrue(emptyList.isEmpty());

        Epic epic1 = new Epic("1", "1", TaskStatus.IN_PROGRESS);
        int epic1Id = createEpic(epic1);

        Epic epic2 = new Epic("2", "2", TaskStatus.IN_PROGRESS);
        int epic2Id = createEpic(epic2);

        List<SubTask> subTasksList = new ArrayList<>();
        int size = 10;
        for (int i = 0; i < size; i++) {
            SubTask subTask = new SubTask(String.valueOf(i), String.valueOf(i), TaskStatus.NEW);
            if (i % 2 == 0) {
                int id = createSubTask(subTask, epic1Id);
                subTask.setId(id);
                subTask.setIdEpic(epic1Id);
                subTasksList.add(subTask);
            }
            else {
                createSubTask(subTask, epic2Id);
            }
        }

        List<SubTask> subTaskListResponse = getEpicSubTasks(epic1Id);

        assertTrue(testListEquality(subTasksList, subTaskListResponse));
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        List<Task> history = getHistory();

        assertNotNull(history, "История не должна быть null");
        assertTrue(history.isEmpty(), "История должна быть пустой");

        Task task = new Task("1", "1", TaskStatus.IN_PROGRESS);
        int id = createTask(task);
        task.setId(id);
        findTaskById(id);
        history = getHistory();

        assertTrue(history.contains(task), "История должна содержать задачу");

        removeTask(id);
        history = getHistory();

        assertTrue(history.isEmpty(), "История должна быть пустой");
    }

    @Test
    public void getPrioritizedTasksTest() throws IOException, InterruptedException {
        List<Task> prioritizedTasks = getPrioritizedTasks();
        int numberOfPrioritizedTasks = getAllTasks().size() + getAllSubTasks().size();

        assertEquals(numberOfPrioritizedTasks, prioritizedTasks.size(),
                "Неправильный размер списка приоритетов");
        for (int i = 0; i < numberOfPrioritizedTasks - 1; i++) {
            Task currentTask = prioritizedTasks.get(i);
            Task nextTask = prioritizedTasks.get(i + 1);
            int compareResult = compareTasksByStartTime(currentTask, nextTask);
            assertTrue(compareResult <= 0, "Неправильный порядок в списке приоритетов");
        }
    }


    private List<Task> getPrioritizedTasks() throws IOException, InterruptedException {
        String endPoint = rootPath + "/";
        String serializedList = sendGetRequest(endPoint);
        Type listType = new TypeToken<List<Task>>(){}.getType();
        return gson.fromJson(serializedList, listType);
    }

    private List<Task> getHistory() throws IOException, InterruptedException {
        String endPoint = rootPath + "/history/";
        String serializedList = sendGetRequest(endPoint);
        Type listType = new TypeToken<List<Task>>(){}.getType();
        return gson.fromJson(serializedList, listType);
    }

    private List<SubTask> getEpicSubTasks(int epicId) throws IOException, InterruptedException {
        String endPoint = rootPath + "/subtask/epic/?id=" + epicId;
        String serializedList = sendGetRequest(endPoint);
        Type listType = new TypeToken<List<SubTask>>(){}.getType();
        return gson.fromJson(serializedList, listType);
    }

    private void removeAllSubTasks() throws IOException, InterruptedException {
        String endPoint = rootPath + "/subtask/";
        sendDeleteRequest(endPoint);
    }

    private void removeSubTask(int id) throws IOException, InterruptedException {
        String endPoint = rootPath + "/subtask/?id=" + id;
        sendDeleteRequest(endPoint);
    }

    private void changeSubTask(int id, SubTask subTask, int idEpic) throws IOException, InterruptedException {
        String endPoint = rootPath + "/subtask/?id=" + id + "&idEpic=" + idEpic;
        String body = gson.toJson(subTask);
        sendPostRequest(endPoint, body);
    }

    private List<SubTask> getAllSubTasks() throws IOException, InterruptedException {
        String endPoint = rootPath + "/subtask/";
        String serializedList = sendGetRequest(endPoint);
        Type listType = new TypeToken<List<SubTask>>(){}.getType();
        return gson.fromJson(serializedList, listType);
    }

    private SubTask findSubTaskById(int id) throws IOException, InterruptedException {
        String endPoint = rootPath + "/subtask/?id=" + id;
        String serializedSubTask = sendGetRequest(endPoint);
        return gson.fromJson(serializedSubTask, SubTask.class);
    }

    private int createSubTask(SubTask subTask, int idEpic) throws IOException, InterruptedException {
        String endPoint = rootPath + "/subtask/?id=" + idEpic;
        String body = gson.toJson(subTask);
        String id = sendPostRequest(endPoint, body);
        return Integer.parseInt(id);
    }

    private void removeAllEpics() throws IOException, InterruptedException {
        String endPoint = rootPath + "/epic/";
        sendDeleteRequest(endPoint);
    }

    private void removeEpic(int id) throws IOException, InterruptedException {
        String endPoint = rootPath + "/epic/?id=" + id;
        sendDeleteRequest(endPoint);
    }

    private <T> boolean testListEquality(List<T> list1, List<T> list2) {
        return (list1.size() == list2.size()) &&
                list1.containsAll(list2) &&
                list2.containsAll(list1);
    }

    private int createTask(Task task) throws IOException, InterruptedException {
        String endPoint = rootPath + "/task/";
        String body = gson.toJson(task);
        String id = sendPostRequest(endPoint, body);
        return Integer.parseInt(id);
    }

    private Task findTaskById(int id) throws IOException, InterruptedException {
        String endPoint = rootPath + "/task/?id=" + id;
        String serializedTask = sendGetRequest(endPoint);
        return gson.fromJson(serializedTask, Task.class);
    }

    private List<Task> getAllTasks() throws IOException, InterruptedException {
        String endPoint = rootPath + "/task/";
        String serializedTasks = sendGetRequest(endPoint);
        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        return gson.fromJson(serializedTasks, taskListType);
    }

    private void updateTask(int id, Task task) throws IOException, InterruptedException {
        String endPoint = rootPath + "/task/?id=" + id;
        String body = gson.toJson(task);
        sendPostRequest(endPoint, body);
    }

    private void removeTask(int id) throws IOException, InterruptedException {
        String endPoint = rootPath + "/task/?id=" + id;
        sendDeleteRequest(endPoint);
    }

    private void removeAllTasks() throws IOException, InterruptedException {
        String endPoint = rootPath + "/task/";
        sendDeleteRequest(endPoint);
    }

    private int createEpic(Epic epic) throws IOException, InterruptedException {
        String endPoint = rootPath + "/epic/";
        String body = gson.toJson(epic);
        String id = sendPostRequest(endPoint, body);
        return Integer.parseInt(id);
    }

    private Epic findEpicById(int id) throws IOException, InterruptedException {
        String endPoint = rootPath + "/epic/?id=" + id;
        String serializedEpic = sendGetRequest(endPoint);
        return gson.fromJson(serializedEpic, Epic.class);
    }

    private List<Epic> getAllEpics() throws IOException, InterruptedException {
        String endPoint = rootPath + "/epic/";
        String serializedEpicList = sendGetRequest(endPoint);
        Type epicListType = new TypeToken<List<Epic>>(){}.getType();
        return gson.fromJson(serializedEpicList, epicListType);
    }

    private String sendGetRequest(String endPoint) throws IOException, InterruptedException {
        URI url = URI.create(endPoint);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(StatusCode.SUCCESS.getCode(), response.statusCode());

        return response.body();
    }

    private void sendDeleteRequest(String endPoint) throws IOException, InterruptedException {
        URI url = URI.create(endPoint);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        assertEquals(StatusCode.SUCCESS.getCode(), response.statusCode());
    }

    private String sendPostRequest(String endPoint, String body) throws IOException, InterruptedException {
        URI url = URI.create(endPoint);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(bodyPublisher).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(StatusCode.SUCCESS.getCode(), response.statusCode());

        return response.body();
    }

    private int compareTasksByStartTime(Task task1, Task task2) {
        LocalDateTime task1Time = task1.getStartTime();

        if (task1Time == null)
            return -1;

        LocalDateTime task2Time = task2.getStartTime();

        if (task2Time == null)
            return 1;

        return task1Time.compareTo(task2Time);
    }


}