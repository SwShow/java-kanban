package challenges;

import java.util.Objects;

public class SubTask extends Task {
    private int idEpic;
    private int id;

    public SubTask(String name, String description, MyEnum status, int idEpic) {
        super(name, description, status);

        this.id = id;
    }


    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public MyEnum getStatus() {
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

