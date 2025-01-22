package betclic.com.betclic.leaderboard.routes.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlayerRequest(
    val slug: String
)