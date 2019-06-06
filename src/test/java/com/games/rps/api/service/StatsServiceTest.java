package com.games.rps.api.service;

import com.games.rps.api.dto.StatsByPlayerDto;
import com.games.rps.api.dto.StatsSearchDto;
import com.games.rps.api.model.PlayerStats;
import com.games.rps.api.repo.StatsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StatsServiceTest {

    @Mock
    private StatsRepository statsRepositoryMock;

    @InjectMocks
    private StatsService statsService;

    @Test
    public void shouldGetById() {
        // given
        String playerId = "player1";
        when(statsRepositoryMock.getById(playerId)).thenReturn(new PlayerStats(playerId));

        // when
        PlayerStats playerStats = statsService.getById(playerId);

        // then
        assertNotNull(playerStats);
        verify(statsRepositoryMock, times(1)).getById(playerId);
    }

    @Test
    public void shouldSearchStats() {
        // given
        String playerId = "player1";
        PlayerStats playerStats = new PlayerStats(playerId);
        playerStats.setRounds(10L);
        StatsSearchDto statsSearchDto = new StatsSearchDto();
        statsSearchDto.setBeginsWith("");
        statsSearchDto.setStartFrom(0L);
        statsSearchDto.setHowMany(100);
        when(statsRepositoryMock.getStats("", 0L, 100L))
                .thenReturn(Collections.singletonList(playerStats));

        // when
        StatsByPlayerDto actualStats = statsService.get(statsSearchDto);

        // then
        assertThat(reflectionEquals(playerStats, actualStats.getStats().get(0))).isTrue();
        verify(statsRepositoryMock, times(1)).getStats("", 0L, 100L);
    }
}
