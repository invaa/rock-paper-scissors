package com.games.rps.api.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

import static com.games.rps.api.repo.RepoKeyUtil.*;

@RedisHash(KEY_PREFIX + UID + DELIMITER + "rounds")
@Data
public class Game {
	@Id
	@Setter(value = AccessLevel.PRIVATE)
	private String id;
	private Status status;

	@Transient
	private List<Round> rounds;

	public Game() {
		this.status = Status.STARTED;
	}
}
