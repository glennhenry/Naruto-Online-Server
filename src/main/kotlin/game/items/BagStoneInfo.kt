package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BagStoneInfo(
    val id: UInt,
    val index: UInt,
    val num: UInt,
    val exp: UInt,
    val isUltimate: Boolean
) {
    companion object {
        fun dummy(): BagStoneInfo {
            return BagStoneInfo(
                // lvl 6 init rune stone
                id = 44006601u,
                index = 0u,
                num = 10u,
                exp = 100u,
                isUltimate = false
            )
        }
    }
}
