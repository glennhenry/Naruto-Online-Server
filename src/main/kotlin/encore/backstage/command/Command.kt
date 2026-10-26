package encore.backstage.command

import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandResult
import game.context.ServerContext

/**
 * Represents a server command that can be invoked to perform
 * a specific action in the server.
 *
 * Command works by an implementation of this interface.
 * Implement the [execute] method to carry the specific action for the command.
 *
 * - Use the command from the command backstage tool.
 * - Register your commands in `Application.kt`.
 *
 * See [ExampleCommand] for example.
 */
interface Command {
    /**
     * A unique identifier of the command.
     * Can use any kind of format or convention, but must be unique to other commands.
     *
     * For examples: `give-item`, `GetLog`, `Start Event`.
     */
    val id: String

    /**
     * A human-readable name for the command used as display.
     *
     * For examples: `Give Item`, `Get Recent Logs`, `Start an event`.
     */
    val name: String

    /**
     * An explanation about the command.
     *
     * Use this to explain the command's purpose or execution details.
     */
    val description: String

    /**
     * The list of arguments for this command.
     */
    val arguments: List<Argument>

    /**
     * Contain execution logic of the command.
     *
     * @param serverContext [ServerContext] to be used during execution.
     * @param args Input arguments. This could be empty if the user does not provide any.
     *
     * @return Result of command execution: [CommandResult].
     */
    suspend fun execute(serverContext: ServerContext, args: ArgumentInputs): CommandResult
}
