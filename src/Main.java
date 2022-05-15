import Epic.Epic;
import SubTask.SubTask;
import Task.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task data = new Task("посмотреть фильм", "интересный", "NEW");
        manager.createTask(data);
        Task dinner = new Task("сходить в кафе", "изучить меню", "NEW");
        manager.createTask(dinner);
        Task drink = new Task("Coffee", "Capuccino", "NEW");
        manager.createTask(drink);
        System.out.println("Задачи " + manager.tasks);
        Task found = manager.findTaskById(2);
        System.out.println(found);
        Task go = new Task("open the door", "close the door", "NEW");
        manager.updateTask(2, go);
        manager.removeTask(1);
        System.out.println("Задачи " + manager.tasks);  // получить список всех задач

        Epic shopping = new Epic("сходить в магазин", "Ашан", "NEW");
        SubTask Shop1 = new SubTask("купить мыло", "душистое", "NEW", 0);
        SubTask Shop2 = new SubTask("купить шампунь", "для нормальных волос", "NEW", 0);
        manager.createEpic(shopping);
        manager.addSubTask(4, Shop1);
        manager.addSubTask(4, Shop2);

        Epic holiday = new Epic("поход в ресторан", "Макдональдс", "NEW");
        SubTask hol = new SubTask("позвать друзей", "обсудить меню", "NEW", 0);
        manager.createEpic(holiday);
        manager.addSubTask(7, hol);
        SubTask holy = new SubTask("купить платье", "вечернее", "IN_PROGRESS", 0);
        manager.changeSubTask(5, holy, 4);
        manager.findSubTasksOfIdEpic(4);

        Task St = manager.findValOfId(5);
        System.out.println(St);
        SubTask St3 = new SubTask("wash the car", "on the way", "IN_PROGRESS", 0);
        manager.changeSubTask(5, St3, 4);

        manager.removeEpic(4);
        System.out.println("Эпики" + manager.epics);
        System.out.println(manager.subTasks);
        System.out.println(manager.getStatusSubTask(5));
    }
}