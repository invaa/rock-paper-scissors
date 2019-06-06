package com.games.rps.api.controller;

import com.games.rps.api.dto.HumanMoveDto;
import com.games.rps.api.exception.GameIsNotActiveException;
import com.games.rps.api.exception.GameNotExistsException;
import com.games.rps.api.model.Game;
import com.games.rps.api.model.Round;
import com.games.rps.api.service.GameService;
import com.games.rps.api.validator.ValidUuid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static com.games.rps.api.model.Status.STARTED;
import static com.games.rps.api.model.Status.TERMINATED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "api/v1/games")
@Slf4j
@Validated
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(method = POST, value = "", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Game startNewGame() {
        return gameService.startNew();
    }

    @RequestMapping(method = GET, value = "/{gameId}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    Game gameInfo(@PathVariable @ValidUuid String gameId) throws GameNotExistsException {
        if (!gameService.isExists(gameId)) {
            throw new GameNotExistsException(gameId);
        }
        return gameService.findById(gameId).get();
    }

    @RequestMapping(method = DELETE, value = "/{gameId}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    Game terminateGame(@PathVariable @ValidUuid String gameId) throws GameNotExistsException, GameIsNotActiveException {
        Game game = getGameByIdProtected(gameId);
        if (game.getStatus() != STARTED) {
            throw new GameIsNotActiveException(gameId);
        }
        game.setStatus(TERMINATED);
        gameService.save(game);
        return game;
    }

    @RequestMapping(method = PUT, value = "/{gameId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    Round playRound(@PathVariable @ValidUuid String gameId, @RequestBody @Valid HumanMoveDto humanMoveDto)
            throws GameNotExistsException, GameIsNotActiveException {
        return gameService.playNewRound(getGameByIdProtected(gameId), humanMoveDto);
    }

    private Game getGameByIdProtected(@ValidUuid @PathVariable String gameId) {
        Optional<Game> gameSearchResult = gameService.findByIdLazy(gameId);
        if (!gameSearchResult.isPresent()) {
            throw new GameNotExistsException(gameId);
        }
        return gameSearchResult.get();
    }
}
