package com.betclic.leaderboard.domain

sealed class PlayerErrors(override val message: String, val errorCode: String) : Exception() {

    data class PlayerAlreadyExistsError(val slug: String) :
        PlayerErrors(message = "Player $slug already exists", errorCode = "PLAYER_ALREADY_EXISTS")

    data class PlayerNotFoundError(val slug: String) :
        PlayerErrors(message = "Player $slug does not exists", errorCode = "PLAYER_NOT_FOUND")
}
