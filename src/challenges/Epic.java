package challenges;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> idSubTasks;
    private int idEpic;

    public Epic(String name, String description, MyEnum status) {
        super(name, description, status);

        idSubTasks = new ArrayList<>();

    }


    public ArrayList<Integer> getIdSubTasks() {
        return (ArrayList<Integer>) idSubTasks;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public int getId() {
        return idEpic;
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
