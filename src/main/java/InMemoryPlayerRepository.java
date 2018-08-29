import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPlayerRepository implements PlayerRepository {

    private final Map<Integer, Player> playerMap;

    public InMemoryPlayerRepository() {
        playerMap = new ConcurrentHashMap<>();
    }

    @Override
    public void save(Player player) {
    }

    @Override
    public Player findById(int playerId) {
        return playerMap.get(playerId);
    }

    @Override
    public void add(Player player) {
        playerMap.put(player.getId(), player);
    }
}
