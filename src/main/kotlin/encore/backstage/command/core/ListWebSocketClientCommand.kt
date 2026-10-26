package encore.backstage.command.core

import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandResult
import game.context.ServerContext

/**
 * Command to list all websocket clients.
 */
class ListWebSocketClientCommand : Command {
    override val id: String = "list-ws-client"
    override val name: String = "List WebSocket client"
    override val description: String = """
        Retrieve all WebSocket client.
    """.trimIndent()
    override val arguments: List<Argument> = emptyList()

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val clients = serverContext.webSocketManager.getAllClients()
        return CommandResult.Success(
            buildString {
                if (clients.isEmpty()) {
                    appendLine("No websocket clients.")
                }
                clients.forEachIndexed { idx, cl ->
                    appendLine("Found ${clients.size} clients:")
                    appendLine("  ${idx + 1}) $cl")
                }
            }
        )
    }
}
