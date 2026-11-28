package game.items

import kotlinx.serialization.Serializable

@Serializable
data class EquipFumoLevelAttr(
    val hp: UInt,
    val bodyAttack: UInt,
    val bodyDefense: UInt,
    val ninjaAttack: UInt,
    val ninjaDefense: UInt,
    val speed: UInt,
    val critValue: UInt,
    val critDamageValue: UInt,
    val controlValue: UInt,
    val comboValue: UInt,
    val nowLevelBaseRate: UInt,
    val nowLevelForgeRate: UInt,
    val nextLevelBaseRate: UInt,
    val nextLevelForgeRate: UInt
) {
    companion object {
        fun dummy(): EquipFumoLevelAttr {
            return EquipFumoLevelAttr(
                hp = 123u,
                bodyAttack = 123u,
                bodyDefense = 123u,
                ninjaAttack = 123u,
                ninjaDefense = 123u,
                speed = 123u,
                critValue = 123u,
                critDamageValue = 123u,
                controlValue = 123u,
                comboValue = 123u,
                nowLevelBaseRate = 123u,
                nowLevelForgeRate = 123u,
                nextLevelBaseRate = 123u,
                nextLevelForgeRate = 123u
            )
        }
    }
}
