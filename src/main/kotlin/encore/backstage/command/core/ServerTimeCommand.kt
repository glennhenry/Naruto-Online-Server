package encore.backstage.command.core

import detailedDateFormatter
import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandResult
import encore.time.TimeCenter
import game.context.ServerContext
import runningSince

/**
 * Command to get the server time and uptime.
 */
class ServerTimeCommand : Command {
    override val id: String = "time"
    override val name: String = "Server time"
    override val description: String = """
        Get the server time.
    """.trimIndent()
    override val arguments: List<Argument> = emptyList()

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val info = getUptimeInfo()
        return CommandResult.Success(
            buildString {
                appendLine("serverTime (epoch): ${info.serverTime}")
                appendLine("serverTime: ${detailedDateFormatter.format(info.serverTime)}")
                appendLine("runningSince: ${detailedDateFormatter.format(runningSince)}")
                appendLine("hasBeenRunningFor: ${TimeCenter.formatToDurationString(info.hasBeenRunningFor)}")
            }
        )
    }
}

/**
 * Produce [UptimeInfo].
 */
fun getUptimeInfo(): UptimeInfo {
    val now = TimeCenter.now()
    val hasRunFor = (now - runningSince)
    return UptimeInfo(
        serverTime = now,
        runningSince = runningSince,
        hasBeenRunningFor = hasRunFor
    )
}

/**
 * Uptime info of the server.
 *
 * @property serverTime Epoch millis now.
 * @property runningSince Epoch millis since server started running.
 * @property hasBeenRunningFor Milliseconds of the total server running time.
 */
data class UptimeInfo(
    val serverTime: Long,
    val runningSince: Long,
    val hasBeenRunningFor: Long
)
