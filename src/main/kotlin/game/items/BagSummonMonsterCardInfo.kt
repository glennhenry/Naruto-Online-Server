package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BagSummonMonsterCardInfo(
    val id: UInt,
    val num: UInt
) {
    companion object {
        fun dummy(): BagSummonMonsterCardInfo {
            return BagSummonMonsterCardInfo(
                // monkey king (yellow)
                id = 22410022u,
                num = 2u
            )
        }
    }
}
