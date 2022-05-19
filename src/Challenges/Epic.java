package Challenges;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    public ArrayList<Integer> idSubTasks;

    public Epic(String name, String description, String status) {
        super(name, description, status);

        idSubTasks = new ArrayList<>();

    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
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
