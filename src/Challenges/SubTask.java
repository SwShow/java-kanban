package Challenges;

import java.util.Objects;

public class SubTask extends Task {
    private int idEpic;

    public SubTask(String name, String description, String status, int idEpic) {
        super(name, description, status);

        this.idEpic = idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String getStatus() {
        return super.getStatus();
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
        return "Challenges.SubTask{" +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                ", idEpic='" + idEpic + '\'' +
                '}' + "\n";
    }

}

