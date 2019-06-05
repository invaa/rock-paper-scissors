package com.games.rps.api.controller;

import com.games.rps.api.dto.HumanMoveDto;
import com.games.rps.api.exception.GameIsNotActiveException;
import com.games.rps.api.exception.GameNotExistsException;
import com.games.rps.api.model.Game;
import com.games.rps.api.model.Round;
import com.games.rps.api.service.GameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.games.rps.api.model.Move.ROCK;
import static com.games.rps.api.model.Move.SCISSORS;
import static com.games.rps.api.model.Result.WIN;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

    @Mock
    private GameService gameServiceMock;

    @InjectMocks
    private GameController gameController;

    @Test
    public void shouldPlayNewRound() {
        // given
        Game game = new Game();
        ReflectionTestUtils.setField(game, "id", "644d7115-42f7-4c79-927e-04e717586614");
        HumanMoveDto humanMoveDto = new HumanMoveDto();
        humanMoveDto.setPlayerId("player1");
        humanMoveDto.setMove(ROCK);

        Round round = new Round(game.getId(), humanMoveDto.getMove(), SCISSORS, WIN);

        when(gameServiceMock.findByIdLazy(game.getId())).thenReturn(Optional.of(game));
        when(gameServiceMock.playNewRound(game, humanMoveDto)).thenReturn(round);

        // when
        Round actualRound = gameController.playRound(game.getId(), humanMoveDto);

        // then
        assertThat(reflectionEquals(round, actualRound)).isTrue();
    }

    @Test
    public void shouldThrowGameIsNotActiveException() {
        // given
        String errorMessage = "The game with id";
        Game game = new Game();
        HumanMoveDto humanMoveDto = new HumanMoveDto();
        humanMoveDto.setPlayerId("player1");
        humanMoveDto.setMove(ROCK);

        when(gameServiceMock.findByIdLazy(game.getId())).thenReturn(Optional.of(game));
        when(gameServiceMock.playNewRound(game, humanMoveDto)).thenThrow(new GameIsNotActiveException(game.getId()));

        // when
        Throwable throwable = catchThrowable(() -> gameController.playRound(game.getId(), humanMoveDto));

        // then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(GameIsNotActiveException.class)
                .hasMessageContaining(errorMessage);
    }

    @Test
    public void shouldThrowGameNotExistException() {
        // given
        String errorMessage = "The game with id null does not exist";
        Game game = new Game();
        HumanMoveDto humanMoveDto = new HumanMoveDto();
        humanMoveDto.setPlayerId("player1");
        humanMoveDto.setMove(ROCK);

        // when
        Throwable throwable = catchThrowable(() -> gameController.playRound(game.getId(), humanMoveDto));

        // then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(GameNotExistsException.class)
                .hasMessageContaining(errorMessage);
    }
}
