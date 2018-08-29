import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class BuyItemFeature {

    private static final int BOB_PLAYER_ID = 1;
    private static final int CLOCK_ITEM_ID = 21;
    private static final int BAG_ITEM_ID = 20;
    private static final int PANTS_ITEM_ID = 22;

    private PlayerService playerService;
    private InMemoryPlayerRepository inMemoryPlayerRepository;
    private InMemoryItemRepository inMemoryItemRepository;

    @Before
    public void setUp() {
        inMemoryPlayerRepository = new DelayedInMemoryPlayerRepository();
        inMemoryItemRepository = new InMemoryItemRepository();

        playerService = new PlayerService(inMemoryPlayerRepository, inMemoryItemRepository);
    }

    @Test public void
    player_should_be_able_to_buy_items_if_has_enough_coins() throws NotEnoughCoinsException {
        Player bob = new Player(inMemoryPlayerRepository, BOB_PLAYER_ID, 40);
        Item bag = new Item(BAG_ITEM_ID, 25);

        inMemoryPlayerRepository.add(bob);
        inMemoryItemRepository.add(bag);

        playerService.buyItemFor(BOB_PLAYER_ID, BAG_ITEM_ID);

        Assertions.assertThat(playerService.getInventoryFor(BOB_PLAYER_ID)).containsExactly(BAG_ITEM_ID);
    }

    @Test public void
    player_should_not_be_able_to_buy_items_if_does_not_have_enough_coins() throws InterruptedException {
        List<NotEnoughCoinsException> errors = new LinkedList<>();

        Player bob = new Player(inMemoryPlayerRepository, BOB_PLAYER_ID, 40);
        Item bag = new Item(BAG_ITEM_ID, 25);
        Item clock = new Item(CLOCK_ITEM_ID, 30);
        Item pants = new Item(PANTS_ITEM_ID, 20);

        inMemoryPlayerRepository.add(bob);
        inMemoryItemRepository.add(bag);
        inMemoryItemRepository.add(clock);
        inMemoryItemRepository.add(pants);

        BuyItemThread firstBuy = new BuyItemThread(BOB_PLAYER_ID, BAG_ITEM_ID, playerService, errors);
        BuyItemThread secondBuy = new BuyItemThread(BOB_PLAYER_ID, CLOCK_ITEM_ID, playerService, errors);
        BuyItemThread thirdBuy = new BuyItemThread(BOB_PLAYER_ID, PANTS_ITEM_ID, playerService, errors);

        firstBuy.start();
        secondBuy.start();
        thirdBuy.start();

        firstBuy.join();
        secondBuy.join();
        thirdBuy.join();

        Assertions.assertThat(bob.getInventory()).containsAnyOf(bag, clock, pants);
        Assertions.assertThat(bob.getInventory()).hasSize(1);
        Assertions.assertThat(errors).hasSize(2);
        Assertions.assertThat(errors.get(0).getClass()).isEqualTo(NotEnoughCoinsException.class);
    }

    private static class BuyItemThread extends Thread {
        private final int playerId;
        private final int itemId;
        private final PlayerService playerService;
        private final List<NotEnoughCoinsException> errors;

        private BuyItemThread(int playerId, int itemId, PlayerService playerService, List<NotEnoughCoinsException> errors) {
            this.playerId = playerId;
            this.itemId = itemId;
            this.playerService = playerService;
            this.errors = errors;
        }

        public void run() {
            try {
                playerService.buyItemFor(playerId, itemId);
            }
            catch (NotEnoughCoinsException e) {
                errors.add(e);
            }
        }
    }

    private static class DelayedInMemoryPlayerRepository extends InMemoryPlayerRepository {

        @Override
        public Player findById(int playerId) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return super.findById(playerId);
        }
    }
}
