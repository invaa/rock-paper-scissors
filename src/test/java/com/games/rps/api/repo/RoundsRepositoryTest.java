package com.games.rps.api.repo;

import com.games.rps.api.config.ApplicationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
}
