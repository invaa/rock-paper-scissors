package com.games.rps.api.exception;

public class GameNotExistsException extends RuntimeException {

	public GameNotExistsException(String id) {
		super(String.format("The game with id %s does not exist!", id));
	}

}
