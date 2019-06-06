package com.games.rps.api.repo;

import com.games.rps.api.exception.ParsingException;
import com.games.rps.api.model.Move;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.games.rps.api.model.Move.*;
import static com.games.rps.api.repo.RepoKeyUtil.*;

@Repository
public class ProbabilitiesRepository {
    private final StringRedisTemplate template;
    private final RedisMap<String, String> probabilityChain;

    @Autowired
    public ProbabilitiesRepository(StringRedisTemplate template) {
        this.template = template;
        this.probabilityChain = new DefaultRedisMap<>(getChainMapName(), template);
    }

    private RedisList<String> getMoves(String playerId) {
        return new DefaultRedisList<>(getMovesListName(playerId), template);
    }

    private Long getProbability(String playerId, Move lastMove, Move currentMove) {
        String probability = probabilityChain.get(getChainKey(playerId, lastMove, currentMove));
        if (Optional.ofNullable(probability).isPresent() && !probability.isEmpty()) {
            return Long.parseLong(probability);
        } else {
            return 0L;
        }
    }

    private Long[] getChain(String playerId, Move move) {
        switch (move) {
            case ROCK:
                return new Long[]{getProbability(playerId, ROCK, ROCK), getProbability(playerId, ROCK, PAPER), getProbability(playerId, ROCK, SCISSORS)};
            case PAPER:
                return new Long[]{getProbability(playerId, PAPER, ROCK), getProbability(playerId, PAPER, PAPER), getProbability(playerId, PAPER, SCISSORS)};
            case SCISSORS:
                return new Long[]{getProbability(playerId, SCISSORS, ROCK), getProbability(playerId, SCISSORS, PAPER), getProbability(playerId, SCISSORS, SCISSORS)};
            default: {
                throw new ParsingException("Wrong last move " + move);
            }
        }
    }

    private Optional<Move> getLastMove(String playerId) {
        if (!getMoves(playerId).isEmpty()) {
            return Optional.of(Move.valueOf(getMoves(playerId).getFirst()));
        } else {
            return Optional.empty();
        }
    }

    public boolean hasNoGamesPlayed(String playerId) {
        return !getLastMove(playerId).isPresent();
    }

    public Long[] getChain(String playerId) {
        Optional<Move> lastMove = getLastMove(playerId);
        return lastMove.map(move -> getChain(playerId, move))
                .orElseGet(() -> new Long[]{0L, 0L, 0L});
    }

    public void updateChain(String playerId, Move currentMove) {
        Optional<Move> lastMove = getLastMove(playerId);
        getMoves(playerId).addFirst(currentMove.name());
        if (lastMove.isPresent()) {
            probabilityChain.increment(getChainKey(playerId, lastMove.get(), currentMove), 1L);
        } else {
            probabilityChain.putIfAbsent(getChainKey(playerId, ROCK, ROCK), "0");
            probabilityChain.putIfAbsent(getChainKey(playerId, ROCK, PAPER), "0");
            probabilityChain.putIfAbsent(getChainKey(playerId, ROCK, SCISSORS), "0");
            probabilityChain.putIfAbsent(getChainKey(playerId, PAPER, ROCK), "0");
            probabilityChain.putIfAbsent(getChainKey(playerId, PAPER, PAPER), "0");
            probabilityChain.putIfAbsent(getChainKey(playerId, PAPER, SCISSORS), "0");
            probabilityChain.putIfAbsent(getChainKey(playerId, SCISSORS, ROCK), "0");
            probabilityChain.putIfAbsent(getChainKey(playerId, SCISSORS, PAPER), "0");
            probabilityChain.putIfAbsent(getChainKey(playerId, SCISSORS, SCISSORS), "0");
        }
    }
}
