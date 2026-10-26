package encore.backstage.command.core

import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import game.context.ServerContext

class StopActsCommand: Command {
    override val id: String = "stop-acts"
    override val name: String = "Stop acts"
    override val description: String = """
        Stop a currently running stage act by its actId.
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("actId", "Act ID", "The target actId", ArgumentType.String)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val actId = args.getString("actId")
        val result = serverContext.stageActDirector.stop(actId)
        return if (result) {
            CommandResult.Success("Stopped actId '$actId' successfully.")
        } else {
            CommandResult.Success("actId '$actId' is not found.")
        }
    }
}
