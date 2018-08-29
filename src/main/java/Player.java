public class Player {

    private final PlayerRepository playerRepository;
    private int coins;

    public Player(PlayerRepository playerRepository, int coins) {
        this.playerRepository = playerRepository;
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public void buy(Item item) {
        coins -= item.getPrinceInCoins();

        playerRepository.save(this);
    }
}
