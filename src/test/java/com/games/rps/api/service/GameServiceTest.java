package com.games.rps.api.service;

import com.games.rps.api.config.ApplicationConfig;
import com.games.rps.api.dto.HumanMoveDto;
import com.games.rps.api.model.Game;
import com.games.rps.api.model.Round;
import com.games.rps.api.repo.GameRepository;
import com.games.rps.api.repo.RoundsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.games.rps.api.model.Move.ROCK;
import static com.games.rps.api.model.Move.SCISSORS;
import static com.games.rps.api.model.Result.DRAW;
import static com.games.rps.api.model.Result.WIN;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepositoryMock;
    @Mock
    private RoundsRepository roundsRepositoryMock;
    @Mock
    private ResultEvaluator resultEvaluatorMock;
    @Mock
    private DecisionEngine decisionEngineMock;
    @Mock
    private StatsService statsServiceMock;
    @Mock
    private ApplicationConfig configMock;

    @InjectMocks
    private GameService gameService;

    @Test
    public void shouldNotReturnRoundsWhenFindByIdLazy() {
        // given
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(new Game()));

        // when
        Optional<Game> gameOptional = gameService.findByIdLazy(gameId);

        // then
        assertNull(gameOptional.get().getRounds());
    }

    @Test
    public void shouldReturnRoundsWhenFindById() {
        // given
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(new Game()));

        List<Round> rounds = Arrays.asList(
                new Round("1", ROCK, ROCK, DRAW),
                new Round("1", ROCK, SCISSORS, WIN)
                );
        when(roundsRepositoryMock.getAll(gameId)).thenReturn(rounds);

        // when
        Optional<Game> gameOptional = gameService.findById(gameId);

        // then
        assertTrue(gameOptional.get().getRounds().size() == 2);
    }

    @Test
    public void shouldStartNew() {
        // given
        Game game = new Game();
        when(gameRepositoryMock.save(game)).thenReturn(game);

        // when
        Game actualGame = gameService.startNew();

        // then
        assertEquals(game, actualGame);
    }

    @Test
    public void shouldSave() {
        // given
        Game game = new Game();
        when(gameRepositoryMock.save(game)).thenReturn(game);

        // when
        gameService.save(game);

        // then
        verify(gameRepositoryMock, times(1)).save(game);
    }

    @Test
    public void shouldStartNewRound() {
        // given
        String playerId = "player1";
        HumanMoveDto humanMoveDto = new HumanMoveDto();
        humanMoveDto.setPlayerId(playerId);
        humanMoveDto.setMove(ROCK);
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        Game game = new Game();
        ReflectionTestUtils.setField(game, "id", gameId);
        ReflectionTestUtils.setField(gameService, "maxRounds", 10);
        Round expectedRound = new Round(gameId, ROCK, ROCK, DRAW);
        when(decisionEngineMock.act(playerId, humanMoveDto.getMove())).thenReturn(ROCK);
        when(resultEvaluatorMock.evaluateMoves(ROCK, ROCK)).thenReturn(DRAW);

        // when
        Round actualRound = gameService.playNewRound(game, humanMoveDto);

        // then
        assertThat(reflectionEquals(expectedRound, actualRound)).isTrue();
    }

}
