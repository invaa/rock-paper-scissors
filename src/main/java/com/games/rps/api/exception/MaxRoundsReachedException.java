package com.games.rps.api.exception;

public class MaxRoundsReachedException extends RuntimeException {
    public MaxRoundsReachedException(int number, String gameId) {
        super(String.format("Game %s have reached max allowed amount of rounds (%d). You can only end the game.", gameId, number));
    }
}
