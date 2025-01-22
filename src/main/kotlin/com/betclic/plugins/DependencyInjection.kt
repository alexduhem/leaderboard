package com.betclic.plugins

import com.betclic.leaderboard.usecase.LeaderboardUseCases
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            singleOf(::LeaderboardUseCases)
        })
    }
}
