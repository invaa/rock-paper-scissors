package com.games.rps.api.controller;

import com.games.rps.api.config.TestConfig;
import com.games.rps.api.dto.StatsByPlayerDto;
import com.games.rps.api.model.PlayerStats;
import com.games.rps.api.service.StatsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.games.rps.api.TestUtil.convertToJsonString;
import static com.games.rps.api.TestUtil.getExceptionResolver;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Slf4j
public class StatsControllerIT {

    @Autowired
    @Qualifier("statsServiceMock")
    private StatsService statsServiceMock;

    @Autowired
    private StatsController statsController;

    private MockMvc mockMvc;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(statsController)
                .setHandlerExceptionResolvers(getExceptionResolver())
                .build();
    }

    @After
    public void resetMocks() {
        Mockito.reset(statsServiceMock);
    }

    @Test
    public void shouldGetStats() throws Exception {
        // given
        StatsByPlayerDto statsByPlayerDto = new StatsByPlayerDto();
        PlayerStats playerStats = new PlayerStats("player1");
        playerStats.setRounds(10L);
        statsByPlayerDto.getStats().add(playerStats);
        when(statsServiceMock.get(any())).thenReturn(statsByPlayerDto);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/stats")
                .param("startFrom", "0")
                .param("howMany", "100")
                .param("startsWith", "player1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String content = response.getContentAsString();

        // then
        verify(statsServiceMock).get(any());
        assertEquals(content, convertToJsonString(statsByPlayerDto));
    }

    @Test
    public void shouldGetStatsByPlayer() throws Exception {
        // given
        String playerId = "player1";
        PlayerStats playerStats = new PlayerStats(playerId);
        playerStats.setRounds(10L);
        when(statsServiceMock.getById(playerId)).thenReturn(playerStats);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/stats/{playerId}", playerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String content = response.getContentAsString();

        // then
        verify(statsServiceMock).getById(playerId);
        assertEquals(content, convertToJsonString(playerStats));
    }
}
