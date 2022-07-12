package challenges;

import java.util.Objects;

public class Task {
    private int id;
    private TypeTask type;
    private final String name;
    private final String description;
    private MyEnum status;

    public Task(String name, String description, MyEnum status) {
        this(TypeTask.TASK, name, description, status);
    }
    protected Task(TypeTask type, String name, String description, MyEnum status) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public TypeTask getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(MyEnum status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }


    public MyEnum getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return  getId() + "," + getType() + "," + getName() + "," +
                 getDescription() + "," +
                 getStatus();
    }
}


