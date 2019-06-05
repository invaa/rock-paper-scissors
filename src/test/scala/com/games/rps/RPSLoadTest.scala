package com.games.rps

import scala.util.Random
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RPSLoadTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
  	.contentTypeHeader("application/json;charset=UTF-8")
		.userAgentHeader("PostmanRuntime/7.4.0")

	val movesArrays = Array("ROCK", "PAPER", "SCISSORS")

	val headers = Map(
		"cache-control" -> "no-cache"
	)

	val scn = scenario("RPSLoadTest")
		.pause(1,100)
		.exec(http("createGame")
			.post("/api/v1/games/")
  		.headers(headers)
			.check(status.is(201))
			.check(jsonPath("$.id").exists.saveAs("gameId"))
		)
		.exitHereIfFailed
		.pause(1,2)
	  .exec(_.set("playerId", "player" + (1 + Random.nextInt(9999))))
		.exec(_.set("move", Random.shuffle(movesArrays.toList).head))
		.exec(http("playRound")
			.put("/api/v1/games/${gameId}")
			.headers(headers)
			.body(StringBody("""{"playerId": "${playerId}", "move": "${move}"}"""))
      .check(status.is(200))
		)
		.pause(1,2)
		.exec(http("finishRound")
			.delete("/api/v1/games/${gameId}")
			.headers(headers)
			.check(status.is(200))
		)

	setUp(scn.inject(
		nothingFor(4 seconds),
		atOnceUsers(10000)
	).protocols(httpProtocol)).maxDuration(2 minutes)
}