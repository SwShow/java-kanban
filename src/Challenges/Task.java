package Challenges;

import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private MyEnum status;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = MyEnum.valueOf(status);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(String status) {
        this.status = MyEnum.valueOf(status);
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
        return "Challenges.Task{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}' + "\n";
    }
}


