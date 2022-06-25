package missions;

import challenges.Task;

import java.io.IOException;
import java.util.List;


public interface HistoryManager {

    void addHistory(Task task) throws IOException;  //   помечает задачи как просмотренные

    List<Task> getHistory() throws IOException;  //  возвращает список просмотренных задач

    void remove(int id) throws IOException;

}
