package betclic

import com.betclic.infrastructure.Constants
import com.betclic.infrastructure.PlayerDao
import com.betclic.module
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject

fun ApplicationTestBuilder.appTestSetup() {
    application {
        module()
    }
}

suspend fun buildTestDbClient(): MongoClient {
    val client = MongoClient.create(System.getenv("MONGODB_URI") ?: throw Exception("MONGODB_URI NOT SET"))
    client.getDatabase(Constants.MONGO_DB_NAME).getCollection<PlayerDao>(Constants.PLAYERS_COLLECTION)
        .drop()
    return client
}

fun ApplicationTestBuilder.buildHttpTestClient(): HttpClient {
    return createClient {
        install(ContentNegotiation) {
            json()
        }
    }
}

suspend fun MongoClient.insertPlayerDao(player: PlayerDao) {
    getDatabase(Constants.MONGO_DB_NAME)
        .getCollection<PlayerDao>(Constants.PLAYERS_COLLECTION)
        .insertOne(player)
}