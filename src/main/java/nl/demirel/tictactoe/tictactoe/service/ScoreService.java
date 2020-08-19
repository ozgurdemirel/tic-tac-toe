package nl.demirel.tictactoe.tictactoe.service;


import nl.demirel.tictactoe.tictactoe.domain.GameConstant;
import nl.demirel.tictactoe.tictactoe.domain.Score;
import nl.demirel.tictactoe.tictactoe.domain.Game;
import nl.demirel.tictactoe.tictactoe.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public void save(Score score) {
        scoreRepository.saveScore(score);
    }

    public List<Score> getScores() {
        return scoreRepository.getScores();
    }

    //The score is calculated based on the lowest number of moves combined with the duration of the game
    public long calculate(Game game) {
        long score = GameConstant.MAX_SCORE;
        if (game.checkWinner() && !game.getCurrentPlayer().getUsername().equals(GameConstant.COMPUTER.getUsername())) {
            long moveCount = moveCount(game);
            long durationTime = durationTime(game);
            score = score / moveCount;
            score = score / durationTime;
            return score;
        }
        return 0;
    }

    private long moveCount(Game game) {
        long moveCount = 0;
        Character[][] board = game.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == GameConstant.PLAYER_MARKER) {
                    moveCount++;
                }
            }
        }
        return moveCount;
    }

    private long durationTime(Game game) {
        return Duration.between(game.getGameStartedTime(), game.getGameEndedTime()).toSeconds();
    }

}
