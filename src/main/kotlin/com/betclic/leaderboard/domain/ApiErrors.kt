package com.betclic.leaderboard.domain

sealed class ApiErrors(override val message: String, val errorCode: String) : Exception() {

    data class PlayerAlreadyExistsError(val slug: String) :
        ApiErrors(message = "Player $slug already exists", errorCode = "PLAYER_ALREADY_EXISTS")

    data class PlayerNotFoundError(val slug: String) :
        ApiErrors(message = "Player $slug does not exists", errorCode = "PLAYER_NOT_FOUND")

    data class PlayerIdMalformedError(val id:String) :
        ApiErrors(message = "The player id is malformed", errorCode = "BAD_REQUEST")
}
