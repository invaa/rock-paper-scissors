package com.games.rps.api.dto;

import com.games.rps.api.validator.ValidPlayerIdForSearch;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class StatsSearchDto {
    @NotNull
    @Min(0)
    private Long startFrom;
    @NotNull
    @Min(1)
    @Max(1000)
    private Integer howMany;
    @ValidPlayerIdForSearch
    private String beginsWith;
}
