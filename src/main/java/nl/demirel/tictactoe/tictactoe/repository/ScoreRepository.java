package nl.demirel.tictactoe.tictactoe.repository;

import nl.demirel.tictactoe.tictactoe.domain.Score;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ScoreRepository {

    private List<Score> scores = Collections.synchronizedList(new ArrayList());

    public List<Score> saveScore(Score score) {
        //if score is bigger then top 10
        synchronized (scores) {
            scores.add(score);
            scores = this.scores.stream().sorted().collect(Collectors.toList());
            if (scores.size() > 10) {
                scores.remove(scores.size() - 1);
            }
        }
        return scores;
    }

    public synchronized List<Score> getScores() {
        return scores;
    }
}
