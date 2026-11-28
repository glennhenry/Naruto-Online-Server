package game.items

import kotlinx.serialization.Serializable

// rune stone
@Serializable
data class BagRuneInfo(
    val id: UInt,
    val index: UInt,
    val num: UInt,
    val exp: UInt,
    val composeExp: Int,
    val levelUpExp: Int,
    val isUltimate: Boolean
) {
    companion object {
        fun dummy(): BagRuneInfo {
            return BagRuneInfo(
                44006931u, 0u, 1u, 100u, 102, 1115, false
            )
        }
    }
}
