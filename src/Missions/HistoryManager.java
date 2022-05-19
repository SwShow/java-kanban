package Missions;

import Challenges.Task;

import java.util.LinkedList;


public interface HistoryManager {

    void addHistory(Task task);  //   помечает задачи как просмотренные

    LinkedList<Task> getHistory();  //  возвращает список просмотренных задач

}
