package missions;

import challenges.Task;

import java.io.IOException;
import java.util.List;


public interface HistoryManager {

    void addHistory(Task task);  //   помечает задачи как просмотренные

    List<Task> getHistory();  //  возвращает список просмотренных задач

    void remove(int id);

}
