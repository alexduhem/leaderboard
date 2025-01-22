package com.betclic.leaderboard.domain

import PlayerId

interface PlayerRepository {

    suspend fun userExists(slug: String): Boolean

    suspend fun insertPlayer(player: Player)

    suspend fun updatePlayerPoints(playerId: PlayerId, points: Int)

    suspend fun findPlayer(playerId: PlayerId): Player?

    suspend fun getAllPlayers(): List<Player>

    suspend fun deletePlayers()

}