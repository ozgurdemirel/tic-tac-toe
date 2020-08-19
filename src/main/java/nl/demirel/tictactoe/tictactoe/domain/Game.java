package nl.demirel.tictactoe.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class Game {

    private String gameId;

    private Character[][] board;

    private Player player;

    private Player currentPlayer;

    private GameStatus status;

    private LocalDateTime gameStartedTime;

    private LocalDateTime gameEndedTime;

    private Long score;

    public boolean placeMark(int row, int col, char currentPlayerMark) {
        if (isBoardFull()) {
            return false;
        }

        boolean isColAndRowInBound = (row >= 0) && (row < 3) && (col >= 0) && (col < 3);
        if (!isColAndRowInBound) {
            return false;
        }

        if (board[row][col] == GameConstant.DEFAULT_MARKER) {
            board[row][col] = currentPlayerMark;
            return true;
        }
        return false;
    }

    public void changePlayer() { //toggle for user change
        if (currentPlayer.getMarker() == GameConstant.PLAYER_MARKER) {
            currentPlayer = GameConstant.COMPUTER;
        } else {
            currentPlayer = player;
        }
    }

    public boolean isBoardFull() {
        boolean isFull = Boolean.TRUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == GameConstant.DEFAULT_MARKER) {
                    isFull = Boolean.FALSE;
                    break;
                }
            }
        }
        return isFull;
    }

    public boolean checkWinner() {
        return checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin();
    }

    private boolean checkRowsForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[i][0], board[i][1], board[i][2]) == Boolean.TRUE) {
                return true;
            }
        }
        return false;
    }


    private boolean checkColumnsForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[0][i], board[1][i], board[2][i]) == Boolean.TRUE) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalsForWin() {
        return (checkRowCol(board[0][0], board[1][1], board[2][2]) == Boolean.TRUE)
                ||
                (checkRowCol(board[0][2], board[1][1], board[2][0]) == Boolean.TRUE);
    }


    private boolean checkRowCol(char c1, char c2, char c3) {
        return ((c1 != GameConstant.DEFAULT_MARKER) && (c1 == c2) && (c2 == c3));
    }

}
