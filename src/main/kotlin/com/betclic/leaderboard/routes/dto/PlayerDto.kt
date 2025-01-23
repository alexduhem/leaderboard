package com.betclic.leaderboard.routes.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(val id: String, val slug: String, val points: Int)

