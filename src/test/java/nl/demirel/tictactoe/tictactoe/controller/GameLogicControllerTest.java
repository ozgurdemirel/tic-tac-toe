package nl.demirel.tictactoe.tictactoe.controller;

import nl.demirel.tictactoe.tictactoe.common.exception.InvalidRowOrColumnException;
import nl.demirel.tictactoe.tictactoe.domain.Game;
import nl.demirel.tictactoe.tictactoe.domain.GameConstant;
import nl.demirel.tictactoe.tictactoe.domain.GameStatus;
import nl.demirel.tictactoe.tictactoe.domain.Player;
import nl.demirel.tictactoe.tictactoe.service.GameLogicService;
import nl.demirel.tictactoe.tictactoe.service.ScoreService;
import nl.demirel.tictactoe.tictactoe.common.ResponseType;
import nl.demirel.tictactoe.tictactoe.common.RestResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {GameLogicController.class, RestResponseFactory.class})
class GameLogicControllerTest {

    private static final String USER_NAME = "user name";
    private static final String GAME_ID = "RANDOM_ID";
    private static final LocalDateTime START_TIME = LocalDateTime.of(2019, 4, 4, 20, 20, 20);
    private static final Player PLAYER = Player.builder().username(USER_NAME).marker(GameConstant.PLAYER_MARKER).build();

    @MockBean
    private GameLogicService gameLogicService;

    @MockBean
    private ScoreService scoreService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldStartNewGame() throws Exception {

        Game game = getSampleGame(START_TIME, PLAYER, GameStatus.STARTED);

        when(gameLogicService.initializeGameBoard(USER_NAME)).thenReturn(game);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/startNewGame")
                        .param("userName", USER_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("msgId").value("gameStarted"))
                .andExpect(jsonPath("type").value(ResponseType.INFO.name()))
                .andExpect(jsonPath("text").isNotEmpty())
                .andExpect(jsonPath("data", notNullValue()))
                .andExpect(jsonPath("data.gameId").value(GAME_ID))
                .andExpect(jsonPath("data.status").value(GameStatus.STARTED.name()))
                .andExpect(jsonPath("data.gameStartedTime").value(START_TIME.toString()))
                .andExpect(jsonPath("data.currentPlayer.username").value(USER_NAME));
    }

    @Test
    void shouldMove() throws Exception {
        String url = String.format("/executeTurn/%s/%s", USER_NAME, GAME_ID);
        int markedRow = 1;
        int markedColumn = 1;
        Game gameMarked = getSampleGame(START_TIME, PLAYER, GameStatus.STARTED);
        gameMarked.placeMark(1, 1, GameConstant.PLAYER_MARKER);

        when(gameLogicService.move(USER_NAME, GAME_ID, markedRow, markedColumn)).thenReturn(gameMarked);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .param("row", String.valueOf(markedRow))
                        .param("column", String.valueOf(markedColumn))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("msgId").value("moveExecuted"))
                .andExpect(jsonPath("type").value(ResponseType.INFO.name()))
                .andExpect(jsonPath("$.data.board[1][1]").value(Character.toString(GameConstant.PLAYER_MARKER)))
        ;
    }

    @Test
    void shouldThrowExceptionOnMove() throws Exception {
        String url = String.format("/executeTurn/%s/%s", USER_NAME, GAME_ID);
        int markedRow = 111;
        int markedColumn = 111;
        Game game = getSampleGame(START_TIME, PLAYER, GameStatus.STARTED);
        String errorMessage = String.format("row=%d or col=%d is not correct, user name=%s game id=%s", markedRow, markedColumn, USER_NAME, game.getGameId());
        when(gameLogicService.move(USER_NAME, GAME_ID, markedRow, markedColumn)).thenThrow(new InvalidRowOrColumnException(USER_NAME, game, markedRow, markedColumn));
        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .param("row", String.valueOf(markedRow))
                        .param("column", String.valueOf(markedColumn))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("type").value(ResponseType.ERROR.name()))
                .andExpect(jsonPath("msgId").value(errorMessage))
        ;
    }

    private Game getSampleGame(LocalDateTime startTime, Player player, GameStatus status) {
        return Game.builder()
                .gameId(GAME_ID)
                .board(GameConstant.DEFAULT_BOARD)
                .status(status)
                .player(player)
                .gameStartedTime(startTime)
                .currentPlayer(player)
                .build();
    }

}
