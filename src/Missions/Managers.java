package Missions;


public class Managers {

    public static TaskManager getDefault() {
            return (TaskManager) InMemoryTaskManager.history;
        }

}

