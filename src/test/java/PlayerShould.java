import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerShould {

    private static final int BOB_PLAYER_ID = 1;
    private static final int BAG_PLAYER_ID = 10;

    @Mock
    PlayerRepository playerRepository;

    @Test public void
    be_able_to_buy_items() throws NotEnoughCoinsException {
        Item item = new Item(BAG_PLAYER_ID, 25);
        Player player = new Player(playerRepository, BOB_PLAYER_ID, 40);
        player.buy(item);

        Assertions
            .assertThat(player.getCoins())
            .isEqualTo(15);

        Assertions
            .assertThat(player.getInventory())
            .containsExactly(item);

        Mockito
            .verify(playerRepository, Mockito.times(1))
            .save(player);
    }

    @Test(expected = NotEnoughCoinsException.class) public void
    have_enough_coins_to_buy_an_item() throws NotEnoughCoinsException {
        Item item = new Item(BAG_PLAYER_ID, 25);
        Player player = new Player(playerRepository, BOB_PLAYER_ID, 40);
        player.buy(item);
        player.buy(item);
    }
}
