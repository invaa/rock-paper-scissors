package com.games.rps.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.games.rps.api.config.TestConfig;
import com.games.rps.api.dto.HumanMoveDto;
import com.games.rps.api.model.Game;
import com.games.rps.api.model.Round;
import com.games.rps.api.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;

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
@Slf4j
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

    private static HandlerExceptionResolver getExceptionResolver() {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(ExceptionHandlerConfiguration.class).resolveMethod(exception);

                log.debug(String.format("Handling exception %s by %s", exception.getMessage(), method.toString()));
                return new ServletInvocableHandlerMethod(new ExceptionHandlerConfiguration(), method);
            }
        };
        resolver.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        resolver.afterPropertiesSet();
        return resolver;
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

    public static String convertToJsonString(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());
        return objectMapper.writeValueAsString(o);
    }
}
