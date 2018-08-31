import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Player {

    private final PlayerRepository playerRepository;
    private final int id;
    private int coins;

    private Set<Item> items;
    private Lock buyItemLock;

    public Player(PlayerRepository playerRepository, int id, int coins) {
        this.id = id;
        this.playerRepository = playerRepository;
        this.coins = coins;

        items = new HashSet<>();
        buyItemLock = new ReentrantLock();
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
        executeInLock(buyItemLock, () -> {
            if (coins < item.getPrinceInCoins()) {
                throw new NotEnoughCoinsException();
            }

            coins -= item.getPrinceInCoins();
            items.add(item);

            playerRepository.save(this);
        });
    }

    private void executeInLock(Lock lock, Action action) throws NotEnoughCoinsException {
        try {
            lock.lock();
            action.execute();
        }
        finally {
            lock.unlock();
        }
    }

    @FunctionalInterface
    private interface Action {
        void execute() throws NotEnoughCoinsException;
    }
}
