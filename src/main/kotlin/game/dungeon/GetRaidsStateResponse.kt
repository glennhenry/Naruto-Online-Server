package game.dungeon

import game.ninjaExam.ExamStageItemInfo
import game.ninjaExam.NinjaExamInfo
import game.protocol.response.RetInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class GetRaidsStateResponse(
    val ret: RetInfo = RetInfo(0, ""),
    val raidsState: RaidsType,
    val dungeonRaidsInfo: DungeonRaidsInfo,
    val raidsReward: List<RaidsCountReward>,
    val examRaidsInfo: NinjaExamInfo,
    val stageItemInfo: List<ExamStageItemInfo>
)
