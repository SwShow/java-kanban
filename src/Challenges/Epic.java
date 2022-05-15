package Challenges;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    public ArrayList<Integer> idSubTasks;
    private String status;

    public Epic(String name, String description, String status) {
        super(name, description, status);

        this.status = status;
        idSubTasks = new ArrayList<>();

    }


    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return idSubTasks.equals(epic.idSubTasks) && status.equals(epic.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubTasks, status);
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
