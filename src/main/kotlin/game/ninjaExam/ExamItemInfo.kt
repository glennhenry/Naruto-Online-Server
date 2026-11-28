package game.ninjaExam

import kotlinx.serialization.Serializable

@Serializable
data class ExamItemInfo(
    val id: UInt,
    val num: UInt
) {
    companion object {
        fun dummy(): ExamItemInfo {
            return ExamItemInfo(
                10600006u, 1u
            )
        }
    }
}
