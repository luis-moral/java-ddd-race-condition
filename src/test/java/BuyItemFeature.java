import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
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
        inMemoryPlayerRepository = new InMemoryPlayerRepository();
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
    player_should_not_be_able_to_buy_items_if_does_not_have_enough_coins() {
        List<Throwable> errors = new LinkedList<>();

        Player bob = new Player(inMemoryPlayerRepository, BOB_PLAYER_ID, 40);
        Item bag = new Item(BAG_ITEM_ID, 25);
        Item clock = new Item(CLOCK_ITEM_ID, 30);
        Item pants = new Item(PANTS_ITEM_ID, 20);

        inMemoryPlayerRepository.add(bob);
        inMemoryItemRepository.add(bag);
        inMemoryItemRepository.add(clock);
        inMemoryItemRepository.add(pants);

        buyItemForMono(BOB_PLAYER_ID, BAG_ITEM_ID)
            .doOnError(IllegalArgumentException.class, e -> errors.add(e.getCause()))
            .subscribeOn(Schedulers.elastic())
            .subscribe();

        buyItemForMono(BOB_PLAYER_ID, CLOCK_ITEM_ID)
            .doOnError(IllegalArgumentException.class, e -> errors.add(e.getCause()))
            .subscribeOn(Schedulers.elastic())
            .subscribe();

        buyItemForMono(BOB_PLAYER_ID, PANTS_ITEM_ID)
            .doOnError(IllegalArgumentException.class, e -> errors.add(e.getCause()))
            .subscribeOn(Schedulers.elastic())
            .subscribe();

        Mono
            .delay(Duration.ofMillis(200))
            .block();

        Assertions.assertThat(playerService.getInventoryFor(BOB_PLAYER_ID)).containsOnly(BAG_ITEM_ID);
        Assertions.assertThat(errors).hasSize(2);
        Assertions.assertThat(errors.get(0).getClass()).isEqualTo(NotEnoughCoinsException.class);
    }

    private Mono<Void> buyItemForMono(int playerId, int itemId) {
        return
            Mono
                .fromRunnable(() -> {
                    try {
                        playerService.buyItemFor(playerId, itemId);
                    } catch (NotEnoughCoinsException e) {
                        throw new IllegalArgumentException(e);
                    }
                });
    }
}
