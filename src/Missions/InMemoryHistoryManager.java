package Missions;

import Challenges.Task;

import java.util.LinkedList;


class InMemoryHistoryManager implements HistoryManager {
    private static LinkedList<Task> history = new LinkedList<>();

    @Override
    public void addHistory(Task task) {
        if (history.size() >= 10) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }

}
