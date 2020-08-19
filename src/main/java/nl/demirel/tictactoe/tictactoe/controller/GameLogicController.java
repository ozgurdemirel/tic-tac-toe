package nl.demirel.tictactoe.tictactoe.controller;

import io.swagger.annotations.ApiOperation;
import nl.demirel.tictactoe.tictactoe.common.RestResponse;
import nl.demirel.tictactoe.tictactoe.common.RestResponseFactory;
import nl.demirel.tictactoe.tictactoe.domain.Game;
import nl.demirel.tictactoe.tictactoe.domain.Score;
import nl.demirel.tictactoe.tictactoe.service.GameLogicService;
import nl.demirel.tictactoe.tictactoe.service.ScoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameLogicController {

    private final GameLogicService gameLogicService;
    private final ScoreService scoreService;
    private final RestResponseFactory responseFactory;

    public GameLogicController(GameLogicService gameLogicService, ScoreService scoreService, RestResponseFactory responseFactory) {
        this.gameLogicService = gameLogicService;
        this.scoreService = scoreService;
        this.responseFactory = responseFactory;
    }

    @PatchMapping("/executeTurn/{userName}/{gameId}")
    @ApiOperation(
            httpMethod = "PATCH",
            value = "Resource to make move ",
            notes = "method return with status if status is win then current user is winner")
    public RestResponse<Game> move(
            @PathVariable String userName,
            @PathVariable String gameId,
            @RequestParam int row,
            @RequestParam int column
    ) {
        //if status = ENDED_WITH_WINNER, current user is winner whether its computer or player
        return responseFactory.info("moveExecuted", gameLogicService.move(userName, gameId, row, column));
    }


    @PostMapping("/startNewGame")
    @ApiOperation(
            httpMethod = "POST",
            value = "Resource to start new game with user name",
            notes = "start game initialization of first game state")
    public RestResponse<Game> startNewGame(@RequestParam String userName) {
        return responseFactory.info("gameStarted", gameLogicService.initializeGameBoard(userName));
    }

    @GetMapping("/scoreBoard")
    @ApiOperation(
            httpMethod = "GET",
            value = "Resource to get Top 10 score")
    public RestResponse<List<Score>> scores() {
        return responseFactory.info("scoreBoard", scoreService.getScores());
    }


}
