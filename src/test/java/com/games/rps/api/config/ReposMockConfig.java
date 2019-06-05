package com.games.rps.api.config;

import com.games.rps.api.repo.ProbabilitiesRepository;
import com.games.rps.api.repo.RoundsRepository;
import com.games.rps.api.repo.StatsRepository;
import com.games.rps.api.service.StatsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
public class ReposMockConfig {
    @Primary
    @Bean(name = "statsServiceMock")
    public StatsService statsServiceMock() {
        return mock(StatsService.class);
    }

    @Primary
    @Bean(name = "roundsRepositoryMock")
    public RoundsRepository roundsRepositoryMock() {
        return mock(RoundsRepository.class);
    }

    @Primary
    @Bean(name = "probabilitiesRepositoryMock")
    public ProbabilitiesRepository probabilitiesRepositoryMock() {
        return mock(ProbabilitiesRepository.class);
    }

    @Primary
    @Bean(name = "statsRepositoryMock")
    public StatsRepository statsRepositoryMock() {
        return mock(StatsRepository.class);
    }
}
