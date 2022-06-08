package Missions;

import Challenges.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public interface HistoryManager {

    void addHistory(Task task);  //   помечает задачи как просмотренные

    List<Task> getHistory();  //  возвращает список просмотренных задач

    void remove(int id);
}
