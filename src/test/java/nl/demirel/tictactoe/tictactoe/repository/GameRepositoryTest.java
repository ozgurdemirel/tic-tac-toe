package nl.demirel.tictactoe.tictactoe.repository;

import nl.demirel.tictactoe.tictactoe.domain.Game;
import nl.demirel.tictactoe.tictactoe.domain.GameConstant;
import nl.demirel.tictactoe.tictactoe.domain.GameStatus;
import nl.demirel.tictactoe.tictactoe.domain.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GameRepositoryTest {

    private static final String USER_NAME = "player";
    private static final Player PLAYER = Player.builder().username(USER_NAME).marker(GameConstant.PLAYER_MARKER).build();

    @InjectMocks
    private GameRepository gameRepository;

    @Test
    void shouldSaveGameState() {
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'O', '-', 'O'},
                {'X', '-', '-'},
                {'X', 'X', 'X'}
        };
        LocalDateTime secondsAgo = LocalDateTime.now().minusSeconds(100);
        LocalDateTime endTime = secondsAgo.minusSeconds(50);
        Game game = getSampleGame(secondsAgo, endTime, PLAYER, PLAYER, GameStatus.ENDED_WITH_WINNER, gameId, board);
        gameRepository.saveGameState(USER_NAME, game);

        assertThat(gameRepository.getGameState(USER_NAME, gameId)).isEqualTo(game);
    }

    private Game getSampleGame(LocalDateTime startTime, LocalDateTime endTime, Player player, Player current, GameStatus status, String gameID, Character[][] board) {
        return Game.builder()
                .gameId(gameID)
                .board(board)
                .status(status)
                .player(player)
                .gameStartedTime(startTime)
                .gameEndedTime(endTime)
                .currentPlayer(current)
                .build();
    }

}
