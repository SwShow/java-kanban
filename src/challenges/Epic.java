package challenges;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> idSubTasks;
    private int id;

    public Epic(String name, String description, MyEnum status) {
        super(name, description, status);

        idSubTasks = new ArrayList<>();

    }

    public List<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setStatus(MyEnum status) {
        super.setStatus(status);
    }

    @Override
    public String toString() {
        return "Challenges.Epic{" +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", idSubTask'" + idSubTasks + '\'' +
                ", status=" + super.getStatus() +
                '}' + "\n";
    }


}
