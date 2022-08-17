package mission;

import challenges.Epic;
import challenges.SubTask;
import challenges.Task;
import challenges.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTimeTest {

    private final LocalDateTime startTime = LocalDateTime.of(2022, 7, 14, 0, 0);
    private final long duration = 2000;

    @Test
    void taskNoTime() {
        Task task = new Task("task", "", TaskStatus.NEW);

        assertNull(task.getStartTime(), "startTime должен быть null");
        assertEquals(task.getDuration(), 0, "duration должен равнятся 0");
        assertNull(task.getEndTime(), "endTime должен быть null");
    }

    @Test
    void taskEndTime() {
        Task task = new Task("task", "description", TaskStatus.NEW, startTime, duration);

        LocalDateTime endTime = startTime.plusMinutes(duration);

        assertEquals(endTime, task.getEndTime(), "Неправильно рассчитано время окончания задачи");
    }

    @Test
    void subTaskEndTime() {
        SubTask task = new SubTask("task", "description", TaskStatus.NEW, startTime, duration);

        LocalDateTime endTime = startTime.plusMinutes(duration);

        assertEquals(endTime, task.getEndTime(), "Неправильно рассчитано время окончания задачи");
    }

    @Test
    void epicWithoutSubtasks() {
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);

        assertNull(epic.getStartTime(), "У эпика без подзадача не должно быть стартовой даты");
        assertEquals(epic.getDuration(), 0, "У эпика без подзадач время выполнение должно быть 0");
        assertNull(epic.getEndTime(), "У эпика без подзадач не должно быть даты окончания");
    }

    @Test
    void epicWithSubtasks() {
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);

        SubTask subTask1 = new SubTask("subtask1", "", TaskStatus.NEW, startTime, duration);
        epic.addSubTask(subTask1);

        assertEquals(subTask1.getStartTime(), epic.getStartTime(), "Не совпадает стартовая дата");
        assertEquals(subTask1.getDuration(), epic.getDuration(), "Не совпадает длительность");
        assertEquals(subTask1.getEndTime(), epic.getEndTime(), "Не совпадает дата окончания");

        LocalDateTime laterStartTime = startTime.plusMinutes(duration + 1);
        SubTask subTask2 = new SubTask("subtask2", "", TaskStatus.NEW, laterStartTime, duration);
        subTask2.setId(subTask1.getId() + 1);
        epic.addSubTask(subTask2);

        assertEquals(subTask1.getStartTime(), epic.getStartTime(), "Не совпадает стартовая дата");
        assertEquals(subTask1.getDuration() + subTask2.getDuration(),
                epic.getDuration(),
                "Не совпадает длительность");
        assertEquals(subTask2.getEndTime(), epic.getEndTime(), "Не совпадает дата окончания");

        epic.removeSubTask(subTask1.getId());

        assertEquals(subTask2.getStartTime(), epic.getStartTime(), "Не совпадает стартовая дата");
        assertEquals(subTask2.getDuration(), epic.getDuration(), "Не совпадает длительность");
        assertEquals(subTask2.getEndTime(), epic.getEndTime(), "Не совпадает дата окончания");
    }

    @Test
    void epicAddAndRemoveSubtask() {
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);

        SubTask subTask = new SubTask("", "", TaskStatus.NEW, startTime, duration);
        epic.addSubTask(subTask);

        assertEquals(subTask.getStartTime(), epic.getStartTime(), "Не совпадает стартовая дата");
        assertEquals(subTask.getDuration(), epic.getDuration(), "Не совпадает длительность");
        assertEquals(subTask.getEndTime(), epic.getEndTime(), "Не совпадает дата окончания");

        epic.removeSubTask(subTask.getId());

        assertNull(epic.getStartTime(), "У эпика без подзадача не должно быть стартовой даты");
        assertEquals(epic.getDuration(), 0, "У эпика без подзадач время выполнение должно быть 0");
        assertNull(epic.getEndTime(), "У эпика без подзадач не должно быть даты окончания");
    }

    @Test
    void updateTime() {
        SubTask subTask = new SubTask("", "", TaskStatus.NEW, startTime, duration);

        long newDuration = duration * 2;
        LocalDateTime newStartTime = startTime.plusMinutes(duration);
        LocalDateTime newEndTime = newStartTime.plusMinutes(newDuration);

        subTask.setStartTime(newStartTime);
        subTask.setDuration(newDuration);

        assertEquals(subTask.getStartTime(), newStartTime, "Время старта должно совпадать");
        assertEquals(subTask.getDuration(), newDuration, "Длительность должна совпадать");
        assertEquals(subTask.getEndTime(), newEndTime, "Время окончания должно совпадать");

        Epic epic = new Epic("", "", TaskStatus.NEW);
        epic.addSubTask(subTask);

        epic.setStartTime(newStartTime);
        epic.setDuration(newDuration);

        assertEquals(subTask.getStartTime(), epic.getStartTime(), "Время старта у эпика не должно изменятся");
        assertEquals(subTask.getDuration(), epic.getDuration(), "Длительность эпика не должна изменятся");
        assertEquals(subTask.getEndTime(), epic.getEndTime(), "Время окончания эпика не должно изменятся");
    }
}
