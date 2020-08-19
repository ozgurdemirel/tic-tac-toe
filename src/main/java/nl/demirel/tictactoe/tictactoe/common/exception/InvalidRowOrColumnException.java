package nl.demirel.tictactoe.tictactoe.common.exception;

import nl.demirel.tictactoe.tictactoe.domain.Game;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
public class InvalidRowOrColumnException extends RuntimeException {

    public InvalidRowOrColumnException(String userName, Game game, int row, int col) {
        super(String.format("row=%d or col=%d is not correct, user name=%s game id=%s", row, col, userName, game.getGameId()));
    }

}
