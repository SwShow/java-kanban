package mission;

import challenges.Epic;
import challenges.SubTask;
import challenges.Task;
import challenges.TaskStatus;
import missions.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class HistoryManagerTest<T extends HistoryManager> {

    protected T historyManager;

    protected Task task;

    protected abstract T getInstance();

    protected LocalDateTime startTime;

    protected long duration;

    @BeforeEach
    void init() {
        historyManager = getInstance();
        startTime = LocalDateTime.of(2022, Calendar.AUGUST, 14, 0, 0, 0);
        duration = 2000;
        task = new Task("task", "description", TaskStatus.NEW,
                startTime, duration);
    }

    @Test
    void add() {
        historyManager.addHistory(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История неправильного размера.");
        assertEquals(task, history.get(0), "Не та задача в истории");
    }

    @Test
    void addDuplicate() {
        historyManager.addHistory(task);
        historyManager.addHistory(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История неправильного размера.");
    }

    @Test
    void addComplex() {
        int historySize = 50;
        List<Task> tasks = fillHistory(historySize);
        List<Task> history = historyManager.getHistory();

        assertEquals(tasks.size(), history.size(), "История неправильного размера");
        for (int i = 0; i < historySize; i++) {
            assertEquals(tasks.get(i), history.get(i), "История неправильно сформирована");
        }

        int middleIndex = historySize / 2;
        historyManager.addHistory(tasks.get(middleIndex));
        history = historyManager.getHistory();

        assertEquals(tasks.size(), history.size(), "История неправильного размера");
        assertEquals(tasks.get(middleIndex), history.get(historySize - 1), "Некорректное добавление в конец");
        assertEquals(tasks.get(middleIndex + 1), history.get(middleIndex), "Не убрано задание из середины");
    }

    @Test
    void removeEmpty() {
        historyManager.remove(task.getId());
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пуста");
    }

    @Test
    void remove() {
        int historySize = 50;
        fillHistory(historySize);

        int firstIndex = 0;
        testRemoveIndex(firstIndex);
        historySize--;

        int middleIndex = historySize / 2;
        testRemoveIndex(middleIndex);
        historySize--;

        int lastIndex = historySize - 1;
        testRemoveIndex(lastIndex);
    }

    private List<Task> fillHistory(int size) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Task task = null;
            if (i % 3 == 0)
                task = new Task("task" + i, "description" + i, TaskStatus.NEW,
                        startTime, duration);
            if (i % 3 == 1)
                task = new SubTask("task" + i, "description" + i, TaskStatus.NEW,
                        startTime, duration);
            if (i % 3 == 2)
                task = new Epic("task" + i, "description" + i, TaskStatus.NEW);
            task.setId(i);
            tasks.add(task);
            historyManager.addHistory(task);
        }
        return tasks;
    }

    private void testRemoveIndex(int indexToRemove) {
        List<Task> oldHistory = historyManager.getHistory();
        int oldSize = oldHistory.size();
        int id = oldHistory.get(indexToRemove).getId();
        historyManager.remove(id);
        List<Task> newHistory = historyManager.getHistory();

        assertEquals(oldSize - 1, newHistory.size(), "История неправильного размера");
        for (int i = 0; i < indexToRemove; i++) {
            assertEquals(oldHistory.get(i), newHistory.get(i), "Неправильно удалилась задача");
        }
        for (int i = indexToRemove + 1; i < oldSize; i++) {
            assertEquals(oldHistory.get(i), newHistory.get(i - 1), "Неправильно удалилась задача");
        }
    }
}
