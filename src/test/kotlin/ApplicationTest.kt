package betclic

import PlayerId
import com.betclic.infrastructure.Constants
import com.betclic.infrastructure.PlayerDao
import com.betclic.module
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.ktor.ext.inject
import kotlin.test.Test
import kotlin.test.assertEquals

fun ApplicationTestBuilder.appTestSetup() {
    application {
        module()
        val mongoClient by inject<MongoClient>()
        runBlocking {
            mongoClient.getDatabase(Constants.MONGO_DB_NAME).getCollection<PlayerDao>(Constants.PLAYERS_COLLECTION)
                .drop()
        }
    }
}

fun ApplicationTestBuilder.buildHttpTestClient(): HttpClient {
    return createClient {
        install(ContentNegotiation) {
            json()
        }
    }
}

class ApplicationTest : KoinComponent {


    @Test
    fun aPlayerShouldBeCreatedWithAValidSlug() = testApplication {
        appTestSetup()
        val client = buildHttpTestClient()
        val mongoClient by inject<MongoClient>()
        val aSlug = "Michel"
        val aFakeId = "1f61fbee-c399-4215-9d2c-3ec10d5c9450"
        mockkObject(PlayerId.Companion)
        every { PlayerId.random() } returns PlayerId.fromString(aFakeId)

        client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("slug" to aSlug))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertEquals(
                mapOf(
                    "id" to aFakeId,
                    "slug" to aSlug,
                    "points" to "0"
                ), body()
            )
            assertEquals(
                1,
                mongoClient.getDatabase(Constants.MONGO_DB_NAME)
                    .getCollection<PlayerDao>(Constants.PLAYERS_COLLECTION)
                    .countDocuments()
            )
        }
    }

    @Test
    fun aBadRequestIsThrownIfNoSlugProvided() = testApplication {
        val client = buildHttpTestClient()
        appTestSetup()
        client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("whatever" to "whatever"))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun aBadRequestIsThrownIfABlankSlugProvided() = testApplication {
        val client = buildHttpTestClient()
        appTestSetup()
        client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("slug" to " "))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals(
                mapOf(
                    "error" to "A player slug must be provided",
                    "errorCode" to "BAD_REQUEST"
                ),
                body<Map<String, String>>()
            )
        }
    }

}
