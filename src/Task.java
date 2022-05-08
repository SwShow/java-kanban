

public class Task {

    String name;
    String description;
    static String status;
    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;

    }


    @Override
    public String toString() {
        return "Task{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}' + "\n";
    }
}

