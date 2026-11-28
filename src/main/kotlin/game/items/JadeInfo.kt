package game.items

import kotlinx.serialization.Serializable

// magatama!
@Serializable
data class JadeInfo(
    val id: UInt,
    val num: UInt,
    val exp: UInt,
    val isUltimate: Boolean
) {
    companion object {
        fun dummy(): JadeInfo {
            return JadeInfo(
                id = 40703701u,
                num = (1u..4u).random(),
                exp = 1234u,
                isUltimate = false
            )
        }
    }
}
