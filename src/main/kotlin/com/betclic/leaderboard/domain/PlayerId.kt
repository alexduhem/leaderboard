import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class PlayerId @OptIn(ExperimentalUuidApi::class) constructor(val uuid: Uuid) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun random(): PlayerId {
            return PlayerId(Uuid.random())
        }

        @OptIn(ExperimentalUuidApi::class)
        fun fromString(aString:String): PlayerId {
            return PlayerId(Uuid.parse(aString))
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun toString(): String {
        return this.uuid.toString()
    }
}