package missions;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager getInstance() {
        return new InMemoryTaskManager();
    }

}
