import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerShould {

    private static final int ID_PLAYER = 1;

    @Mock
    PlayerRepository playerRepository;

    @Test public void
    reduce_coins_when_buying_item() throws NotEnoughCoinsException {
        Item item = new Item(25);
        Player player = new Player(playerRepository, ID_PLAYER, 40);
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
    have_a_positive_amount_of_coins() throws NotEnoughCoinsException {
        Item item = new Item(25);
        Player player = new Player(playerRepository, ID_PLAYER, 40);
        player.buy(item);
        player.buy(item);
    }
}
