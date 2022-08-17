package mission;

import challenges.*;

import static org.junit.jupiter.api.Assertions.*;

import missions.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Epic epic;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected Task task1;
    protected Task task2;

    protected LocalDateTime startTime;

    protected long duration;

    protected abstract T getInstance();

    @BeforeEach
    protected void init() {
        taskManager = getInstance();
        startTime = LocalDateTime.of(2022, Calendar.AUGUST, 14, 0, 0, 0);
        duration = 2000;
        epic = new Epic("epic", "description epic", TaskStatus.NEW);
        subTask1 = new SubTask("subTask1", "description subtask1", TaskStatus.NEW);
        subTask2 = new SubTask("subTask2", "description subtask2", TaskStatus.NEW);
        task1 = new Task("task1", "description task1", TaskStatus.NEW);
        task2 = new Task("task2", "description task2", TaskStatus.NEW);
    }

    @Test
    void emptyEpic() {
        epic.setStatus(TaskStatus.DONE);
        taskManager.createEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Неправильный статус");
    }

    @Test
    void emptyEpicAfterRemoveSubtasks() {
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        assertEquals(epic.getId(), subTask1.getIdEpic(), "Подзадача не связана с эпиком");
        taskManager.removeSubTasks();
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Неправильный статус");
    }

    @Test
    void epicWithAllNewSubtasks() {
        epic.setStatus(TaskStatus.DONE);
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        taskManager.addSubTask(epic.getId(), subTask2);
        assertEquals(epic.getId(), subTask1.getIdEpic(), "Подзадача не связана с эпиком");
        assertEquals(epic.getId(), subTask2.getIdEpic(), "Подзадача не связана с эпиком");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Неправильный статус");
    }

    @Test
    void epicWithAllDoneSubtasks() {
        subTask1.setStatus(TaskStatus.DONE);
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        assertEquals(epic.getId(), subTask1.getIdEpic(), "Подзадача не связана с эпиком");
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Неправильный статус");
    }

    @Test
    void epicWithNewAndDoneSubtasks() {
        subTask1.setStatus(TaskStatus.DONE);
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        taskManager.addSubTask(epic.getId(), subTask2);
        assertEquals(epic.getId(), subTask1.getIdEpic(), "Подзадача не связана с эпиком");
        assertEquals(epic.getId(), subTask2.getIdEpic(), "Подзадача не связана с эпиком");
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Неправильный статус");
    }

    @Test
    void epicWithInProgressSubtasks() {
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        taskManager.addSubTask(epic.getId(), subTask2);
        assertEquals(epic.getId(), subTask1.getIdEpic(), "Подзадача не связана с эпиком");
        assertEquals(epic.getId(), subTask2.getIdEpic(), "Подзадача не связана с эпиком");
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Неправильный статус");
    }

    @Test
    void epicComplex() {
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask2);
        assertEquals(epic.getId(), subTask2.getIdEpic(), "Подзадача не связано с эпиком");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Неправильный статус");
        taskManager.addSubTask(epic.getId(), subTask1);
        assertEquals(epic.getId(), subTask1.getIdEpic(), "Подзадача не связано с эпиком");
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Неправильный статус");
        taskManager.removeSubTask(subTask1.getId());
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Неправильный статус");
    }

    @Test
    void createTask() {
        taskManager.createTask(task1);

        Task savedTask = taskManager.findTaskById(task1.getId());

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(savedTask, task1, "Задачи не совпадают");

        List<Task> tasks = taskManager.getTasks();

        assertEquals(task1, tasks.get(0), "Задачи не совпадают");
        assertEquals(1, getNumberOfTasks(), "Неверное число задач");

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertFalse(prioritizedTasks.isEmpty(), "Список приоритетов не должен быть пуст");
        assertEquals(task1, prioritizedTasks.get(0), "В списке приоритетов не та задача");
    }

    @Test
    void createNullTask() {
        taskManager.createTask(null);
        List<Task> tasks = taskManager.getTasks();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertTrue(tasks.isEmpty(), "Добавлена null задача");
        assertTrue(prioritizedTasks.isEmpty(), "В список приоритетов добавлена null задача");
    }
    @Test
    void createTaskComplex() {
        int numberOfTasks = 50;
        List<Task> tasks = fillManagerWithTasks(numberOfTasks);

        for (Task task: tasks) {
            Task savedTask = taskManager.findTaskById(task.getId());
            assertNotNull(savedTask, "Задача не найдена");
            assertEquals(savedTask, task, "Задачи не совпадают");
        }

        assertEquals(tasks.size(), getNumberOfTasks(), "Неверное число задач");
        testPrioritizedTasks();
    }

    @Test
    void createTaskOverlappingTime() {
        Task task = new Task("", "", TaskStatus.NEW, startTime, duration);
        taskManager.createTask(task);

        Task overlappingTask = new Task("", "", TaskStatus.NEW, startTime, duration);
        taskManager.createTask(overlappingTask);

        assertNull(taskManager.findTaskById(overlappingTask.getId()), "Задача не должна быть добавлена");
        assertEquals(1, taskManager.getPrioritizedTasks().size(),
                "Неправильный размер списка приоритетов");
    }

    @Test
    void createTaskTestCloseTime() {
        Task task = new Task("", "", TaskStatus.NEW, startTime, duration);
        taskManager.createTask(task);

        LocalDateTime closeTime = startTime.plusMinutes(duration);
        Task closeTask = new Task("", "", TaskStatus.NEW, closeTime, duration);
        taskManager.createTask(closeTask);

        assertEquals(closeTask, taskManager.findTaskById(closeTask.getId()),
                "Задача должна быть добавлена");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "Неправильная длина списка приоритов");
        testPrioritizedTasks();
    }

    @Test
    void findTaskById() {
        int nonExistentId = 1;
        Task nonExistentTask = taskManager.findTaskById(nonExistentId);

        assertNull(nonExistentTask, "Найдена несуществующая задача");

        taskManager.createTask(task1);
        Task savedTask = taskManager.findTaskById(task1.getId());

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task1, savedTask, "Задачи не совпадают");

        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "Задача не внесена в историю");
        assertEquals(task1, history.get(0), "В историю внесена не та задача");
    }

    @Test
    void removeTasks() {
        taskManager.createTask(task1);

        taskManager.removeTasks();

        Task nonExistentTask = taskManager.findTaskById(task1.getId());

        assertNull(nonExistentTask, "Задача должна быть удалена");

        assertEquals(0, getNumberOfTasks(), "Список задач должен быть пуст");
        assertTrue(taskManager.getPrioritizedTasks().isEmpty(), "Список приоритетов должен быть пуст");
    }

    @Test
    void removeTasksComplex() {
        int numberOfTasks = 50;
        List<Task> tasks = fillManagerWithTasks(numberOfTasks);

        taskManager.removeTasks();

        for (Task task: tasks) {
            assertNull(taskManager.findTaskById(task.getId()), "Задача должна быть удалена");
        }

        assertEquals(0, getNumberOfTasks(), "Список задач должен быть пуст");
        assertTrue(taskManager.getPrioritizedTasks().isEmpty(), "Список приоритетов должен быть пуст");
    }

    @Test
    void updateTaskNonExistentId() {
        int nonExistentId = 0;
        taskManager.updateTask(nonExistentId, task1);
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "Добавлена задача по несуществующему id");
    }

    @Test
    void updateNullTask() {
        taskManager.createTask(task1);
        taskManager.updateTask(task1.getId(), null);
        Task updatedTask =  taskManager.findTaskById(task1.getId());
        assertEquals(task1, updatedTask, "Добавлена null задача");
    }

    @Test
    void updateTask() {
        taskManager.createTask(task1);
        taskManager.updateTask(task1.getId(), task2);
        Task updatedTask = taskManager.findTaskById(task1.getId());
        assertEquals(task2, updatedTask, "Задача не обновлена");

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(1, prioritizedTasks.size(), "Неправильный размер списка приоритетов");
        assertEquals(task2, prioritizedTasks.get(0), "Задача в списке приоритетов не обновлена");
    }

    @Test
    void removeTaskNonExistentId() {
        taskManager.createTask(task1);
        int nonExistentId = 0;
        taskManager.removeTask(nonExistentId);
        assertEquals(1, getNumberOfTasks(), "Удалена неправильная задача");
    }

    @Test
    void removeTask() {
        taskManager.createTask(task1);
        int id = task1.getId();
        taskManager.removeTask(id);
        assertEquals(0, getNumberOfTasks(), "Список задач должен быть пуст");
        assertNotEquals(task1, taskManager.findTaskById(id), "Задача не удалена");
        assertTrue(taskManager.getPrioritizedTasks().isEmpty(), "Список приоритетов должен быть пуст");
    }

    @Test
    void findNonExistentEpic() {
        taskManager.createEpic(epic);
        int nonExistentId = 0;
        Epic foundEpic = taskManager.findEpicById(nonExistentId);
        assertNull(foundEpic, "Найден эпик по несуществующему id");
    }

    @Test
    void findEpic() {
        taskManager.createEpic(epic);
        Epic foundEpic = taskManager.findEpicById(epic.getId());
        assertEquals(epic, foundEpic, "Найден не тот эпик");
    }

    @Test
    void updateEpicNull() {
        taskManager.createEpic(epic);
        int id = epic.getId();
        taskManager.updateEpic(id, null);
        Epic foundEpic = taskManager.findEpicById(id);
        assertNotNull(foundEpic, "Обновлен null эпик");
        assertEquals(epic, foundEpic, "Эпики должны совпадать");
    }

    @Test
    void updateEpicNonExistentId() {
        int nonExistentId = 0;
        taskManager.updateEpic(nonExistentId, epic);
        assertEquals(0, getNumberOfEpics(), "Добавлен эпик по несуществующему id");
    }

    @Test
    void updateEpic() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        int id = epic.getId();
        Epic toUpdate = new Epic("updated", "to update", TaskStatus.NEW);
        taskManager.updateEpic(id, toUpdate);
        assertEquals(toUpdate, taskManager.findEpicById(id), "Эпик не обновлен");
        assertNull(taskManager.findSubTaskById(subTask1.getId()), "Не удалена подзадача");
        assertTrue(taskManager.getPrioritizedTasks().isEmpty(), "Не удалена подзадача из списка приоритетов");
    }

    @Test
    void removeEpics() {
        fillManagerWithEpics(50);
        taskManager.removeEpics();
        assertEquals(0, getNumberOfEpics(), "Эпики не удалены");
    }

    @Test
    void removeEpicNonExistentId() {
        taskManager.createEpic(epic);
        int nonExistentId = 0;
        taskManager.removeEpic(nonExistentId);
        assertEquals(1, getNumberOfEpics(), "Неправильное количество эпиков");
    }

    @Test
    void removeEpic() {
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        taskManager.findEpicById(epicId);

        taskManager.addSubTask(epicId, subTask1);
        int subTaskId = subTask1.getId();
        taskManager.findSubTaskById(subTaskId);

        taskManager.removeEpic(epicId);
        assertNull(taskManager.findEpicById(epicId), "Эпик не удалился");
        assertNull(taskManager.findSubTaskById(epicId), "Подзадача не удалилась");
        assertTrue(taskManager.getHistory().isEmpty(), "История должна быть пуста");
        assertTrue(taskManager.getPrioritizedTasks().isEmpty(), "Список приоритетов должен быть пуст");
    }

    @Test
    void addSubTaskNonExistentId() {
        int nonExistentId = 0;
        taskManager.addSubTask(nonExistentId, subTask1);
        assertEquals(0, getNumberOfSubTasks(), "Число подзадач не должно было увеличится");
    }

    @Test
    void addSubTaskNull() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), null);
        assertEquals(0, getNumberOfSubTasks(), "Число сабстасков не должно было увеличится");
        assertEquals(0, epic.getNumberOfSubtasks(), "Подзадача не должена быть связана с эпиком");
    }

    @Test
    void addSubTask() {
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        taskManager.addSubTask(epicId, subTask1);
        taskManager.addSubTask(epicId, subTask2);
        int subTaskId = subTask1.getId();
        SubTask addedSubtask = taskManager.findSubTaskById(subTaskId);

        assertEquals(subTask1, addedSubtask, "Подзадачи не совпадают");
        assertEquals(subTask1, epic.getSubTask(subTaskId), "Эпик не связан с подзадачей");

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(1, prioritizedTasks.size(), "Неправильный размер списка приоритетов");
        assertEquals(subTask1, prioritizedTasks.get(0), "В списке приоритетов неправильная задача");
    }

    @Test
    void findSubTaskByIdNonExistent() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        int nonExistentId = 0;
        SubTask foundSubtask = taskManager.findSubTaskById(nonExistentId);
        assertNull(foundSubtask, "Найденная подзадача должна равнятся null");
        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пуста");
    }

    @Test
    void findSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        SubTask foundSubtask = taskManager.findSubTaskById(subTask1.getId());
        assertEquals(subTask1, foundSubtask, "Найдена не та подзадача");
        List<Task> history = taskManager.getHistory();
        assertFalse(history.isEmpty(), "История должна быть не пуста");
        assertEquals(subTask1, history.get(0), "Подзадача не записалась в истории");
    }

    @Test
    void removeSubTasks() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        taskManager.addSubTask(epic.getId(), subTask2);
        taskManager.findSubTaskById(subTask1.getId());
        taskManager.removeSubTasks();
        assertEquals(0, getNumberOfSubTasks(), "Не все подзадачи удалились");
        assertTrue(taskManager.getHistory().isEmpty(), "История должна быть пуста");
        assertEquals(0, epic.getNumberOfSubtasks(), "Связь с эпиком должна была удалится");
        assertTrue(taskManager.getPrioritizedTasks().isEmpty(), "Список приоритетов должен быть пуст");
    }

    @Test
    void removeSubTaskNonExistent() {
        int nonExistentId = 0;
        taskManager.removeSubTask(nonExistentId);
    }

    @Test
    void removeSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        int id = subTask1.getId();
        taskManager.findSubTaskById(id);
        taskManager.removeSubTask(id);
        assertEquals(0, getNumberOfSubTasks(), "Подзадача не удалилась");
        assertTrue(taskManager.getHistory().isEmpty(), "История должна быть пуста");
        assertEquals(0, epic.getNumberOfSubtasks(), "Связь с эпиком должна была удалится");
        assertTrue(taskManager.getPrioritizedTasks().isEmpty(), "Список приоритетов должен быть пуст");
    }

    @Test
    void getStatusSubTaskNonExistentId() {
        int nonExistentId = 0;
        TaskStatus taskStatus = taskManager.getStatusSubTask(nonExistentId);
        assertNull(taskStatus, "Статус должен был null");
    }

    @Test
    void getStatusSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        TaskStatus status = taskManager.getStatusSubTask(subTask1.getId());
        assertEquals(status, subTask1.getStatus(), "Статус не совпадает");
    }

    @Test
    void changeSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);
        int subTaskId = subTask1.getId();
        taskManager.findSubTaskById(subTaskId);

        Epic epic1 = new Epic("epic1", "description epic1", TaskStatus.NEW);
        taskManager.createEpic(epic1);
        taskManager.changeSubTask(subTaskId, subTask1, epic1.getId());

        assertEquals(subTask1, epic1.getSubTask(subTaskId), "Эпик должен быть связан с подзадачей");
        assertEquals(epic1.getId(), subTask1.getIdEpic(), "Подзадача должна быть связана с эпиком");
        assertNull(epic.getSubTask(subTaskId), "Старый эпик не должен быть связан с подзадачей");

        taskManager.changeSubTask(subTaskId, subTask2, epic.getId());
        List<Task> history = taskManager.getHistory();

        assertTrue(history.isEmpty(), "Старая подзадача удалилась из истории");
    }

    @Test
    void changeSubTaskNonExistentId() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(epic.getId(), subTask1);

        int nonExistentId = 0;
        taskManager.changeSubTask(nonExistentId, subTask2, epic.getId());
        assertNull(taskManager.findSubTaskById(nonExistentId), "Подзадача не должена была добавится");

        taskManager.changeSubTask(subTask1.getId(), null, epic.getId());
        SubTask changedSubtask = taskManager.findSubTaskById(subTask1.getId());
        assertEquals(subTask1, changedSubtask, "Подзадача не должена была изменится");

        taskManager.changeSubTask(subTask1.getId(), subTask2, nonExistentId);
        changedSubtask = taskManager.findSubTaskById(subTask1.getId());
        assertEquals(subTask1, changedSubtask, "Подзадача не должна была изменится");
    }

    @Test
    void findSubtasksNonExistentId() {
        int nonExistentId = 0;
        List<Integer> subTasks = taskManager.findSubtasksOfIdEpic(nonExistentId);
        assertNull(subTasks, "Список должен равнятся null");
    }

    @Test
    void findSubtasksOfIdEpic() {
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        taskManager.addSubTask(epicId, subTask1);
        taskManager.addSubTask(epicId, subTask2);
        List<Integer> subTasksId = taskManager.findSubtasksOfIdEpic(epicId);

        assertEquals(2, subTasksId.size(), "Неправильный размер списка");

        Integer subTask1Id = subTask1.getId();
        assertTrue(subTasksId.contains(subTask1Id), "Подзадача не содержится в списке");

        Integer subTask2Id = subTask2.getId();
        assertTrue(subTasksId.contains(subTask2Id), "Подзадача не содержится в списке");
    }

    @Test
    void getEpics() {
        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Список не должен равнятся null");
        assertTrue(epics.isEmpty(), "Список должен быть пуст");

        int size = 100;
        List<Epic> epicsToAdd = fillManagerWithEpics(size);
        List<Epic> addedEpics = taskManager.getEpics();

        assertEquals(size, addedEpics.size(), "Количество эпиков не совпадает");
        for (Epic epic: epicsToAdd) {
            assertTrue(addedEpics.contains(epic), "В списке должен содержаться эпик");
        }
    }

    @Test
    void getSubTasks() {
        List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Список не должен равнятся null");
        assertTrue(subTasks.isEmpty(), "Список должен быть пуст");

        taskManager.createEpic(epic);
        int size = 100;
        List<SubTask> subTasksToAdd = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            SubTask subTask = new SubTask("subtask" + i, "description" + i, TaskStatus.IN_PROGRESS,
                    startTime, duration);
            subTasksToAdd.add(subTask);
            taskManager.addSubTask(epic.getId(), subTask);
        }
        List<SubTask> addedSubtasks = taskManager.getSubTasks();

        assertEquals(size, addedSubtasks.size(), "Количество подзадач не совпадает");
        for (SubTask subTask: subTasksToAdd) {
            assertTrue(addedSubtasks.contains(subTask), "В списке должна содержаться подзадача");
        }
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Список не должен равнятся null");
        assertTrue(tasks.isEmpty(), "Список должен быть пуст");

        int size = 100;
        List<Task> tasksToAdd = fillManagerWithTasks(size);
        List<Task> addedTasks = taskManager.getTasks();

        assertEquals(size, addedTasks.size(), "Количество задач не совпадает");
        for (Task task: tasksToAdd) {
            assertTrue(addedTasks.contains(task), "В списке должена содержаться задача");
        }
    }

    @Test
    void getHistory() {
        List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не должна быть null");
        assertTrue(history.isEmpty(), "История должна быть пустой");

        taskManager.createTask(task1);
        taskManager.findTaskById(task1.getId());
        history = taskManager.getHistory();

        assertTrue(history.contains(task1), "История должна содержать задачу");

        taskManager.removeTask(task1.getId());
        history = taskManager.getHistory();

        assertTrue(history.isEmpty(), "История должна быть пустой");
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

    private void testPrioritizedTasks() {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        int numberOfPrioritizedTasks = getNumberOfTasks() + getNumberOfSubTasks();

        assertEquals(numberOfPrioritizedTasks, prioritizedTasks.size(),
                "Неправильный размер списка приоритетов");
        for (int i = 0; i < numberOfPrioritizedTasks - 1; i++) {
            Task currentTask = prioritizedTasks.get(i);
            Task nextTask = prioritizedTasks.get(i + 1);
            int compareResult = compareTasksByStartTime(currentTask, nextTask);
            assertTrue(compareResult <= 0, "Неправильный порядок в списке приоритетов");
        }
    }
    private List<Task> fillManagerWithTasks(int size) {
        List<Task> tasks = new ArrayList<>();
        LocalDateTime time = LocalDateTime.of(1990, 10, 25, 10, 30);
        for (int i = 0; i < size; i++) {
            LocalDateTime startTime = time.plusMinutes(duration * i);
            Task task = new Task("task" + i, "description" + i, TaskStatus.NEW,
                    startTime, duration);
            tasks.add(task);
            taskManager.createTask(task);
        }
        return tasks;
    }

    private List<Epic> fillManagerWithEpics(int size) {
        List<Epic> tasks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Epic task = new Epic("epic" + i, "description" + i, TaskStatus.NEW);
            tasks.add(task);
            taskManager.createEpic(task);
        }
        return tasks;
    }

    private int getNumberOfTasks() {
        List<Task> tasks = taskManager.getTasks();
        return tasks.size();
    }

    private int getNumberOfEpics() {
        List<Epic> epics = taskManager.getEpics();
        return epics.size();
    }

    private int getNumberOfSubTasks() {
        List<SubTask> subTasks = taskManager.getSubTasks();
        return subTasks.size();
    }
}
