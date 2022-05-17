package Missions;
import Challenges.Task;
import java.util.LinkedList;


public interface HistoryManager <T extends Task> {

    public void add(T task);  //   помечает задачи как просмотренные
    public LinkedList<HistoryManager<T>> getHistory();  //  возвращает список просмотренных задач

}
