package com.betclic.plugins

import com.betclic.leaderboard.domain.ApiErrors
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun ApiErrors.httpCode(): HttpStatusCode {
    return when (this) {
        is ApiErrors.PlayerAlreadyExistsError -> HttpStatusCode.Conflict
        is ApiErrors.PlayerNotFoundError -> HttpStatusCode.NotFound
        is ApiErrors.PlayerIdMalformedError -> HttpStatusCode.BadRequest
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
        exception<ApiErrors> { call, cause ->
            call.respond(cause.httpCode(), mapOf(
                    "errorCode" to cause.errorCode,
                    "error" to cause.message
                )
            )
        }
    }
}