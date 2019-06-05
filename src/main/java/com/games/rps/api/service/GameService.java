package com.games.rps.api.service;

import com.games.rps.api.config.ApplicationConfig;
import com.games.rps.api.dto.HumanMoveDto;
import com.games.rps.api.exception.GameIsNotActiveException;
import com.games.rps.api.exception.MaxRoundsReachedException;
import com.games.rps.api.model.Game;
import com.games.rps.api.model.Move;
import com.games.rps.api.model.Result;
import com.games.rps.api.model.Round;
import com.games.rps.api.repo.GameRepository;
import com.games.rps.api.repo.RoundsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.games.rps.api.model.Status.STARTED;

@Service
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final RoundsRepository roundsRepository;
    private final ResultEvaluator resultEvaluator;
    private final DecisionEngine decisionEngine;
    private final StatsService statsService;
    private final int maxRounds;

    @Autowired
    public GameService(
            GameRepository gameRepository,
            RoundsRepository roundsRepository,
            ResultEvaluator resultEvaluator,
            DecisionEngine decisionEngine,
            StatsService statsService,
            ApplicationConfig config
    ) {
        this.gameRepository = gameRepository;
        this.roundsRepository = roundsRepository;
        this.resultEvaluator = resultEvaluator;
        this.decisionEngine = decisionEngine;
        this.statsService = statsService;
        this.maxRounds = config.getMaxRounds();
    }

    private boolean isRoundsMaxedOut(String gameId) {
        return roundsRepository.size(gameId) >= maxRounds;
    }

    public Game startNew() {
        Game newGame = new Game();
        newGame.setStatus(STARTED);
        gameRepository.save(newGame);

        if (log.isDebugEnabled()) {
            log.debug("Game created: " + newGame.getId());
        }

        return newGame;
    }

    public Round playNewRound(Game game, HumanMoveDto humanMoveDto) {
        if (!STARTED.equals(game.getStatus())) {
            throw new GameIsNotActiveException(game.getId());
        }
        if (isRoundsMaxedOut(game.getId())) {
            throw new MaxRoundsReachedException(maxRounds, game.getId());
        }

        String humanPlayerId = humanMoveDto.getPlayerId();
        Move humanMove = humanMoveDto.getMove();
        Move aiMove = decisionEngine.act(humanPlayerId, humanMove);
        Result result = resultEvaluator.evaluateMoves(humanMove, aiMove);

        Round newRound = new Round(game.getId(), humanMove, aiMove, result);
        roundsRepository.add(game.getId(), newRound);
        statsService.update(humanPlayerId, result);
        return newRound;
    }

    public boolean isExists(String gameId) {
        return gameRepository.existsById(gameId);
    }

    public Optional<Game> findByIdLazy(String gameId) {
        return gameRepository.findById(gameId);
    }

    public Optional<Game> findById(String gameId) {
        Optional<Game> gameOptional = findByIdLazy(gameId);
        if (gameOptional.isPresent()) {
            gameOptional.get().setRounds(roundsRepository.getAll(gameId));
            return gameOptional;
        }
        return Optional.empty();
    }

    public void save(Game game) {
        gameRepository.save(game);
    }
}
