package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BaojuAttr(
    val hp: UInt,
    val bodyAttack: UInt,
    val bodyDefense: UInt,
    val ninjaAttack: UInt,
    val ninjaDefense: UInt
) {
    companion object {
        fun dummy(): BaojuAttr {
            return BaojuAttr(
                hp = 100u,
                bodyAttack = 100u,
                bodyDefense = 100u,
                ninjaAttack = 100u,
                ninjaDefense = 100u
            )
        }
    }
}
