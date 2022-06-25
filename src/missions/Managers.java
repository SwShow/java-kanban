package missions;
import challenges.Epic;
import challenges.SubTask;
import challenges.Task;
import java.io.IOException;
import static challenges.MyEnum.*;
import static challenges.TypeTask.*;
import static missions.FileBackedTasksManager.historyToString;

public class Managers {

    public static TaskManager getDefault() {return new FileBackedTasksManager("history.csv");}
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static void main(String[] args) throws IOException {

        TaskManager manager = Managers.getDefault();

        Task data = new Task(TASK,"посмотреть фильм", "интересный", NEW);
        manager.createTask(data);
        Task dinner = new Task(TASK,"сходить в кафе", "изучить меню", NEW);
        manager.createTask(dinner);
        Task drink = new Task(TASK,"Coffee", "Cappuccino", NEW);
        manager.createTask(drink);
        //System.out.println("Задачи " + manager.getTasks());
        Task go = new Task(TASK,"open the door", "close the door", NEW);
        manager.updateTask(2, go);

        Epic shopping = new Epic(EPIC,"сходить в магазин", "Ашан", NEW);
        SubTask Shop1 = new SubTask(SUBTASK,"купить мыло", "душистое", NEW);
        SubTask Shop2 = new SubTask(SUBTASK,"купить шампунь", "для нормальных волос", DONE);
        manager.createEpic(shopping);
        manager.addSubTask(4, Shop1);
        manager.addSubTask(4, Shop2);

        Epic holiday = new Epic(EPIC,"поход в ресторан", "Макдональдс", NEW);
        SubTask hol = new SubTask(SUBTASK,"позвать друзей", "обсудить меню", NEW);
        manager.createEpic(holiday);
        manager.addSubTask(7, hol);
        //System.out.println("Подзадачи " + manager.getSubTasks());
        SubTask holy = new SubTask(SUBTASK,"купить платье", "вечернее", IN_PROGRESS);
        manager.changeSubTask(5, holy, 4);
        manager.findSubTasksOfIdEpic(4);

        Epic programming = new Epic(EPIC,"сесть за стол", "налить кофе", NEW);
        manager.updateEpic(7, programming);
        //System.out.println("Подзадачи " + manager.getSubTasks());
        SubTask St3 = new SubTask(SUBTASK,"wash the car", "on the way", IN_PROGRESS);
        manager.addSubTask(7, St3);

        //manager.removeSubTask();
        // manager.removeEpic(4);
        //System.out.println("Эпики" + manager.getEpics());
        //System.out.println("Подзадачи " + manager.getSubTasks());

        manager.findTaskById(3);
        manager.findEpicById(4);
        manager.findSubTaskById(6);
        manager.findSubTaskById(5);
        manager.findTaskById(3);
        manager.findSubTaskById(9);
        manager.findTaskById(1);

       System.out.println("История:" + manager.getHistory());
    }

}

