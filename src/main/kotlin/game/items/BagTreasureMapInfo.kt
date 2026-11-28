package game.items

import game.player.common.Point
import kotlinx.serialization.Serializable

// treasure map item
@Serializable
data class BagTreasureMapInfo(
    val id: Int,
    val index: Int,
    val mapId: Int,
    val point: Point
) {
    companion object {
        fun dummy(): BagTreasureMapInfo {
            return BagTreasureMapInfo(
                id = 10002,
                index = 0,
                mapId = 10002,
                point = Point(50u, 65u)
            )
        }
    }
}
