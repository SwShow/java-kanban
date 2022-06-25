package challenges;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class Epic extends Task {

    public final ArrayList<Integer> idSubTasks;
    private int idEpic;

    public Epic(TypeTask type, String name, String description, MyEnum status) {
        super(type, name, description, status);

        idSubTasks = new ArrayList<>();

    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    @Override
    public TypeTask getType() {
        return super.getType();
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public  int getIdEpic() {
        return idEpic;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public MyEnum getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(MyEnum status) {
        super.setStatus(status);
    }

    @Override
    public String toString() {
        return String.join( ",",valueOf(idEpic), type.toString(), name, description, status.toString(), "\n");
    }
}
