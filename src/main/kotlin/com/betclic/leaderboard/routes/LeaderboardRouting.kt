package com.betclic.leaderboard.routes

import com.betclic.leaderboard.domain.dto.toDto
import com.betclic.leaderboard.routes.request.CreatePlayerRequest
import com.betclic.leaderboard.usecase.LeaderboardUseCases
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureLeaderboardRouting() {
    val leaderboardUseCases by inject<LeaderboardUseCases>()
    routing {
        post("/players") {
            val body = call.receive<CreatePlayerRequest>()
            val player = leaderboardUseCases.createPlayer(body.slug)
            call.respond(player.toDto())
        }
    }
}