package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BaojuSpecialityAttr(
    val speed: UInt,
    val controlValue: UInt,
    val critValue: UInt,
    val comboValue: UInt,
    val critDamageValue: UInt,
    val hp: UInt,
    val bodyAttack: UInt,
    val bodyDefense: UInt,
    val ninjaAttack: UInt,
    val ninjaDefense: UInt,
) {
    companion object {
        fun dummy(): BaojuSpecialityAttr {
            return BaojuSpecialityAttr(
                speed = 100u,
                controlValue = 100u,
                critValue = 100u,
                comboValue = 100u,
                critDamageValue = 100u,
                hp = 100u,
                bodyAttack = 100u,
                bodyDefense = 100u,
                ninjaAttack = 100u,
                ninjaDefense = 100u
            )
        }
    }
}
