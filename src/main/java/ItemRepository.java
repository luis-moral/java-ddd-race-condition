public interface ItemRepository {

    Item findById(int itemId);

    void add(Item item);
}
