package encore.backstage.command.core

import basicDateFormatter
import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import encore.time.TimeCenter
import game.context.ServerContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * Command to list all currently online players.
 */
class ListOnlineCommand : Command {
    override val id: String = "list-online"
    override val name: String = "List online players"
    override val description: String = """
        List all currently online players
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("limit", "Limit", "Limit the results", ArgumentType.Int, 20)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val limit = args.getInt("limit")
        val players = serverContext.subunits.presence.getAllPlayers(limit)

        return CommandResult.Success(
            buildString {
                appendLine("Total online players: ${serverContext.subunits.presence.getNumOnlinePlayers()}")
                appendLine("Results showing ${players.size} players:")
                players.forEachIndexed { idx, presence ->
                    appendLine("${idx + 1})  playerId: ${presence.playerId}")
                    appendLine("    onlineSince: ${basicDateFormatter.format(presence.onlineSince)} (${(TimeCenter.now() - presence.onlineSince).milliseconds.inWholeMinutes} minutes)")
                    appendLine("    lastNetworkActivity: ${basicDateFormatter.format(presence.lastNetworkActivity)}")
                    appendLine()
                }
            }
        )
    }
}
