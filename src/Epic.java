import java.util.ArrayList;


public class Epic extends Task {
    ArrayList<Integer> idSubTasks;
    public String name;
    public String description;
    public String status;

    public Epic(String name, String description, String status) {
        super(name, description, status);

        idSubTasks = new ArrayList<>();
        this.description = description;
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idSubTask'" + idSubTasks + '\'' +
                ", status=" + status +
                '}' + "\n";
    }


}