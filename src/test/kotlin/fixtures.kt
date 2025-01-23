package betclic

import com.betclic.infrastructure.PlayerDao
import kotlin.random.Random


fun buildRandomString(length: Int = 10): String {
    val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Alphabet + chiffres
    return (1..length)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

fun aFakePlayerDao(aSlug: String?, somePoints: Int? = 0): PlayerDao {
    return PlayerDao(
        PlayerId.random().toString(),
        aSlug ?: buildRandomString(),
        somePoints ?: Random.nextInt(0, 1000)
    )
}