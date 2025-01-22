package betclic.com.betclic

import betclic.com.betclic.leaderboard.routes.configureLeaderboardRouting
import betclic.com.betclic.leaderboard.routes.configureRequestsValidation
import betclic.com.betclic.plugins.configureDependencyInjection
import betclic.com.betclic.plugins.configureErrorCatcher
import betclic.com.betclic.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureLeaderboardRouting()
    configureRequestsValidation()
    configureErrorCatcher()
}
