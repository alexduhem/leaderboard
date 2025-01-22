package com.betclic.leaderboard.domain.dto

import com.betclic.leaderboard.domain.Player

fun Player.toDto() = PlayerDto(this.id.toString(), this.slug, this.points)