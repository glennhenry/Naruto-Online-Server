package game.activities.dayActivity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class DayActivityInfo(
    val type: DayActivityType,
    val desc: String,
    val image: String,
    val openUi: String,
    val arg: Int,
    val argType: Int,
    val data: Int
) {
    companion object {
        fun dummy(): DayActivityInfo {
            return DayActivityInfo(
                type = DayActivityType.PROTO_DAY_ACTIVITY_TYPE_1,
                desc = "Hello activity",
                image = "???",
                openUi = "test",
                arg = 1,
                argType = 1,
                data = 1
            )
        }
    }
}
