package com.games.rps.api.service;

import com.games.rps.api.model.Move;
import com.games.rps.api.repo.ProbabilitiesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.games.rps.api.model.Move.*;

@Slf4j
@Service
public class MarkovDecisionEngine implements DecisionEngine {
    private final ProbabilitiesRepository probabilitiesRepository;

    @Autowired
    public MarkovDecisionEngine(ProbabilitiesRepository probabilitiesRepository) {
        this.probabilitiesRepository = probabilitiesRepository;
    }

    private Move getNextMove(String playerId) {
        if (probabilitiesRepository.hasNoGamesPlayed(playerId)) {
            return rpsSocietyBasedRandom();
        }
        Long[] chain = probabilitiesRepository.getChain(playerId);
        int numberOfChains = Move.values().length;
        int nextMoveIndex = 0;
        for (int i = 0; i < numberOfChains; i++) {
            if (chain[nextMoveIndex] < chain[i]) {
                nextMoveIndex = i;
            }
        }
        return Move.values()[nextMoveIndex].losesTo();
    }

    //according to word RPS society odd are: Rock 35.4pc, Paper 35.0pc, Scissors 29.6pc
    private Move rpsSocietyBasedRandom() {
        Random random = new Random();
        int randomInt = random.nextInt(1000);
        if (randomInt >= 0 && randomInt < 354) {
            return ROCK;
        } else if (randomInt >= 354 && randomInt < 704) {
            return PAPER;
        } else {
            return SCISSORS;
        }
    }

    private void update(String playerId, Move newMove) {
        probabilitiesRepository.updateChain(playerId, newMove);
    }

    @Override
    public Move act(String playerId, Move humanMove) {
        Move aiMove = getNextMove(playerId);
        update(playerId, humanMove);
        return aiMove;
    }
}
