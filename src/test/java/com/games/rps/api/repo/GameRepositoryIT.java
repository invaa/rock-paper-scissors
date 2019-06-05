package com.games.rps.api.repo;

import com.games.rps.api.config.RedisTestConfig;
import com.games.rps.api.config.ReposMockConfig;
import com.games.rps.api.model.Game;
import com.games.rps.api.model.Status;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisTestConfig.class, ReposMockConfig.class})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class GameRepositoryIT {

    @Autowired
    private GameRepository repository;

    private static redis.embedded.RedisServer redisServer;

    @BeforeClass
    public static void startRedisServer() throws IOException {
        redisServer = new redis.embedded.RedisServer(6379);
        redisServer.start();
    }

    @AfterClass
    public static void stopRedisServer() throws IOException {
        redisServer.stop();
    }

    @Test
    public void shouldSaveGames() throws Exception {
        final Game game = new Game();
        repository.save(game);
        final Game savedGame = repository.findById(game.getId()).get();
        assertEquals(game.getId(), savedGame.getId());
        assertEquals(game.getStatus(), Status.STARTED);
    }

    @Test
    public void shouldUpdateGames() throws Exception {
        final Game game = new Game();
        repository.save(game);
        game.setStatus(Status.TERMINATED);
        repository.save(game);
        final Game retrievedGame = repository.findById(game.getId()).get();
        assertEquals(game.getStatus(), retrievedGame.getStatus());
    }

    @Test
    public void shouldFetchAllSavedGames() throws Exception {
        final Game gameFirst = new Game();
        final Game gameSecond = new Game();
        repository.save(gameFirst);
        repository.save(gameSecond);
        repository.findById(gameSecond.getId());
        assertTrue(repository.findById(gameFirst.getId()).isPresent());
        assertTrue(repository.findById(gameSecond.getId()).isPresent());
    }

    @Test
    public void shouldDeleteGames() throws Exception {
        final Game game = new Game();
        repository.save(game);
        repository.deleteById(game.getId());
        final Game retrievedGame = repository.findById(game.getId()).orElse(null);
        assertNull(retrievedGame);
    }
}