public interface PlayerRepository {

    void save(Player player);

    Player findById(int playerId);

    void add(Player player);
}
