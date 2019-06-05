package com.games.rps.api.service;

import com.games.rps.api.config.ApplicationConfig;
import com.games.rps.api.model.Game;
import com.games.rps.api.model.Round;
import com.games.rps.api.repo.GameRepository;
import com.games.rps.api.repo.RoundsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.games.rps.api.model.Move.ROCK;
import static com.games.rps.api.model.Move.SCISSORS;
import static com.games.rps.api.model.Result.DRAW;
import static com.games.rps.api.model.Result.WIN;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private RoundsRepository roundsRepository;
    @Mock
    private ResultEvaluator resultEvaluator;
    @Mock
    private DecisionEngine decisionEngine;
    @Mock
    private StatsService statsService;
    @Mock
    private ApplicationConfig config;

    @InjectMocks
    private GameService gameService;

    @Test
    public void shouldNotReturnRoundsWhenFindByIdLazy() {
        // given
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(new Game()));

        // when
        Optional<Game> gameOptional = gameService.findByIdLazy(gameId);

        // then
        assertNull(gameOptional.get().getRounds());
    }

    @Test
    public void shouldReturnRoundsWhenFindById() {
        // given
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(new Game()));

        List<Round> rounds = Arrays.asList(
                new Round("1", ROCK, ROCK, DRAW),
                new Round("1", ROCK, SCISSORS, WIN)
                );
        when(roundsRepository.getAll(gameId)).thenReturn(rounds);

        // when
        Optional<Game> gameOptional = gameService.findById(gameId);

        // then
        assertTrue(gameOptional.get().getRounds().size() == 2);
    }

}
