package com.games.rps.api.dto;

import com.games.rps.api.model.PlayerStats;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatsByPlayerDto {
    private List<PlayerStats> stats = new ArrayList<>();
}
