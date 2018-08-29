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
}
