package game.ninjaExam

import kotlinx.serialization.Serializable

@Serializable
data class NinjaExamInfo(
    val maxStage: UInt,
    val currStage: UInt,
    val totalExp: UInt,
    val freeCount: UInt,
    val raidsTime: UInt,
    val myRank: UInt,
    val totalMoney: UInt
) {
    companion object {
        fun dummy(): NinjaExamInfo {
            return NinjaExamInfo(
                maxStage = 100u,
                currStage = 1u,
                totalExp = 0u,
                // sweep attempts
                freeCount = 1u,
                raidsTime = 1u,
                myRank = 1u,
                totalMoney = 100u
            )
        }
    }
}
