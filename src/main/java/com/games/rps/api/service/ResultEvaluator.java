package com.games.rps.api.service;

import com.games.rps.api.model.Move;
import com.games.rps.api.model.Result;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResultEvaluator {
	public Result evaluateMoves(Move playerOneMove, Move playerTwoMove) {
		if (!Optional.ofNullable(playerOneMove).isPresent()) {
			return Result.LOSE;
		}
		if (playerOneMove.equals(playerTwoMove)) {
			return Result.DRAW;
		}
		else if (playerTwoMove.losesTo().equals(playerOneMove)){
			return Result.WIN;
		}
		return Result.LOSE;
	}
}
