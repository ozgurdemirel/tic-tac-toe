package nl.demirel.tictactoe.tictactoe.domain;

public final class GameConstant {

    public static final char PLAYER_MARKER = 'X';
    public static final char COMPUTER_MARKER = 'O';
    public static final char DEFAULT_MARKER = '-';
    public static final long MAX_SCORE = 10_000;

    public static final Character[][] DEFAULT_BOARD = new Character[][]{
            {'-', '-', '-'},
            {'-', '-', '-'},
            {'-', '-', '-'}
    };

    public static final Player COMPUTER = Player.builder().username("computer").marker(COMPUTER_MARKER).build();

    private GameConstant() {
    }
}
