package com.games.rps.api.repo;

import com.games.rps.api.model.PlayerStats;
import com.games.rps.api.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.games.rps.api.repo.RepoKeyUtil.getPlayersDefaultDictName;
import static com.games.rps.api.repo.RepoKeyUtil.getPlayersDictName;
import static com.games.rps.api.repo.RepoKeyUtil.getStatsMapName;
import static com.games.rps.api.repo.RepoKeyUtil.getStatsRoundsMapName;

@Repository
public class StatsRepository {
    private final StringRedisTemplate template;
    private RedisMap<String, String> wins;
    private RedisMap<String, String> loses;
    private RedisMap<String, String> rounds;
    private RedisList<String> allPlayers;

    @Autowired
    public StatsRepository(StringRedisTemplate template) {
        this.template = template;
        this.rounds = new DefaultRedisMap<>(getStatsRoundsMapName(), template);
        this.wins = new DefaultRedisMap<>(getStatsMapName(Result.WIN.name()), template);
        this.loses = new DefaultRedisMap<>(getStatsMapName(Result.LOSE.name()), template);
        this.allPlayers = new DefaultRedisList<>(getPlayersDefaultDictName(), template);
    }

    private void updateDictionary(String playerId) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playerId.length(); i++) {
            sb.append(playerId.charAt(i));
            getDictionary(sb.toString()).addLast(playerId);
        }
    }

    private RedisList<String> getDictionary(String key) {
        if (key.isEmpty())
            return allPlayers;
        return new DefaultRedisList<>(getPlayersDictName(key), template);
    }

    public void addPlayer(String playerId) {
        if (isEmpty(playerId)) {
            rounds.putIfAbsent(playerId, "0");
            wins.putIfAbsent(playerId, "0");
            loses.putIfAbsent(playerId, "0");

            allPlayers.addLast(playerId);
            updateDictionary(playerId);
        }
    }

    public Collection<PlayerStats> getStats(String part, Long from, Long to) {
        List<String> names = getDictionary(part).range(from, to);
        List<PlayerStats> list = new ArrayList<>();

        for (String playerId : names) {
            list.add(getById(playerId));
        }
        return list;
    }

    public void incrementGames(String playerId) {
        rounds.increment(playerId, 1L);
    }

    public void incrementWins(String playerId) {
        wins.increment(playerId, 1L);
    }

    public void incrementLoses(String playerId) {
        loses.increment(playerId, 1L);
    }

    public PlayerStats getById(String playerId) {
        PlayerStats playerStats = new PlayerStats(playerId);
        if (!isEmpty(playerId)) {
            playerStats.setWins(Long.parseLong(wins.get(playerId)));
            playerStats.setRounds(Long.parseLong(rounds.get(playerId)));
            playerStats.setLoses(Long.parseLong(loses.get(playerId)));
            playerStats.setDraws(playerStats.getRounds() - playerStats.getLoses() - playerStats.getWins());
        }
        return playerStats;
    }

    public boolean isEmpty(String playerId) {
        return rounds.get(playerId) == null || rounds.get(playerId).isEmpty();
    }
}
