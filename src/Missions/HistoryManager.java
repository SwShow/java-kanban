package Missions;

import Challenges.Task;

import java.util.ArrayList;
import java.util.LinkedList;


public interface HistoryManager {

    void addHistory(Task task);  //   помечает задачи как просмотренные

    ArrayList<Task> getHistory();  //  возвращает список просмотренных задач

}
