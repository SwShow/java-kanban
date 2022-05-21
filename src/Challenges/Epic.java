package Challenges;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> idSubTasks;
    private int idEpic;

    public Epic(String name, String description, MyEnum status) {
        super(name, description, status);

        idSubTasks = new ArrayList<>();

    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
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
