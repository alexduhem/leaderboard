package com.betclic

import com.betclic.leaderboard.routes.configureLeaderboardRouting
import com.betclic.leaderboard.routes.configureRequestsValidation
import com.betclic.plugins.configureDependencyInjection
import com.betclic.plugins.configureErrorCatcher
import com.betclic.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module() {
    val mongoUri = System.getenv("MONGODB_URI") ?: "mongodb://user:dev@localhost:27017"
    configureSerialization()
    configureDependencyInjection(mongoUri)
    configureLeaderboardRouting()
    configureRequestsValidation()
    configureErrorCatcher()
}
