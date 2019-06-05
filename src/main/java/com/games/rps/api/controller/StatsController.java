package com.games.rps.api.controller;

import com.games.rps.api.dto.StatsByPlayerDto;
import com.games.rps.api.dto.StatsSearchDto;
import com.games.rps.api.model.PlayerStats;
import com.games.rps.api.service.StatsService;
import com.games.rps.api.validator.ValidPlayerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "api/v1/stats")
@Slf4j
@Validated
public class StatsController {

	private final StatsService statsService;

	public StatsController(StatsService statsService) {
		this.statsService = statsService;
	}

	@RequestMapping(method = GET, value = "", produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	StatsByPlayerDto stats(@Valid StatsSearchDto searchDto) {
		return statsService.get(searchDto);
	}

	@RequestMapping(method = GET, value = "/{playerId}", produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	PlayerStats statsByPlayer(@PathVariable @ValidPlayerId String playerId) {
		return statsService.getById(playerId);
	}
}
