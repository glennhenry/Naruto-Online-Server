package game.gdpr

import kotlinx.serialization.Serializable

@Serializable
data class GetRoleRequest(
    val ninjaLimitRole: Int
)
