package encore.backstage.command.core

import encore.acts.ActScope
import encore.acts.template.runTimer
import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandResult
import game.context.ServerContext
import game.mongo.collection.ServerId
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds

/**
 * Command to shutdown the server.
 */
class ShutdownCommand : Command {
    override val id: String = "shutdown"
    override val name: String = "Shutdown"
    override val description: String = """
        Shutdown the server (real 😀). 
         The server will actually shutdown after 5 seconds.
    """.trimIndent()
    override val arguments: List<Argument> = emptyList()

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        return CommandResult.Success("Shutting down in 5 seconds, bye!").also {
            serverContext.stageActDirector.runTimer(5.seconds, ActScope(ServerId, serverContext.appScope)) {
                exitProcess(0)
            }
        }
    }
}
