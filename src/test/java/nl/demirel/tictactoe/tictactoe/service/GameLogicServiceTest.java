package nl.demirel.tictactoe.tictactoe.service;

import nl.demirel.tictactoe.tictactoe.common.exception.InvalidRowOrColumnException;
import nl.demirel.tictactoe.tictactoe.domain.*;
import nl.demirel.tictactoe.tictactoe.domain.Game;
import nl.demirel.tictactoe.tictactoe.domain.GameStatus;
import nl.demirel.tictactoe.tictactoe.domain.Player;
import nl.demirel.tictactoe.tictactoe.domain.Score;
import nl.demirel.tictactoe.tictactoe.repository.GameRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameLogicServiceTest {

    private static final String USER_NAME = "player";
    private static final Player PLAYER = Player.builder().username(USER_NAME).marker(GameConstant.PLAYER_MARKER).build();

    @InjectMocks
    private GameLogicService gameLogicService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ScoreService scoreService;

    @Captor
    private ArgumentCaptor<String> userNameCaptor;

    @Captor
    private ArgumentCaptor<Game> gameCaptor;

    @Test
    void shouldInitializeGame() {

        Game game = gameLogicService.initializeGameBoard(USER_NAME);

        verify(gameRepository, times(1)).saveGameState(userNameCaptor.capture(), gameCaptor.capture());

        assertThat(userNameCaptor.getValue()).isEqualTo(USER_NAME);
        assertThat(gameCaptor.getValue().getGameId()).isNotEmpty();
        assertThat(gameCaptor.getValue().getStatus()).isEqualTo(GameStatus.STARTED);
        assertThat(gameCaptor.getValue().getBoard()).isNotEmpty();
        assertThat(game).isEqualTo(gameCaptor.getValue());
    }

    @Test
    void shouldMoveHappen() {
        int row = 2;
        int column = 2;
        String gameId = "gameId";
        Game game = getSampleGame(LocalDateTime.now(), PLAYER, GameStatus.STARTED, gameId);

        when(gameRepository.getGameState(USER_NAME, gameId)).thenReturn(game);

        Game afterMoveGame = gameLogicService.move(USER_NAME, gameId, row, column);

        assertThat(afterMoveGame.getGameId()).isEqualTo(gameId);
        assertThat(afterMoveGame.getBoard()[row][column]).isEqualTo(GameConstant.PLAYER_MARKER);
        assertThat(isArrayContainsValue(game.getBoard(), GameConstant.COMPUTER_MARKER)).isEqualTo(Boolean.TRUE);
    }

    @Test
    void shouldGameEndWithoutAnyWinnerAfterMove() {
        int row = 2;
        int column = 1;
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'X', 'X', 'O'},
                {'O', 'O', 'X'},
                {'X', '-', 'O'}
        };
        Game game = getSampleGame(LocalDateTime.now(), PLAYER, GameStatus.STARTED, gameId, board);
        when(gameRepository.getGameState(USER_NAME, gameId)).thenReturn(game);
        Game afterMoveGame = gameLogicService.move(USER_NAME, gameId, row, column);

        assertThat(afterMoveGame.getStatus()).isEqualTo(GameStatus.ENDED_WITHOUT_WINNER);
    }

    @Test
    void shouldGameEndWithPlayerWinAfterMove() {
        int row = 0;
        int column = 1;
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'X', '-', 'X'},
                {'X', 'O', 'X'},
                {'O', 'X', 'O'}
        };
        Game game = getSampleGame(LocalDateTime.now(), PLAYER, GameStatus.STARTED, gameId, board);
        when(gameRepository.getGameState(USER_NAME, gameId)).thenReturn(game);

        Game afterMoveGame = gameLogicService.move(USER_NAME, gameId, row, column);

        assertThat(isArrayContainsValue(afterMoveGame.getBoard(), '-')).isFalse();
        assertThat(afterMoveGame.getStatus()).isEqualTo(GameStatus.ENDED_WITH_WINNER);
        assertThat(afterMoveGame.getCurrentPlayer().getUsername()).isEqualTo(PLAYER.getUsername());

    }

    @Test
    void shouldScoreHasToCalculatedAfterWin() {
        int row = 0;
        int column = 1;
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'X', '-', 'X'},
                {'X', 'O', 'X'},
                {'O', 'X', 'O'}
        };
        Game game = getSampleGame(LocalDateTime.now(), PLAYER, GameStatus.STARTED, gameId, board);

        when(gameRepository.getGameState(USER_NAME, gameId)).thenReturn(game);

        Game afterMoveGame = gameLogicService.move(USER_NAME, gameId, row, column);

        verify(scoreService, times(1)).calculate(game);
        verify(scoreService, times(1)).save(
                Score.builder().username(USER_NAME).score(game.getScore()).build()
        );

        assertThat(afterMoveGame.getStatus()).isEqualTo(GameStatus.ENDED_WITH_WINNER);

    }

    @Test
    void shouldGameEndWithPlayerLoseAfterMove() {
        int row = 1;
        int column = 2;
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'O', '-', 'O'},
                {'X', '-', '-'},
                {'X', '-', 'X'}
        };
        Game game = getSampleGame(LocalDateTime.now(), PLAYER, GameStatus.STARTED, gameId, board);
        when(gameRepository.getGameState(USER_NAME, gameId)).thenReturn(game);
        Game afterMoveGame = gameLogicService.move(USER_NAME, gameId, row, column);

        assertThat(afterMoveGame.getStatus()).isEqualTo(GameStatus.ENDED_WITH_WINNER);
        assertThat(afterMoveGame.getCurrentPlayer().getUsername()).isEqualTo(GameConstant.COMPUTER.getUsername());

    }

    @Test
    void shouldThrowExceptionForInvalidRowOrColumns() {
        int row = 0;
        int column = 0;
        String gameId = "gameId";
        Character[][] board = new Character[][]{
                {'O', '-', 'O'},
                {'X', '-', '-'},
                {'X', '-', 'X'}
        };
        Game game = getSampleGame(LocalDateTime.now(), PLAYER, GameStatus.STARTED, gameId, board);
        when(gameRepository.getGameState(USER_NAME, gameId)).thenReturn(game);
        Assertions.assertThatThrownBy(() -> {
            gameLogicService.move(USER_NAME, gameId, row, column);
        }).isInstanceOf(InvalidRowOrColumnException.class).hasMessage(
                String.format("row=%d or col=%d is not correct, user name=%s game id=%s", row, column, USER_NAME, game.getGameId())
        );
    }


    private Game getSampleGame(LocalDateTime startTime, Player player, GameStatus status, String gameID) {
        return Game.builder()
                .gameId(gameID)
                .board(GameConstant.DEFAULT_BOARD)
                .status(status)
                .player(player)
                .gameStartedTime(startTime)
                .currentPlayer(player)
                .build();
    }

    private Game getSampleGame(LocalDateTime startTime, Player player, GameStatus status, String gameID, Character[][] board) {
        return Game.builder()
                .gameId(gameID)
                .board(board)
                .status(status)
                .player(player)
                .gameStartedTime(startTime)
                .currentPlayer(player)
                .build();
    }

    private boolean isArrayContainsValue(Character[][] board, char marker) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == marker) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }


}
