package nl.demirel.tictactoe.tictactoe.repository;

import nl.demirel.tictactoe.tictactoe.domain.Score;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class ScoreRepositoryTest {

    @InjectMocks
    private ScoreRepository scoreRepository;

    @Test
    void shouldSaveTop10Score() {
        List<Score> scores = Arrays.asList(
                Score.builder().username("w").score(2l).build(),
                Score.builder().username("a").score(7l).build(),
                Score.builder().username("e").score(3l).build(),
                Score.builder().username("r").score(4l).build(),
                Score.builder().username("t").score(5l).build(),
                Score.builder().username("q").score(1l).build(),
                Score.builder().username("d").score(9l).build(),
                Score.builder().username("y").score(6l).build(),
                Score.builder().username("z").score(11l).build(),
                Score.builder().username("s").score(8l).build(),
                Score.builder().username("f").score(10l).build()
        );

        for (Score score : scores) {
            scoreRepository.saveScore(score);
        }

        assertThat(scoreRepository.getScores().size()).isEqualTo(10);
        assertThat(scoreRepository.getScores()).hasSize(10).doesNotContain(Score.builder().username("q").score(1l).build());
        assertThat(scoreRepository.getScores().get(0)).isEqualTo(Score.builder().username("z").score(11l).build());

    }


}
