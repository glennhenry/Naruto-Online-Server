package game.dungeon

import kotlinx.serialization.Serializable

@Serializable
data class DungeonRaidsInfo(
    val dungeonId: UInt,
    val remainCount: UInt,
    val remainTime: UInt,
    val currCount: UInt,
    val currTime: UInt,
    val eliteDungeonRemainTimes: UInt
) {
    companion object {
        fun dummy(): DungeonRaidsInfo {
            return DungeonRaidsInfo(
                dungeonId = 1u,
                remainCount = 1u,
                remainTime = 1u,
                currCount = 1u,
                currTime = 1u,
                eliteDungeonRemainTimes = 1u
            )
        }
    }
}
