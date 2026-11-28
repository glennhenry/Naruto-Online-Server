package game.ninjaExam

import kotlinx.serialization.Serializable

@Serializable
data class ExamStageItemInfo(
    val stage: UInt,
    val examItemInfo: List<ExamItemInfo>
) {
    companion object {
        fun dummy(): ExamStageItemInfo {
            return ExamStageItemInfo(
                1u, listOf(ExamItemInfo.dummy())
            )
        }
    }
}
