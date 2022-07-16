package challenges;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int idEpic;

    public SubTask(String name, String description, TaskStatus status,
                   LocalDateTime startTime, long duration) {
        super(TypeTask.SUBTASK, name, description, status, startTime, duration);
    }

    public SubTask(String name, String description, TaskStatus status) {
        super(TypeTask.SUBTASK, name, description, status, null, 0);
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public TypeTask getType() {
        return super.getType();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public String getName() {
        return super.getName();
    }


    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public TaskStatus getStatus() {
        return super.getStatus();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return getIdEpic() == subTask.getIdEpic();
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdEpic());
    }

    @Override
    public String toString() {
        return super.toString() +"," + getIdEpic();
    }
}


