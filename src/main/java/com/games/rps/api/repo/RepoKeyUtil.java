package com.games.rps.api.repo;

import com.games.rps.api.model.Move;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RepoKeyUtil {
    public static final String UID = "uid";
    public static final String KEY_PREFIX = "v1";
    public static final String DELIMITER = ":";
    private static final String CHAIN_NAME = "chain";
    private static final String MOVES_NAME = "moves";
    private static final String STATS_NAME = "stats";
    private static final String ROUNDS_STATS_NAME = "statsRounds";
    private static final String PLAYERS_DEFAULT_DICT_NAME = "playerDefaultDict";
    private static final String PLAYERS_DICT_NAME = "playerDict";
    private static final String ROUNDS_NAME = "rounds";

    static String getMovesListName(String id) {
        return String.join(DELIMITER, KEY_PREFIX, UID, id, MOVES_NAME);
    }

    static String getChainMapName() {
        return String.join(DELIMITER, KEY_PREFIX, UID, CHAIN_NAME);
    }

    static String getRoundsListName(String id) {
        return String.join(DELIMITER, KEY_PREFIX, UID, id, ROUNDS_NAME);
    }

    static String getChainKey(String id, Move lastMove, Move currentMove) {
        return String.join(DELIMITER, KEY_PREFIX, UID, lastMove.name().toLowerCase(), currentMove.name().toLowerCase(), id.toLowerCase());
    }

    static String getStatsMapName(String type) {
        return String.join(DELIMITER, KEY_PREFIX, UID, STATS_NAME, type.toLowerCase());
    }

    static String getStatsRoundsMapName() {
        return String.join(DELIMITER, KEY_PREFIX, UID, STATS_NAME, ROUNDS_STATS_NAME);
    }

    static String getPlayersDefaultDictName() {
        return String.join(DELIMITER, KEY_PREFIX, UID, PLAYERS_DEFAULT_DICT_NAME);
    }

    static String getPlayersDictName(String key) {
        return String.join(DELIMITER, KEY_PREFIX, UID, PLAYERS_DICT_NAME, key);
    }

}
