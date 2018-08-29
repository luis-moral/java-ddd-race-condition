public class Item {

    private final int id;
    private final int princeInCoins;

    public Item(int id, int priceInCoins) {
        this.id = id;
        this.princeInCoins = priceInCoins;
    }

    public int getId() {
        return id;
    }

    public int getPrinceInCoins() {
        return princeInCoins;
    }
}
