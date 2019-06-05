package com.games.rps.api.exception;

public class RoundSerializationException extends RuntimeException {
    public RoundSerializationException(String round, Exception e) {
        super(String.format("Round %s can't be serialised/deserialised.", round.toString()), e);
    }
}
