package com.betclic.infrastructure

import com.betclic.leaderboard.domain.Player

fun Player.toDao() : PlayerDao{
    return PlayerDao(this.id.toString(), this.slug, this.points)
}