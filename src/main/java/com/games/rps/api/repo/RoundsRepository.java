package com.games.rps.api.repo;

import com.games.rps.api.config.ApplicationConfig;
import com.games.rps.api.exception.RoundSerializationException;
import com.games.rps.api.model.Round;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.games.rps.api.repo.RepoConstants.getRoundsListName;

@Repository
public class RoundsRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StringRedisTemplate template;
    private final int maxRounds;

    @Autowired
    public RoundsRepository(StringRedisTemplate template, ApplicationConfig config) {
        this.template = template;
        this.maxRounds = config.getMaxRounds();
    }

    private RedisList<String> getRounds(String gameId) {
        return new DefaultRedisList<>(getRoundsListName(gameId), template);
    }

    public long size(String gameId) {
        return getRounds(gameId).size();
    }

    public List<Round> getAll(String gameId) {
        return getRounds(gameId).range(0, maxRounds).stream().map(round -> {
            try {
                return objectMapper.readValue(round, Round.class);
            } catch (IOException e) {
                throw new RoundSerializationException(round, e);
            }
        }).collect(Collectors.toList());
    }

    public void add(String gameId, Round round) {
        try {
            getRounds(gameId).addLast(objectMapper.writeValueAsString(round));
        } catch (IOException e) {
            throw new RoundSerializationException(round.toString(), e);
        }
    }
}
