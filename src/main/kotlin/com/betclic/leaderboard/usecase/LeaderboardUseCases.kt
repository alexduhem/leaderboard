package com.betclic.leaderboard.usecase

import com.betclic.leaderboard.domain.Player
import com.betclic.leaderboard.domain.ApiErrors
import com.betclic.leaderboard.domain.PlayerRepository

class LeaderboardUseCases(private val repository: PlayerRepository) {

    suspend fun createPlayer(slug: String): Player {
        if (repository.userExists(slug)) {
            throw ApiErrors.PlayerAlreadyExistsError(slug)
        }
        val player = Player(PlayerId.random(), slug, 0)
        repository.insertPlayer(player)
        return player;
    }

    suspend fun updatePlayerPoint(playerId: String, points: Int): Unit {
        repository.updatePlayerPoints(PlayerId.fromString(playerId), points)
    }

    suspend fun deletePlayers(): Unit {
        repository.deletePlayers()
    }

    suspend fun getAllPlayers(): List<Player> {
        return repository.getAllPlayers()
    }

    suspend fun findPlayer(playerId: String): Player {
        return repository.findPlayer(PlayerId.fromString(playerId)) ?: throw ApiErrors.PlayerNotFoundError(playerId)
    }
}