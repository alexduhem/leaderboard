package com.betclic.leaderboard.usecase

import com.betclic.leaderboard.domain.Player

class LeaderboardUseCases {

    suspend fun createPlayer(slug:String): Player {
        return Player(PlayerId.random(), slug, 0)
    }
}