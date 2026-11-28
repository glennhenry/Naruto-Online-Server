package game.player.pk

import kotlinx.serialization.Serializable

@Serializable
data class ShowFreePKBattle(
    val freePkBattleId: ULong,
    val freePkSide: Int
)
