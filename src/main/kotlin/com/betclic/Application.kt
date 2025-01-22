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
    configureSerialization()
    configureDependencyInjection()
    configureLeaderboardRouting()
    configureRequestsValidation()
    configureErrorCatcher()
}
