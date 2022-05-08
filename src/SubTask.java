

public class SubTask  extends Task{
    public SubTask(String name, String description, String status) {
        super(name, description, status);
    }


    @Override
    public String toString() {
        return "SubTask{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}' + "\n";
    }

}
