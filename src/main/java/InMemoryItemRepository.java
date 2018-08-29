import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryItemRepository implements ItemRepository {

    private final Map<Integer, Item> itemMap;

    public InMemoryItemRepository() {
        itemMap = new ConcurrentHashMap<>();
    }

    @Override
    public Item findById(int itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public void add(Item item) {
        itemMap.putIfAbsent(item.getId(), item);
    }
}
