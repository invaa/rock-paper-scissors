package com.games.rps.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.games.rps.api.model.Move.*;
import static com.games.rps.api.model.Result.*;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ResultEvaluatorTest {

    private final ResultEvaluator resultEvaluator = new ResultEvaluator();

    @Test
    public void shouldReturnDrawWhenRockRock() {
        // given when then
        assertTrue(DRAW.equals(resultEvaluator.evaluateMoves(ROCK, ROCK)));
    }

    @Test
    public void shouldReturnLoseWhenRockPaper() {
        // given when then
        assertTrue(LOSE.equals(resultEvaluator.evaluateMoves(ROCK, PAPER)));
    }

    @Test
    public void shouldReturnWinWhenRockScissors() {
        // given when then
        assertTrue(WIN.equals(resultEvaluator.evaluateMoves(ROCK, SCISSORS)));
    }

    @Test
    public void shouldReturnWinWhenPaperRock() {
        // given when then
        assertTrue(WIN.equals(resultEvaluator.evaluateMoves(PAPER, ROCK)));
    }

    @Test
    public void shouldReturnLoseWhenPaperPaper() {
        // given when then
        assertTrue(DRAW.equals(resultEvaluator.evaluateMoves(PAPER, PAPER)));
    }

    @Test
    public void shouldReturnWinWhenPaperScissors() {
        // given when then
        assertTrue(LOSE.equals(resultEvaluator.evaluateMoves(PAPER, SCISSORS)));
    }

    @Test
    public void shouldReturnWinWhenScissorsRock() {
        // given when then
        assertTrue(LOSE.equals(resultEvaluator.evaluateMoves(SCISSORS, ROCK)));
    }

    @Test
    public void shouldReturnLoseWhenScissorsPaper() {
        // given when then
        assertTrue(WIN.equals(resultEvaluator.evaluateMoves(SCISSORS, PAPER)));
    }

    @Test
    public void shouldReturnWinWhenScissorsScissors() {
        // given when then
        assertTrue(DRAW.equals(resultEvaluator.evaluateMoves(SCISSORS, SCISSORS)));
    }

    @Test
    public void shouldReturnLoseWhenHumanMoveIsNull() {
        // given when then
        assertTrue(LOSE.equals(resultEvaluator.evaluateMoves(null, SCISSORS)));
    }

    @Test
    public void shouldReturnLoseWhenBothNull() {
        // given when then
        assertTrue(LOSE.equals(resultEvaluator.evaluateMoves(null, null)));
    }

}