package encore.backstage.command.core

import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandResult
import game.context.ServerContext

/**
 * Command to clear all sessions in the SessionSubunits
 */
class ClearSessionCommand : Command {
    override val id: String = "clear-sessions"
    override val name: String = "Clear all sessions"
    override val description: String = """
        Clear all sessions on SessionSubunit
    """.trimIndent()
    override val arguments: List<Argument> = emptyList()

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val total = serverContext.subunits.session.clearAllSessions()
        return CommandResult.Success("Cleared $total total sessions.")
    }
}
