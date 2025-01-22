package com.betclic.leaderboard.routes.request

import kotlinx.serialization.Serializable

@Serializable
data class PatchPlayerRequest(
    val points: Int
)