package nl.demirel.tictactoe.tictactoe.service;

import nl.demirel.tictactoe.tictactoe.domain.GameConstant;
import nl.demirel.tictactoe.tictactoe.domain.GameStatus;
import nl.demirel.tictactoe.tictactoe.domain.Player;
import nl.demirel.tictactoe.tictactoe.domain.Score;
import nl.demirel.tictactoe.tictactoe.common.exception.InvalidRowOrColumnException;
import nl.demirel.tictactoe.tictactoe.domain.Game;
import nl.demirel.tictactoe.tictactoe.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class GameLogicService {

    private final GameRepository gameRepository;
    private final ScoreService scoreService;

    public GameLogicService(GameRepository gameRepository, ScoreService scoreService) {
        this.gameRepository = gameRepository;
        this.scoreService = scoreService;
    }

    public Game initializeGameBoard(String username) {
        Character[][] board = new Character[][]{
                {'-', '-', '-'},
                {'-', '-', '-'},
                {'-', '-', '-'}
        };
        Player player = Player.builder().username(username).marker(GameConstant.PLAYER_MARKER).build();
        Game game = Game.builder()
                .gameId(UUID.randomUUID().toString())
                .board(board)
                .status(GameStatus.STARTED)
                .player(player)
                .gameStartedTime(LocalDateTime.now())
                .currentPlayer(player)
                .build();
        gameRepository.saveGameState(username, game);
        return game;
    }

    public Game move(String userName, String gameId, int row, int col) {
        Game game = getGame(userName, gameId);
        placeMark(userName, game, row, col);
        if (checkGameStatus(userName, game)) {
            return game;
        }
        changePlayer(userName, game); // MOVE currentPlayer to Computer
        //COMPUTER START
        //make auto move for computer
        makeDummyMove(game);
        //im checking winner again for in case of computer win!
        if (checkGameStatus(userName, game)) {
            return game;
        }
        //COMPUTER END
        changePlayer(userName, game);

        return game;
    }

    private boolean checkGameStatus(String userName, Game game) {
        if (!game.checkWinner() && game.isBoardFull()) {
            completeGame(userName, game, GameStatus.ENDED_WITHOUT_WINNER);
            return true;
        }
        if (game.checkWinner()) {
            completeGame(userName, game, GameStatus.ENDED_WITH_WINNER);
            return true;
        }
        return false;
    }

    private Game getGame(String username, String gameId) {
        return gameRepository.getGameState(username, gameId);
    }

    private void placeMark(String username, Game game, int row, int col) {
        boolean isMarkPlaced = game.placeMark(row, col, game.getCurrentPlayer().getMarker());
        if (!isMarkPlaced) {
            throw new InvalidRowOrColumnException(username, game, row, col);
        }
        gameRepository.saveGameState(username, game);
    }

    private void changePlayer(String username, Game game) {
        game.changePlayer();
        gameRepository.saveGameState(username, game);
    }

    private void completeGame(String username, Game game, GameStatus status) {
        game.setStatus(status);
        game.setGameEndedTime(LocalDateTime.now());
        game.setScore(scoreService.calculate(game));
        scoreService.save(Score.builder().username(username).score(game.getScore()).build());
        gameRepository.saveGameState(username, game);
    }

    // dummy implementation for computer move
    // place marker for first room on iteration found
    private void makeDummyMove(Game game) {
        Character[][] board = game.getBoard();
        boolean isMarked = Boolean.FALSE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == GameConstant.DEFAULT_MARKER) {
                    isMarked = game.placeMark(i, j, GameConstant.COMPUTER_MARKER);
                    break;
                }
            }
            if (isMarked) break;
        }
    }


}
