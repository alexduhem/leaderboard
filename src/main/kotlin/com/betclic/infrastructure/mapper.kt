package com.betclic.infrastructure

import com.betclic.leaderboard.domain.Player

fun Player.toDao(): PlayerDao {
    return PlayerDao(this.id.toString(), this.slug, this.points)
}

fun PlayerDao.toDomain(): Player {
    return Player(PlayerId.fromString(this._id), this.slug, this.points)
}