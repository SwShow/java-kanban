package challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.valueOf;

public class Epic extends Task {

    private final ArrayList<Integer> idSubTasks;
    private int idEpic;

    public Epic(String name, String description, MyEnum status) {
        super(TypeTask.EPIC, name, description, status);

        idSubTasks = new ArrayList<>();

    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    @Override
    public TypeTask getType() {
        return super.getType();
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public MyEnum getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(MyEnum status) {
        super.setStatus(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return getIdEpic() == epic.getIdEpic() && Objects.equals(getIdSubTasks(), epic.getIdSubTasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdSubTasks(), getIdEpic());
    }

    @Override
    public String toString() {
        return getIdEpic() + "," +  getType() + "," + getName() +
                "," + getDescription() +"," + getStatus() +"," + getIdSubTasks();
    }
}
