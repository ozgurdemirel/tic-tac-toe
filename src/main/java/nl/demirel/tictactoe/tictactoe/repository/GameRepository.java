package nl.demirel.tictactoe.tictactoe.repository;


import nl.demirel.tictactoe.tictactoe.domain.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameRepository {

    // username , (gameId, GAME)
    private Map<String, Map<String, Game>> gameStore = new ConcurrentHashMap<>();

    public void saveGameState(String username, Game game) {
        Map<String, Game> games = gameStore.getOrDefault(username, new HashMap<>());
        games.put(game.getGameId(), game);
        gameStore.put(username, games);
    }

    public Game getGameState(String username, String gameId) {
        Map<String, Game> games = gameStore.getOrDefault(username, new HashMap<>());
        return games.get(gameId);
    }

}
