package challenges;

import missions.InMemoryTaskManager;

import java.io.IOException;
import java.util.Objects;

import static java.lang.String.valueOf;

public class Task {
    private int id;
    public TypeTask type;
    public final String name;
    public final String description;
    public MyEnum status;

    public Task(TypeTask type, String name, String description, MyEnum status) {
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
        return String.join( ",",valueOf(id), type.toString(), name, description, status.toString(), "\n");
    }

}


