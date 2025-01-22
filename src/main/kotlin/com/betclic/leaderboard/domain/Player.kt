package com.betclic.leaderboard.domain

import PlayerId


data class Player constructor(val id: PlayerId, val slug: String, val points: Int)
