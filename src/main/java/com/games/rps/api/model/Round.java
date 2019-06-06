package com.games.rps.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Round {
    private String gameId;
    private Move humanMove;
    private Move aiMove;
    private Result result;
}
