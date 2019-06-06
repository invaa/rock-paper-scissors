package com.games.rps.api.dto;

import com.games.rps.api.model.Move;
import com.games.rps.api.validator.ValidPlayerId;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HumanMoveDto {
    @ValidPlayerId
    private String playerId;
    @NotNull
    private Move move;
}
