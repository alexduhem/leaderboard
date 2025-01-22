package betclic

import com.betclic.module
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import kotlin.test.Test
import kotlin.test.assertEquals

fun ApplicationTestBuilder.buildTestClient(): HttpClient {
    return createClient {
        install(ContentNegotiation) {
            json()
        }
    }
}

class ApplicationTest {

    @Test
    fun aPlayerShouldBeCreatedWithAValidSlug() = testApplication {
        val client = buildTestClient()
        application {
            module()

        }
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
        }
    }

    @Test
    fun aBadRequestIsThrownIfNoSlugProvided() = testApplication {
        val client = buildTestClient()
        application {
            module()

        }
        client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("whatever" to "whatever"))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun aBadRequestIsThrownIfABlankSlugProvided() = testApplication {
        val client = buildTestClient()
        application {
            module()
        }
        client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("slug" to " "))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("{\"error\":\"A player slug must be provided\"}", body())
        }
    }

}
