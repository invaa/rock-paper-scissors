package com.games.rps.api.service;

import com.games.rps.api.model.Move;
import com.games.rps.api.repo.ProbabilitiesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.games.rps.api.model.Move.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarkovDecisionEngineTest {

    @Mock
    private ProbabilitiesRepository probabilitiesRepositoryMock;

    @InjectMocks
    private MarkovDecisionEngine markovDecisionEngine;

    @Test
    public void shouldReturnRandomMoveWhenNoStatsAndSaveStats() {
        // given
        String playerId = "player1";
        when(probabilitiesRepositoryMock.hasNoGamesPlayed(playerId)).thenReturn(true);

        // when
        Move predictedMove = markovDecisionEngine.act(playerId, ROCK);

        // then
        assertNotNull(predictedMove);
        verify(probabilitiesRepositoryMock).updateChain(playerId, ROCK);
    }

    @Test
    public void shouldReturnPredictedMoveWhenHasStatsForPaper() {
        // given
        String playerId = "player1";
        when(probabilitiesRepositoryMock.hasNoGamesPlayed(playerId)).thenReturn(false);
        when(probabilitiesRepositoryMock.getChain(playerId)).thenReturn(new Long[]{100L,0L,0L});

        // when
        Move predictedMove = markovDecisionEngine.act(playerId, SCISSORS);

        // then
        assertEquals(predictedMove, PAPER);
        verify(probabilitiesRepositoryMock).updateChain(playerId, SCISSORS);
    }

    @Test
    public void shouldReturnPredictedMoveWhenHasStatsForScissors() {
        // given
        String playerId = "player1";
        when(probabilitiesRepositoryMock.hasNoGamesPlayed(playerId)).thenReturn(false);
        when(probabilitiesRepositoryMock.getChain(playerId)).thenReturn(new Long[]{0L,100L,0L});

        // when
        Move predictedMove = markovDecisionEngine.act(playerId, PAPER);

        // then
        assertEquals(predictedMove, SCISSORS);
        verify(probabilitiesRepositoryMock).updateChain(playerId, PAPER);
    }

    @Test
    public void shouldReturnPredictedMoveWhenHasStatsForRock() {
        // given
        String playerId = "player1";
        when(probabilitiesRepositoryMock.hasNoGamesPlayed(playerId)).thenReturn(false);
        when(probabilitiesRepositoryMock.getChain(playerId)).thenReturn(new Long[]{100L,0L,0L});

        // when
        Move predictedMove = markovDecisionEngine.act(playerId, ROCK);

        // then
        assertEquals(predictedMove, PAPER);
        verify(probabilitiesRepositoryMock).updateChain(playerId, ROCK);
    }

    @Test
    public void shouldReturnPredictedMoveWhenHumanMoveIsNull() {
        // given
        String playerId = "player1";
        when(probabilitiesRepositoryMock.hasNoGamesPlayed(playerId)).thenReturn(false);
        when(probabilitiesRepositoryMock.getChain(playerId)).thenReturn(new Long[]{100L,0L,0L});

        // when
        Move predictedMove = markovDecisionEngine.act(playerId, null);

        // then
        assertEquals(predictedMove, PAPER);
        verify(probabilitiesRepositoryMock).updateChain(playerId, null);
    }
}