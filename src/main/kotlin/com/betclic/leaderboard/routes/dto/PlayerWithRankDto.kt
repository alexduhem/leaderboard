package com.betclic.leaderboard.routes.dto

import com.betclic.leaderboard.domain.Player
import com.betclic.leaderboard.domain.Rank
import kotlinx.serialization.Serializable

@Serializable
data class PlayerWithRankDto(val id: String, val slug: String, val points: Int, val rank: Long) {
    companion object {
        fun fromPlayerAndRank(pair: Pair<Player, Rank>): PlayerWithRankDto {
            return PlayerWithRankDto(
                pair.first.id.toString(),
                pair.first.slug,
                pair.first.points,
                pair.second
            )
        }
    }
}

