package com.games.rps.api.service;

import com.games.rps.api.dto.StatsByPlayerDto;
import com.games.rps.api.dto.StatsSearchDto;
import com.games.rps.api.model.PlayerStats;
import com.games.rps.api.model.Result;
import com.games.rps.api.repo.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.games.rps.api.model.Result.LOSE;
import static com.games.rps.api.model.Result.WIN;

@Service
public class StatsService {
    private final StatsRepository statsRepository;

    @Autowired
    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Async
    public void update(String playerId, Result result) {
        if (statsRepository.isEmpty(playerId)) {
            statsRepository.addPlayer(playerId);
        }
        statsRepository.incrementGames(playerId);
        if (WIN.equals(result)) {
            statsRepository.incrementWins(playerId);
        } else if (LOSE.equals(result)) {
            statsRepository.incrementLoses(playerId);
        }
    }

    public StatsByPlayerDto get(StatsSearchDto searchDto) {
        StatsByPlayerDto stats = new StatsByPlayerDto();
        stats.getStats().addAll(statsRepository.getStats(
                searchDto.getBeginsWith(),
                searchDto.getStartFrom(),
                searchDto.getStartFrom() + searchDto.getHowMany())
        );
        return stats;
    }

    public PlayerStats getById(String playerId) {
        return statsRepository.getById(playerId);
    }
}
