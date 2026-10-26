package encore.backstage.command

import encore.acts.ActiveActInfo
import encore.backstage.command.core.MemoryStats
import kotlinx.serialization.Serializable

/**
 * Monitor data for backstage client.
 */
@Serializable
data class MonitorData(
    val numPlayers: Int,
    val memStats: MemoryStats,
    val acts: List<ActiveActInfo>
)
