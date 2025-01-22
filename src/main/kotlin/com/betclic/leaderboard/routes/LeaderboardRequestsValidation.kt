package betclic.com.betclic.leaderboard.routes

import betclic.com.betclic.leaderboard.routes.request.CreatePlayerRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestsValidation() {
    install(RequestValidation) {
        validate<CreatePlayerRequest> { player ->
            if (player.slug.isBlank()) {
                ValidationResult.Invalid("A player slug must be provided")
            }
            else ValidationResult.Valid
        }
    }
}