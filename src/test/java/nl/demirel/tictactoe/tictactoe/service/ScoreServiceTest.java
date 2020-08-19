package nl.demirel.tictactoe.tictactoe.service;

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
class ScoreServiceTest {

    private static final String USER_NAME = "player";
    private static final Player PLAYER = Player.builder().username(USER_NAME).marker(GameConstant.PLAYER_MARKER).build();

    @InjectMocks
    private ScoreService scoreService;

    @Test
    void shouldScoreCalculateWithPlayerWinner() {
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'O', '-', 'O'},
                {'X', '-', '-'},
                {'X', 'X', 'X'}
        };
        LocalDateTime secondsAgoStartTime = LocalDateTime.now().minusSeconds(100);
        LocalDateTime endTime = secondsAgoStartTime.plusSeconds(50);
        Game game = getSampleGame(secondsAgoStartTime, endTime, PLAYER, PLAYER, GameStatus.ENDED_WITH_WINNER, gameId, board);
        long seconds = 50;
        int xMoveCount = 4;
        long scoreExpected = GameConstant.MAX_SCORE / (xMoveCount * seconds);

        long score = scoreService.calculate(game);

        assertThat(score).isNotNull();
        assertThat(scoreExpected).isEqualTo(score);
    }

    @Test
    void shouldScoreNotCalculateWithoutWin() {
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'X', 'X', 'O'},
                {'O', 'O', 'X'},
                {'X', 'X', 'O'}
        };
        LocalDateTime secondsAgo = LocalDateTime.now().minusSeconds(100);
        LocalDateTime endTime = secondsAgo.minusSeconds(50);

        Game game = getSampleGame(secondsAgo, endTime, PLAYER, PLAYER, GameStatus.ENDED_WITHOUT_WINNER, gameId, board);

        long score = scoreService.calculate(game);
        assertThat(score).isEqualTo(0);
    }

    @Test
    void shouldScoreNotCalculateWithComputerWin() {
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'O', 'O', 'O'},
                {'X', '-', '-'},
                {'X', '-', 'X'}
        };
        LocalDateTime secondsAgo = LocalDateTime.now().minusSeconds(100);
        LocalDateTime endTime = secondsAgo.minusSeconds(50);

        Game game = getSampleGame(secondsAgo, endTime, PLAYER, GameConstant.COMPUTER, GameStatus.ENDED_WITH_WINNER, gameId, board);

        long score = scoreService.calculate(game);
        assertThat(score).isEqualTo(0);
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
