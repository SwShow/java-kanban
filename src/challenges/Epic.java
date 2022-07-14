package challenges;

import java.time.LocalDateTime;
import java.util.*;


public class Epic extends Task {

    private final Map<Integer, SubTask> subTaskMap;

    public Epic(String name, String description, TaskStatus status) {
        super(TypeTask.EPIC, name, description, status, null, 0);

        subTaskMap = new HashMap<>();
    }

    public void addSubTask(SubTask subTask) {
        // ничего не делать, если задача null
        if (subTask == null)
            return;

        // если subTask с таким id уже был добавлен, удалить старый
        subTaskMap.remove(subTask.getId());

        // добавить задачу
        subTaskMap.put(subTask.getId(), subTask);

        // обновить при необходимости startTime
        LocalDateTime taskStartTime = subTask.getStartTime();
        if (taskStartTime != null && (startTime == null || taskStartTime.isBefore(startTime)))
            startTime = taskStartTime;

        // обновить при необходимости endTime
        LocalDateTime taskEndTime = subTask.getEndTime();
        if (taskEndTime != null && (endTime == null || taskEndTime.isAfter(endTime)))
            endTime = taskEndTime;

        // обновить duration
        duration += subTask.getDuration();
    }

    public SubTask getSubTask(int id) {
        return subTaskMap.get(id);
    }

    public void removeSubTask(int id) {
        // удалить задачу
        SubTask removedTask = subTaskMap.remove(id);

        // закончить, если такой задачи нет
        if (removedTask == null)
            return;

        // обновить при необходимости startTime
        LocalDateTime removedStartTime = removedTask.getStartTime();
        if (removedStartTime != null && removedStartTime.equals(startTime)) {
            startTime = null;
            for (SubTask task: subTaskMap.values()) {
                LocalDateTime taskStartTime = task.getStartTime();
                if (startTime == null || taskStartTime.isBefore(startTime))
                    startTime = taskStartTime;
            }
        }

        // обновить при необходимости endTime
        LocalDateTime removedEndTime = removedTask.getEndTime();
        if (removedEndTime != null && removedEndTime.equals(endTime)) {
            endTime = null;
            for (SubTask task: subTaskMap.values()) {
                LocalDateTime taskEndTime = task.getEndTime();
                if (endTime == null || taskEndTime.isBefore(endTime))
                    endTime = taskEndTime;
            }
        }

        // обновить duration
        duration -= removedTask.getDuration();
    }

    public int getNumberOfSubtasks() {
        return subTaskMap.size();
    }

    public List<Integer> getIdSubtasks() {
        List<Integer> ids = new ArrayList<>();
        for (SubTask task: subTaskMap.values())
            ids.add(task.getId());
        return ids;
    }

    @Override
    public TypeTask getType() {
        return super.getType();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public TaskStatus getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return super.equals(o) && Objects.equals(subTaskMap, epic.subTaskMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskMap);
    }

    @Override
    public String toString() {
        return super.toString() +"," + subTaskMap.toString();
    }
}
