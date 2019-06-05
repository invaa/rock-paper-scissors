package com.games.rps.api.config;

import com.games.rps.api.controller.GameController;
import com.games.rps.api.repo.GameRepository;
import com.games.rps.api.service.GameService;
import com.games.rps.api.service.StatsService;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.mock;

@Configuration
@PropertySource("classpath:test.properties")
@ComponentScan({"com.games.rps.api.controller", "com.games.rps.api.service"})
@Import({RedisTestConfig.class, ReposMockConfig.class})
public class TestConfig {
    @Bean
    public GameController gameController(GameService gameService) {
        return new GameController(gameService);
    }

    @Primary
    @Bean(name = "statsServiceMock")
    public StatsService statsServiceMock() {
        return mock(StatsService.class);
    }

    @Primary
    @Bean(name = "gameServiceMock")
    public GameService gameServiceMock() {
        return mock(GameService.class);
    }

    @Primary
    @Bean(name = "gameRepositoryMock")
    public GameRepository gameRepositoryMock() {
        return mock(GameRepository.class);
    }

    @Primary
    @Bean(name = "applicationConfigMock")
    public ApplicationConfig applicationConfigMock() {
        return mock(ApplicationConfig.class);
    }
}
