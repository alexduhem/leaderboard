package com.betclic.leaderboard.routes.response

import com.betclic.leaderboard.routes.dto.PlayerDto
import kotlinx.serialization.Serializable

@Serializable
data class PlayersResponse(val players: List<PlayerDto>)