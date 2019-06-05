package com.games.rps.api.service;

import com.games.rps.api.model.Move;

@FunctionalInterface
public interface DecisionEngine {
    Move act(String humanPlayerId, Move humanMove);
}
