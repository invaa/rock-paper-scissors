package com.games.rps.api.service;

import com.games.rps.api.model.Move;
import com.games.rps.api.model.Result;
import org.springframework.stereotype.Component;

@Component
public class ResultEvaluator {
	public Result evaluateMoves(Move playerOneMove, Move playerTwoMove) {
		if (playerOneMove.losesTo().equals(playerTwoMove)) {
			return Result.LOSE;
		}
		else if (playerTwoMove.losesTo().equals(playerOneMove)){
			return Result.WIN;
		}
		return Result.DRAW;
	}
}
