import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Player {

    private final PlayerRepository playerRepository;
    private final int id;
    private int coins;

    private Set<Item> items;

    public Player(PlayerRepository playerRepository, int id, int coins) {
        this.id = id;
        this.playerRepository = playerRepository;
        this.coins = coins;

        items = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public int getCoins() {
        return coins;
    }

    public Collection<Item> getInventory() {
        return Collections.unmodifiableCollection(items);
    }

    public void buy(Item item) throws NotEnoughCoinsException {
        if (coins < item.getPrinceInCoins()) {
            throw new NotEnoughCoinsException();
        }

        coins -= item.getPrinceInCoins();
        items.add(item);

        playerRepository.save(this);
    }
}
