package com.betclic.leaderboard.domain

interface PlayerRepository {

    suspend fun userExists(slug:String): Boolean

    suspend fun insertPlayer(player: Player)

}