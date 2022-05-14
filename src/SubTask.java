import java.util.Objects;

public class SubTask extends Task {
    int idEpic;

    public SubTask(String name, String description, String status, int idEpic) {
        super(name, description, status);
        this.name = name;
        this.description = description;
        this.status = status;
        this.idEpic = idEpic;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return idEpic == subTask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", idEpic='" + idEpic + '\'' +
                '}' + "\n";
    }

}

