package game.player.common

import kotlinx.serialization.Serializable

@Serializable
data class NaurtoAllPropertyInfo(
    val hpUlimit: Int,
    val bodyAttack: Int,
    val bodyDefense: Int,
    val ninjaAttack: Int,
    val ninjaDefense: Int,
    val critValue: Int,
    val critDamageValue: Int,
    val speed: Int,
    val comboValue: Int,
    val controlValue: Int,
    val bodyPenetrateValue: Int,
    val ninjaPenetrateValue: Int,
    val damageReduceValue: Int,
    val hpRestore: Int,
    val fireAttack: Int,
    val windAttack: Int,
    val thunderAttack: Int,
    val earthAttack: Int,
    val waterAttack: Int,
    val fireResist: Int,
    val windResist: Int,
    val thunderResist: Int,
    val earthResist: Int,
    val waterResist: Int,
    val nixAttack: Int,
    val nixResist: Int
) {
    companion object {
        fun dummy(): NaurtoAllPropertyInfo {
            return NaurtoAllPropertyInfo(
                hpUlimit = 101,
                bodyAttack = 101,
                bodyDefense = 101,
                ninjaAttack = 101,
                ninjaDefense = 101,
                critValue = 101,
                critDamageValue = 101,
                speed = 101,
                comboValue = 101,
                controlValue = 101,
                bodyPenetrateValue = 101,
                ninjaPenetrateValue = 101,
                damageReduceValue = 101,
                hpRestore = 101,
                fireAttack = 101,
                windAttack = 101,
                thunderAttack = 101,
                earthAttack = 101,
                waterAttack = 101,
                fireResist = 101,
                windResist = 101,
                thunderResist = 101,
                earthResist = 101,
                waterResist = 101,
                nixAttack = 101,
                nixResist = 101
            )
        }
    }
}
