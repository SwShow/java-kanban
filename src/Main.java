

public class Main {

    public static void main(String[] args) {
        Task data = new Task("посмотреть фильм", "интересный", "NEW");
        Manager.createTasks(data);
        Task dinner = new Task("сходить в кафе", "изучить меню", "NEW");
        Manager.createTasks(dinner);
        Task drink = new Task("Coffee", "Capuccino", "NEW");
        Manager.createTasks(drink);
        System.out.println("Задачи " + Manager.Tasks);

        Epic shopping = new Epic("Покупки", "на неделю", "NEW");
        SubTask Shop1 = new SubTask("купить мыло", "душистое", "NEW");
        SubTask Shop2 = new SubTask("купить шампунь", "для нормальных волос", "NEW");
        Manager.createEpic(shopping, Shop1);
        Manager.addSubTask(4, Shop2);
        Manager.printEpic(4);

        Epic holiday = new Epic("поход в ресторан", "вечером", "NEW");
        SubTask hol = new SubTask("позвать друзей", "обсудить меню", "NEW");
        Manager.createEpic(holiday, hol);
        SubTask holy = new SubTask("позвать друзей", "обсудить меню", "IN_PROGRESS");
        Manager.changeSubTask(7, holy);
        Manager.printEpic(7);

        Task found = Manager.findOfId(2);
        System.out.println(found);
        Task go = new Task("open the door", "close the door", "NEW");
        Manager.updateTask(2, go);
        Manager.removeTask(1);
        System.out.println("Задачи " + Manager.Tasks);

        Task St = Manager.findValOfId(5);
        System.out.println(St);
        SubTask St3 = new SubTask("wash the car", "on the way", "IN_PROGRESS");
        Manager.changeSubTask(5, St3);
        Manager.printEpic(4);
        Manager.removeEpic(4);
        System.out.println("Эпики" + Manager.Epics);
        System.out.println(Manager.SubTasks);
        System.out.println(Manager.getStatus(5));
    }
}