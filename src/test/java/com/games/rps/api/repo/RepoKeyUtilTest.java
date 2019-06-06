package com.games.rps.api.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.games.rps.api.model.Move.ROCK;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RepoKeyUtilTest {
    @Test
    public void shouldGetChainKey() {
        // given when then
        assertEquals("v1:uid:rock:rock:player1", RepoKeyUtil.getChainKey("player1", ROCK, ROCK));
    }

    @Test
    public void shouldGetRoundsListName() {
        // given when then
        assertEquals("v1:uid:id:rounds", RepoKeyUtil.getRoundsListName("id"));
    }

    @Test
    public void shouldGetStatsMapName() {
        // given when then
        assertEquals("v1:uid:stats:id", RepoKeyUtil.getStatsMapName("id"));
    }

    @Test
    public void shouldGetMovesListName() {
        // given when then
        assertEquals("v1:uid:id:moves", RepoKeyUtil.getMovesListName("id"));
    }

    @Test
    public void shouldGetChainMapName() {
        // given when then
        assertEquals("v1:uid:chain", RepoKeyUtil.getChainMapName());
    }

    @Test
    public void shouldGetStatsRoundsMapName() {
        // given when then
        assertEquals("v1:uid:stats:statsRounds", RepoKeyUtil.getStatsRoundsMapName());
    }

    @Test
    public void shouldGetPlayersDefaultDictName() {
        // given when then
        assertEquals("v1:uid:playerDefaultDict", RepoKeyUtil.getPlayersDefaultDictName());
    }

    @Test
    public void shouldGetPlayersDictName() {
        // given when then
        assertEquals("v1:uid:playerDict:id", RepoKeyUtil.getPlayersDictName("id"));
    }
}