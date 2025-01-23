package com.betclic.leaderboard.usecase

import PlayerId
import com.betclic.leaderboard.domain.Player
import com.betclic.leaderboard.domain.ApiErrors
import com.betclic.leaderboard.domain.PlayerRepository
import com.betclic.leaderboard.domain.Rank

class LeaderboardUseCases(private val repository: PlayerRepository) {

    suspend fun createPlayer(slug: String): Player {
        if (repository.userExists(slug)) {
            throw ApiErrors.PlayerAlreadyExistsError(slug)
        }
        val player = Player(PlayerId.random(), slug, 0)
        repository.insertPlayer(player)
        return player;
    }

    suspend fun updatePlayerPoint(playerId: String, points: Int): Pair<Player, Rank> {
        val player = repository.updatePlayerPoints(PlayerId.fromString(playerId), points)
            ?: throw ApiErrors.PlayerNotFoundError(playerId)
        return Pair(player, repository.findPlayerRank(player))
    }

    suspend fun deletePlayers(): Unit {
        repository.deletePlayers()
    }

    suspend fun getAllPlayers(): List<Player> {
        return repository.getAllPlayers()
    }

    suspend fun findPlayer(playerId: String): Pair<Player, Rank> {
        val player =
            repository.findPlayer(PlayerId.fromString(playerId)) ?: throw ApiErrors.PlayerNotFoundError(playerId)
        return Pair(player, repository.findPlayerRank(player))
    }
}