package missions;

public class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    protected InMemoryHistoryManager getInstance() {
        return new InMemoryHistoryManager();
    }

}
