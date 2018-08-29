import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceShould {

    private static final int BOB_PLAYER_ID = 1;
    private static final int BAG_ITEM_ID = 10;

    @Mock
    Player bob;
    @Mock
    Item bag;
    @Mock
    PlayerRepository playerRepository;
    @Mock
    ItemRepository itemRepository;

    @Before
    public void setUp() {
        BDDMockito
            .given(playerRepository.findById(BOB_PLAYER_ID))
            .willReturn(bob);

        BDDMockito
            .given(itemRepository.findById(BAG_ITEM_ID))
            .willReturn(bag);
    }

    @Test public void
    allow_players_to_buy_items() throws NotEnoughCoinsException {
        PlayerService playerService = new PlayerService(playerRepository, itemRepository);
        playerService.buyItemFor(BOB_PLAYER_ID, BAG_ITEM_ID);

        Mockito
            .verify(bob, Mockito.times(1))
            .buy(bag);
    }

    @Test public void
    return_the_player_inventory() {
        BDDMockito
            .given(bag.getId())
            .willReturn(BAG_ITEM_ID);

        BDDMockito
            .given(bob.getInventory())
            .willReturn(Arrays.asList(bag));

        PlayerService playerService = new PlayerService(playerRepository, itemRepository);

        Assertions.assertThat(playerService.getInventoryFor(BOB_PLAYER_ID)).containsExactly(BAG_ITEM_ID);
    }
}
