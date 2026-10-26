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
 * Get an online player information by its playerId.
 */
class GetOnlineCommand : Command {
    override val id: String = "get-online"
    override val name: String = "Get online"
    override val description: String = """
        Get a particular online player by its playerId
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("playerId", "Player ID", "Unique playerId of the player", ArgumentType.String)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val playerId = args.getString("playerId")

        return CommandResult.Success(
            buildString {
                val presence = serverContext.subunits.presence.getPresence(playerId)
                if (presence != null) {
                    appendLine("playerId: ${presence.playerId}")
                    appendLine("    onlineSince: ${basicDateFormatter.format(presence.onlineSince)} (${(TimeCenter.now() - presence.onlineSince).milliseconds.inWholeMinutes} minutes)")
                    appendLine("lastNetworkActivity: ${basicDateFormatter.format(presence.lastNetworkActivity)}")
                } else {
                    appendLine("playerId '$playerId' is either not found or not online.")
                }
            }
        )
    }
}
