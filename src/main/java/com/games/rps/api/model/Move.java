package com.games.rps.api.model;

public enum Move {
    ROCK {
        @Override
        public Move losesTo() {
            return PAPER;
        }
    },
    PAPER {
        @Override
        public Move losesTo() {
            return SCISSORS;
        }
    },
    SCISSORS {
        @Override
        public Move losesTo() {
            return ROCK;
        }
    };
    public abstract Move losesTo();
}
