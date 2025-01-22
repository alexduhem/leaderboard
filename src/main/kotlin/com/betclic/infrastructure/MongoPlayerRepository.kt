package com.betclic.infrastructure

import com.betclic.leaderboard.domain.Player
import com.betclic.leaderboard.domain.PlayerRepository
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

class MongoPlayerRepository : PlayerRepository {

    private val PLAYER_COLLECTION_NAME = "players"
    private val LEADERBOARD_DATABASE_NAME = "leaderboard"
    private val playerCollection: MongoCollection<PlayerDao>
    private val client: MongoClient

    constructor(mongoClient: MongoClient) {
        this.client = mongoClient
        val database: MongoDatabase = client.getDatabase(LEADERBOARD_DATABASE_NAME)
        playerCollection = database.getCollection(PLAYER_COLLECTION_NAME)
    }

    override suspend fun userExists(slug: String): Boolean =
        this.playerCollection.find(eq("slug", slug)).firstOrNull() !== null

    override suspend fun insertPlayer(player: Player) {
        this.playerCollection.insertOne(player.toDao())
    }

//    fun findPlayer(id:PlayerId) : Player {
//
//    }

}