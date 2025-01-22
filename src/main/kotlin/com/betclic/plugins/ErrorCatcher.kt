package com.betclic.plugins

import com.betclic.leaderboard.domain.PlayerErrors
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun PlayerErrors.httpCode(): HttpStatusCode {
    return when (this) {
        is PlayerErrors.PlayerAlreadyExistsError -> HttpStatusCode.Conflict
        is PlayerErrors.PlayerNotFoundError -> HttpStatusCode.NotFound
    }
}


fun Application.configureErrorCatcher() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf(
                    "errorCode" to "BAD_REQUEST",
                    "error" to cause.reasons.joinToString("")
                )
            )
        }
        exception<PlayerErrors> { call, cause ->
            call.respond(cause.httpCode(), mapOf(
                    "errorCode" to cause.errorCode,
                    "error" to cause.message
                )
            )
        }
    }
}