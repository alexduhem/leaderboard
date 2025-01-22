package betclic.com.betclic.leaderboard.domain.dto

import betclic.com.betclic.leaderboard.domain.Player

fun Player.toDto() = PlayerDto(this.id.toString(), this.slug, this.points)