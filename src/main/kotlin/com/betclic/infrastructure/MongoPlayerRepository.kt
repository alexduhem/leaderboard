package com.betclic.infrastructure

import PlayerId
import com.betclic.leaderboard.domain.Player
import com.betclic.leaderboard.domain.PlayerRepository
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class MongoPlayerRepository(mongoClient: MongoClient) : PlayerRepository {

    private val playerCollection: MongoCollection<PlayerDao>

    init {
        val database: MongoDatabase = mongoClient.getDatabase(Constants.MONGO_DB_NAME)
        playerCollection = database.getCollection(Constants.PLAYERS_COLLECTION)
    }

    override suspend fun userExists(slug: String): Boolean =
        playerCollection.find(eq(PlayerDao::slug.name, slug)).firstOrNull() !== null

    override suspend fun insertPlayer(player: Player) {
        playerCollection.insertOne(player.toDao())
    }

    override suspend fun updatePlayerPoints(playerId: PlayerId, points: Int) {
        playerCollection.updateOne(
            eq(PlayerDao::_id.name, playerId.toString()),
            Updates.set(PlayerDao::points.name, points)
        )
    }

    override suspend fun findPlayer(playerId: PlayerId): Player? {
        return playerCollection.find(eq(PlayerDao::_id.name, playerId.toString())).firstOrNull()?.toDomain()
    }

    override suspend fun getAllPlayers(): List<Player> {
        return playerCollection.find().sort(Sorts.descending(PlayerDao::points.name)).map { it.toDomain() }.toList()
    }

    override suspend fun deletePlayers() {
        playerCollection.drop()
    }

}