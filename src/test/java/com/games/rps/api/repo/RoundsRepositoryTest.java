package com.games.rps.api.repo;

import com.games.rps.api.config.ApplicationConfig;
import com.games.rps.api.model.Round;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static com.games.rps.api.model.Move.ROCK;
import static com.games.rps.api.model.Result.DRAW;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoundsRepositoryTest {

    @Mock
    private ApplicationConfig applicationConfigMock;
    @Mock
    private StringRedisTemplate stringRedisTemplateMock;
    @Mock
    private BoundListOperations boundListOperationsMock;

    @InjectMocks
    private RoundsRepository roundsRepository;

    @Test
    public void shouldGetSize() {
        // given
        String key = "v1:uid:644d7115-42f7-4c79-927e-04e717586614:rounds";
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";

        when(stringRedisTemplateMock.boundListOps(key)).thenReturn(boundListOperationsMock);
        when(boundListOperationsMock.size()).thenReturn(1L);

        // when
        long size = roundsRepository.size(gameId);

        // then
        assertEquals(1L, size);
    }

    @Test
    public void shouldAdd() {
        // given
        String key = "v1:uid:644d7115-42f7-4c79-927e-04e717586614:rounds";
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        Round round = new Round("1", ROCK, ROCK, DRAW);
        String roundString = "{\"result\":\"DRAW\",\"gameId\":\"1\",\"humanMove\":\"ROCK\",\"aiMove\":\"ROCK\"}";

        when(stringRedisTemplateMock.boundListOps(key)).thenReturn(boundListOperationsMock);
        when(boundListOperationsMock.rightPush(roundString)).thenReturn(100L);

        // when
        roundsRepository.add(gameId, round);

        // then
        verify(boundListOperationsMock, times(1)).rightPush(anyString());
    }

    @Test
    public void shouldGetAll() {
        // given
        String key = "v1:uid:644d7115-42f7-4c79-927e-04e717586614:rounds";
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        ReflectionTestUtils.setField(roundsRepository, "maxRounds", 10);

        String round = "{\"gameId\": \"037335b8-acd3-43a4-b51e-517833cebf89\", \"humanMove\": \"SCISSORS\", \"aiMove\": \"ROCK\", \"result\": \"LOSE\"}";
        List<String> rounds = Arrays.asList(round, round, round);

        when(stringRedisTemplateMock.boundListOps(key)).thenReturn(boundListOperationsMock);
        when(boundListOperationsMock.range(0, 10)).thenReturn(rounds);

        // when
        List<Round> returnedRounds = roundsRepository.getAll(gameId);

        // then
        assertEquals(rounds.size(), returnedRounds.size());
    }
}
