package encore.backstage.command.core

import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import encore.utils.dataStructure.EasyStorage
import game.context.ServerContext

/**
 * Command to peek the contents of [EasyStorage].
 */
class PeekEasyStorageCommand : Command {
    override val id: String = "peek-storage"
    override val name: String = "Peek easy storage"
    override val description: String = """
        See a subset of contents inside the easy storage.
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("limit", "Limit", "Limit the results", ArgumentType.Int, 20)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val limit = args.getInt("limit")
        return CommandResult.Success(EasyStorage.peekContents(limit))
    }
}
