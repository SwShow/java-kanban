package challenges;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private final TypeTask type;
    private final String name;
    private final String description;
    private TaskStatus status;

    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    protected long duration;

    public Task(String name, String description, TaskStatus status,
                LocalDateTime startTime, long duration) {
        this(TypeTask.TASK, name, description, status, startTime, duration);
    }

    public Task(String name, String description, TaskStatus status) {
        this(TypeTask.TASK, name, description, status, null, 0);
    }

    protected Task(TypeTask type, String name, String description, TaskStatus status,
                   LocalDateTime startTime, long duration) {
        if (type == null)
            throw new NullPointerException("type не может быть null");
        if (name == null)
            throw new NullPointerException("name не может быть null");
        if (description == null)
            throw new NullPointerException("description не может быть null");
        if (status == null)
            throw new NullPointerException("status не может быть null");

        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;

        if (startTime == null) {
            endTime = null;
        }
        else {
            endTime = startTime.plusMinutes(duration);
        }

    }

    public TypeTask getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }


    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(type, task.type)
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        String startTimeString = "";
        if (startTime != null)
            startTimeString = startTime.toString();

        String endTimeString = "";
        if (endTime != null)
            endTimeString = endTime.toString();

        return  String.join(",",
                String.valueOf(id), type.toString(),
                name, description, status.toString(),
                startTimeString, String.valueOf(duration), endTimeString);
    }
}


