package game.player.login

import kotlinx.serialization.Serializable

@Serializable
data class TGPInfo(
    val tgpFlag: Boolean,
    val autoLaunchFlag: Boolean,
    val autoLaunchWithStartupFlag: Boolean,
    val tgpBoxFlag: Boolean
)
