package mission;

//import missions.HistoryManagerTest;
import missions.InMemoryHistoryManager;

public class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    protected InMemoryHistoryManager getInstance() {
        return new InMemoryHistoryManager();
    }

}
