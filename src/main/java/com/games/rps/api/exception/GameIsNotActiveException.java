package com.games.rps.api.exception;

public class GameIsNotActiveException extends RuntimeException {
	public GameIsNotActiveException(String id) {
		super(String.format("The game with id %s was terminated or finished!", id));
	}
}
