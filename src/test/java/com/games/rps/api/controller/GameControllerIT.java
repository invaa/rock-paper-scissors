package com.games.rps.api.controller;

import com.games.rps.api.config.TestConfig;
import com.games.rps.api.dto.HumanMoveDto;
import com.games.rps.api.model.Game;
import com.games.rps.api.model.Round;
import com.games.rps.api.service.GameService;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.games.rps.api.TestUtil.convertToJsonString;
import static com.games.rps.api.TestUtil.getExceptionResolver;
import static com.games.rps.api.model.Move.ROCK;
import static com.games.rps.api.model.Result.DRAW;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public class GameControllerIT {

    @Autowired
    @Qualifier("gameServiceMock")
    private GameService gameServiceMock;

    @Autowired
    private GameController gameController;

    private MockMvc mockMvc;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController)
                .setHandlerExceptionResolvers(getExceptionResolver())
                .build();
    }

    @After
    public void resetMocks() {
        Mockito.reset(gameServiceMock);
    }

    @Test
    public void shouldStartNewGame() throws Exception {
        // given
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        Game game = new Game();
        ReflectionTestUtils.setField(game, "id", gameId);
        when(gameServiceMock.startNew()).thenReturn(game);

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/games/")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String content = response.getContentAsString();

        // then
        verify(gameServiceMock).startNew();
        assertEquals(content, convertToJsonString(game));
    }

    @Test
    public void shouldPlayNewRound() throws Exception {
        // given
        String playerId = "player1";
        HumanMoveDto humanMoveDto = new HumanMoveDto();
        humanMoveDto.setPlayerId(playerId);
        humanMoveDto.setMove(ROCK);
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        Game game = new Game();
        ReflectionTestUtils.setField(game, "id", gameId);
        Round round = new Round(playerId, humanMoveDto.getMove(), ROCK, DRAW);

        when(gameServiceMock.findByIdLazy(gameId)).thenReturn(Optional.of(game));
        when(gameServiceMock.playNewRound(game, humanMoveDto)).thenReturn(round);

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/games/{id}", gameId)
                .content(convertToJsonString(humanMoveDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String content = response.getContentAsString();

        // then
        verify(gameServiceMock).playNewRound(any(), any());
        assertEquals(content, convertToJsonString(round));
    }

    @Test
    public void shouldThrowBadRequestWhenPlayerIdIsNull() throws Exception {
        // given
        String playerId = null;
        HumanMoveDto humanMoveDto = new HumanMoveDto();
        humanMoveDto.setPlayerId(playerId);
        humanMoveDto.setMove(ROCK);
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        Game game = new Game();
        ReflectionTestUtils.setField(game, "id", gameId);
        Round round = new Round(playerId, humanMoveDto.getMove(), ROCK, DRAW);

        when(gameServiceMock.findByIdLazy(gameId)).thenReturn(Optional.of(game));
        when(gameServiceMock.playNewRound(game, humanMoveDto)).thenReturn(round);

        // when then
        mockMvc.perform(put("/api/v1/games/{id}", gameId)
                .content(convertToJsonString(humanMoveDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void shouldThrowBadRequestWhenPlayerIdIsEmpty() throws Exception {
        // given
        String playerId = "";
        HumanMoveDto humanMoveDto = new HumanMoveDto();
        humanMoveDto.setPlayerId(playerId);
        humanMoveDto.setMove(ROCK);
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        Game game = new Game();
        ReflectionTestUtils.setField(game, "id", gameId);
        Round round = new Round(playerId, humanMoveDto.getMove(), ROCK, DRAW);

        when(gameServiceMock.findByIdLazy(gameId)).thenReturn(Optional.of(game));
        when(gameServiceMock.playNewRound(game, humanMoveDto)).thenReturn(round);

        // when then
        mockMvc.perform(put("/api/v1/games/{id}", gameId)
                .content(convertToJsonString(humanMoveDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void shouldThrowBadRequestWhenMoveEmpty() throws Exception {
        // given
        String playerId = "player1";
        HumanMoveDto humanMoveDto = new HumanMoveDto();
        humanMoveDto.setPlayerId(playerId);
        humanMoveDto.setMove(null);
        String gameId = "644d7115-42f7-4c79-927e-04e717586614";
        Game game = new Game();
        ReflectionTestUtils.setField(game, "id", gameId);
        Round round = new Round(playerId, humanMoveDto.getMove(), ROCK, DRAW);

        when(gameServiceMock.findByIdLazy(gameId)).thenReturn(Optional.of(game));
        when(gameServiceMock.playNewRound(game, humanMoveDto)).thenReturn(round);

        // when then
        mockMvc.perform(put("/api/v1/games/{id}", gameId)
                .content(convertToJsonString(humanMoveDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
