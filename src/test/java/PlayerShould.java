import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerShould {

    private static final int BOB_PLAYER_ID = 1;
    private static final int BAG_ITEM_ID = 20;
    private static final int CLOCK_ITEM_ID = 21;

    @Mock
    PlayerRepository playerRepository;

    @Test public void
    be_able_to_buy_items() throws NotEnoughCoinsException {
        Item bag = new Item(BAG_ITEM_ID, 25);
        Item clock = new Item(CLOCK_ITEM_ID, 5);

        Player player = new Player(playerRepository, BOB_PLAYER_ID, 40);
        player.buy(bag);
        player.buy(clock);

        Assertions
            .assertThat(player.getCoins())
            .isEqualTo(10);

        Assertions
            .assertThat(player.getInventory())
            .containsOnly(bag, clock);

        Mockito
            .verify(playerRepository, Mockito.times(2))
            .save(player);
    }

    @Test(expected = NotEnoughCoinsException.class) public void
    have_enough_coins_to_buy_an_item() throws NotEnoughCoinsException {
        Item item = new Item(BAG_ITEM_ID, 25);
        Player player = new Player(playerRepository, BOB_PLAYER_ID, 40);
        player.buy(item);
        player.buy(item);
    }
}
