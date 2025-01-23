package betclic

import PlayerId
import com.betclic.infrastructure.Constants
import com.betclic.infrastructure.PlayerDao
import com.betclic.leaderboard.routes.dto.PlayerWithRankDto
import com.betclic.leaderboard.routes.response.PlayersResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import org.koin.core.component.KoinComponent
import kotlin.test.Test
import kotlin.test.assertEquals


class ApplicationTest : KoinComponent {

    @Test
    fun aPlayerShouldBeCreatedWithAValidSlug() = testApplication {
        appTestSetup()
        val client = buildHttpTestClient()
        val dbClient = buildTestDbClient()
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
                dbClient.getDatabase(Constants.MONGO_DB_NAME)
                    .getCollection<PlayerDao>(Constants.PLAYERS_COLLECTION)
                    .countDocuments()
            )
        }
    }

    @Test
    fun aBadRequestIsThrownIfNoSlugProvided() = testApplication {
        val httpClient = buildHttpTestClient()
        appTestSetup()
        httpClient.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("whatever" to "whatever"))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun aBadRequestIsThrownIfABlankSlugProvided() = testApplication {
        val httpClient = buildHttpTestClient()
        appTestSetup()
        httpClient.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("slug" to " "))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals(
                mapOf(
                    "error" to "A player slug must be provided",
                    "errorCode" to "BAD_REQUEST"
                ),
                body()
            )
        }
    }

    @Test
    fun shouldNotCreateUsersWithSameSlug() = testApplication {
        val httpClient = buildHttpTestClient()
        appTestSetup()
        val aSlug = "Michel"
        val dbClient = buildTestDbClient()
        dbClient.insertPlayerDao(aFakePlayerDao(aSlug))
        httpClient.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("slug" to aSlug))
        }.apply {
            assertEquals(HttpStatusCode.Conflict, status)
        }
    }

    @Test
    fun shouldReturnAllThePlayersSortingByDescendingPoints() = testApplication {
        appTestSetup()
        val httpClient = buildHttpTestClient()
        val dbClient = buildTestDbClient()
        dbClient.insertPlayerDao(aFakePlayerDao("Michel", 50))
        dbClient.insertPlayerDao(aFakePlayerDao("Antoine", 5))
        dbClient.insertPlayerDao(aFakePlayerDao("Christophe", 100))
        dbClient.insertPlayerDao(aFakePlayerDao("Marie", 5))
        httpClient.get("/players").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = body<PlayersResponse>()
            assertEquals(
                listOf("Christophe", "Michel", "Antoine", "Marie"), response.players.map { it.slug }
            )
        }
    }

    @Test
    fun shouldReturnAPlayerWithTheGoodRank() = testApplication {

        appTestSetup()
        val httpClient = buildHttpTestClient()
        val dbClient = buildTestDbClient()
        val michel = aFakePlayerDao("Michel", 50)
        dbClient.insertPlayerDao(michel)
        val antoine = aFakePlayerDao("Antoine", 5)
        dbClient.insertPlayerDao(antoine)
        val christophe = aFakePlayerDao("Christophe", 100)
        dbClient.insertPlayerDao(christophe)


        httpClient.get("/players/${michel._id}").apply {
            assertEquals(2, body<PlayerWithRankDto>().rank)
        }

        httpClient.get("/players/${antoine._id}").apply {
            assertEquals(3, body<PlayerWithRankDto>().rank)
        }

        httpClient.get("/players/${christophe._id}").apply {
            assertEquals(1, body<PlayerWithRankDto>().rank)
        }
    }

}
