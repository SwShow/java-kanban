package Missions;

import Challenges.Task;
import java.util.LinkedList;


public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    @Override
    public void add(T task) {

    }

    @Override
    public LinkedList<HistoryManager<T>> getHistory() {
        return null;
    }

}
