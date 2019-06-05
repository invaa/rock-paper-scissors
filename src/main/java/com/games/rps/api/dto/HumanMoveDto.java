package com.games.rps.api.dto;

import com.games.rps.api.model.Move;
import com.games.rps.api.validator.ValidPlayerId;
import lombok.Data;

@Data
public class HumanMoveDto {
    @ValidPlayerId
    private String playerId;
    private Move move;
}
