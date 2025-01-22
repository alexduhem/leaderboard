package com.betclic.leaderboard.usecase

import com.betclic.leaderboard.domain.Player
import com.betclic.leaderboard.domain.PlayerErrors
import com.betclic.leaderboard.domain.PlayerRepository

class LeaderboardUseCases(private val repository: PlayerRepository) {

    suspend fun createPlayer(slug: String): Player {
        if (repository.userExists(slug)) {
            throw PlayerErrors.PlayerAlreadyExistsError(slug)
        }
        val player = Player(PlayerId.random(), slug, 0)
        repository.insertPlayer(player)
        return player;
    }
}