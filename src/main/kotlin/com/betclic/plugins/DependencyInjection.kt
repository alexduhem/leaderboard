package com.betclic.plugins

import com.betclic.infrastructure.MongoPlayerRepository
import com.betclic.leaderboard.domain.Player
import com.betclic.leaderboard.domain.PlayerRepository
import com.betclic.leaderboard.usecase.LeaderboardUseCases
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection(mongoUri: String) {
    install(Koin) {
        slf4jLogger()
        modules(module {
            single { MongoClient.create(mongoUri) }
            single<PlayerRepository> { MongoPlayerRepository(get()) }
            singleOf(::LeaderboardUseCases)
        })
    }
}
