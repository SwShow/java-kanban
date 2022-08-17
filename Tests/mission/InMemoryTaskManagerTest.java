package mission;

import missions.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager getInstance() {
        return new InMemoryTaskManager();
    }

}
