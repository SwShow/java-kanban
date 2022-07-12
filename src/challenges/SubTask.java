package challenges;

import java.util.Objects;

public class SubTask extends Task {
    private int idEpic;
    private int idSubTask;

    public SubTask(String name, String description, MyEnum status) {
        super(TypeTask.SUBTASK, name, description, status);
    }

    public int getIdEpic() {
        return idEpic;
    }

    public int getIdSubTask() {
        return idSubTask;
    }

    public void setIdSubTask(int idSubTask) {
        this.idSubTask = idSubTask;
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
        return  getIdSubTask() + "," + getType() +"," +
                getName() + "," +
                 getDescription() + "," +
                getStatus() +","
                + getIdEpic();
    }
}

