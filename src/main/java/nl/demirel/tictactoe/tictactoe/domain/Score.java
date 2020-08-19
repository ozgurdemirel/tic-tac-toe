package nl.demirel.tictactoe.tictactoe.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Score implements Comparable<Score> {

    private String username;
    private Long score;

    @Override
    public int compareTo(Score o) {
        return (int) (o.score - this.score);
    }
}
