import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class BuyItemFeature {

    private static final int BAG_PLAYER_ID = 20;
    private static final int BOB_PLAYER_ID = 1;

    private PlayerService playerService;
    private InMemoryPlayerRepository inMemoryPlayerRepository;
    private InMemoryItemRepository inMemoryItemRepository;

    @Before
    public void setUp() {
        inMemoryPlayerRepository = new InMemoryPlayerRepository();
        inMemoryItemRepository = new InMemoryItemRepository();

        playerService = new PlayerService(inMemoryPlayerRepository, inMemoryItemRepository);
    }

    @Test public void
    player_should_be_able_to_buy_items_if_has_enough_money() {
        Item bag = new Item(BAG_PLAYER_ID, 25);
        Player bob = new Player(inMemoryPlayerRepository, BOB_PLAYER_ID, 40);

        inMemoryItemRepository.add(bag);
        inMemoryPlayerRepository.add(bob);

        Assertions.assertThat(playerService.getInventoryFor(BOB_PLAYER_ID)).isEqualTo(BAG_PLAYER_ID);
    }
}
