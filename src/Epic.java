

public class Epic extends Task{



    public Epic(String name, String description, String status) {
        super(name, description, status);
    }


    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}' + "\n";
    }
}
