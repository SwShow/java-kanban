package Missions;

import Challenges.Task;

import java.util.ArrayList;


class InMemoryHistoryManager implements HistoryManager {
    private static ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addHistory(Task task) {
        if (history.size() >= 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

}
