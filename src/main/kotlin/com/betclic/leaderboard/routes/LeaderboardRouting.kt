package com.betclic.leaderboard.routes

import com.betclic.leaderboard.domain.Player
import com.betclic.leaderboard.domain.dto.toDto
import com.betclic.leaderboard.routes.request.CreatePlayerRequest
import com.betclic.leaderboard.routes.request.PatchPlayerRequest
import com.betclic.leaderboard.routes.response.PlayersResponse
import com.betclic.leaderboard.usecase.LeaderboardUseCases
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject

fun Application.configureLeaderboardRouting() {
    val leaderboardUseCases by inject<LeaderboardUseCases>()
    routing {
        get("/players") {
            call.respond(
                HttpStatusCode.OK,
                PlayersResponse(leaderboardUseCases.getAllPlayers().map(Player::toDto))
            )
        }
        post("/players") {
            val body = call.receive<CreatePlayerRequest>()
            val player = leaderboardUseCases.createPlayer(body.slug)
            call.respond(HttpStatusCode.Created, player.toDto())
        }
        get("/players/{id}") {
            val id = call.parameters.getOrFail("id")
            val player = leaderboardUseCases.findPlayer(id)
            call.respond(HttpStatusCode.OK, player.toDto())
        }
        patch("/players/{id}") {
            val body = call.receive<PatchPlayerRequest>()
            val id = call.parameters.getOrFail("id")
            leaderboardUseCases.updatePlayerPoint(id, body.points)
            call.respond(HttpStatusCode.NoContent)
        }
        delete("/players") {
            leaderboardUseCases.deletePlayers()
            call.respond(HttpStatusCode.NoContent)
        }

    }
}