package game.activities

import kotlinx.serialization.Serializable

@Serializable
data class ActivityStatus(
    val id: UInt,
    val status: UInt
)
