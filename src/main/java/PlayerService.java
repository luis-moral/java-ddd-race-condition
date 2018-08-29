import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;

    public PlayerService(PlayerRepository playerRepository, ItemRepository itemRepository) {
        this.playerRepository = playerRepository;
        this.itemRepository = itemRepository;
    }

    public void buyItemForPlayer(int playerId, int itemId) throws NotEnoughCoinsException {
        Player player = playerRepository.findById(playerId);
        Item item = itemRepository.findById(itemId);

        player.buy(item);
    }

    public Collection<Integer> getInventoryFor(int playerId) {
        return
            playerRepository.findById(playerId)
                .getInventory()
                .stream()
                .map(item -> item.getId())
                .collect(Collectors.toList());
    }
}
