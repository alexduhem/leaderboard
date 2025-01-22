package betclic.com.betclic.leaderboard.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(val id: String, val slug: String, val points: Int)

