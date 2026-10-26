package encore.backstage.command.core

import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import encore.fancam.events.LogEvent
import encore.fancam.events.TrackEvent
import encore.fancam.impl.OfficialFancam
import game.context.ServerContext
import preciseBasicDateFormatter

/**
 * A command to retrieve the last N recent server logs.
 *
 * - Does not support [TrackEvent] logs. Supports any kind of [LogEvent].
 * - Works with [OfficialFancam].
 *
 * @constructor Creates a new GetLogsCommand
 */
class GetLogsCommand(private val fancam: OfficialFancam) : Command {
    override val id: String = "get-logs"
    override val name: String = "Get logs"
    override val description: String = """
        Get the last N recent server logs. 
        This only supports up to maximum 25 logs. 
        It's not intended for deep logs inspection.
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument(
            "level",
            "Level",
            "The level category of logs: trace, debug, info, warn, error or any",
            ArgumentType.String,
            "any"
        ),
        Argument(
            "amount",
            "Amount",
            "The amount of logs to request (max 25)",
            ArgumentType.Int,
            20
        )
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val level = args.getString("level")
        val amount = minOf(25, args.getInt("amount"))

        val logs = when (level) {
            "trace" -> fancam.lastTrace.takeLast(amount)
            "debug" -> fancam.lastDebug.takeLast(amount)
            "info" -> fancam.lastInfo.takeLast(amount)
            "warn" -> fancam.lastWarn.takeLast(amount)
            "error" -> fancam.lastError.takeLast(amount)
            "any" -> {
                (fancam.lastTrace.takeLast(amount) +
                        fancam.lastDebug.takeLast(amount) +
                        fancam.lastInfo.takeLast(amount) +
                        fancam.lastWarn.takeLast(amount) +
                        fancam.lastError.takeLast(amount))
                    .sortedByDescending { it.timestamp }
                    .take(25)
            }

            else -> {
                return CommandResult.ExecutionFailure(
                    "Invalid level '$level'; valid options: 'any', 'trace', 'debug', 'info', 'warn', or 'error'."
                )
            }
        }

        return CommandResult.Success(
            buildString {
                if (logs.isEmpty()) {
                    appendLine("No messages.")
                }
                if (args.getInt("amount") > 25) {
                    appendLine("Maximum request amount is 25")
                }
                for (i in logs.indices) {
                    val log = logs[i]
                    appendLine("${i + 1}) [${preciseBasicDateFormatter.format(log.timestamp)}][${log.level}][${log.tag}] ${log.message()}")
                }
            }
        )
    }
}
