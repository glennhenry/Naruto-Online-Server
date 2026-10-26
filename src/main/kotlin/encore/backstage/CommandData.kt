package encore.backstage

import kotlinx.serialization.Serializable

/**
 * Command data for backstage client.
 */
@Serializable
data class CommandData(
    val id: String,
    val name: String,
    val description: String,
    val arguments: List<ArgumentData>
)

/**
 * Argument data of command for backstage client.
 */
@Serializable
data class ArgumentData(
    val id: String,
    val name: String,
    val description: String,
    val type: String,
    val defaultValue: String
)
