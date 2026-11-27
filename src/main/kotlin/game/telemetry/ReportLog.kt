package game.telemetry

import kotlinx.serialization.Serializable

@Serializable
data class ReportLog(
    val logId: Int,
    val msg: String,
    val via: String
)
