package com.games.rps.api.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.Size;

@Data
public class PlayerStats {
    @Setter(value = AccessLevel.PRIVATE)
    @Size(max = 20)
    private String playerId;
    private Long rounds;
    private Long wins;
    private Long loses;
    private Long draws;

    public PlayerStats(String playerId) {
        this.playerId = playerId;
        this.rounds = 0L;
        this.wins = 0L;
        this.loses = 0L;
        this.draws = 0L;
    }
}
