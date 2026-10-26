package encore.backstage.command

import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandRequest
import encore.backstage.command.types.CommandResult
import encore.backstage.command.types.ensureIds
import encore.fancam.Fancam
import encore.fancam.Tags
import game.context.ServerContext

/**
 * Dispatch and execute server registered commands.
 *
 * Server commands offers the ability to direct an instruction to the server
 * during runtime by registering and implementing command execution.
 *
 * It can be used for:
 * - Sending an item to particular player.
 * - Starting a game event.
 * - Getting diagnostic information about the server.
 * - ...and any other kind of actions.
 *
 * How to use:
 * - Implement [Command].
 * - Register the command with [register].
 *   All command should be registered in `Application.kt`.
 * - Via the backstage, use the command and input necessary arguments.
 * - Each command outputs a text message.
 *
 * See [ExampleCommand] for example.
 */
class CommandDispatcher {
    private val commands = mutableMapOf<String, Command>()

    /**
     * Register a server command.
     *
     * **Note**: All commands should be registered on server startup
     * on `Application.kt` and not in the middle of operation.
     *
     * @param command The command implementation to be registered.
     * @throws IllegalArgumentException When:
     * - Duplicate registered [Command.id] is found.
     * - Any duplicate argument IDs on [Command.arguments].
     */
    fun register(command: Command) {
        if (commands.containsKey(command.id)) {
            throw IllegalArgumentException("Found duplicate commandId '${command.id}'; Ensure ids are unique across other commands.")
        }
        command.arguments.ensureIds()
        commands[command.id] = command
    }

    /**
     * Handle raw command request from backstage client.
     * @return [CommandResult] that represents the outcome.
     * @throws IllegalArgumentException when required argument isn't found in [CommandRequest.arguments]
     *                                  or when any of the argument does not match the expected type.
     */
    suspend fun handleCommand(request: CommandRequest, serverContext: ServerContext): CommandResult {
        val command = commands[request.commandId] ?: return CommandResult.CommandNotFound(request.commandId)

        Fancam.info(Tags.Command) { "Received command '${request.commandId}' with ${request.arguments.size} arguments." }

        val args = ArgumentInputs(command.id, command.arguments, request.arguments)
        try {
            val result = command.execute(serverContext, args)
            Fancam.info(Tags.Command) { "Executed command '${request.commandId}' with $result" }
            return result
        } catch (e: Exception) {
            Fancam.error(e, Tags.Command) { "Scandal while executing the command '${request.commandId}'" }
            return CommandResult.Error("Scandal while executing the command '${request.commandId}': ${e.message}")
        }
    }

    /**
     * @return [Set] of registered `commandId`(s).
     */
    fun getAllRegisteredCommandsId(): Set<String> {
        return commands.keys.sorted().toSet()
    }

    /**
     * @return [Set] of registered commands.
     */
    fun getAllRegisteredCommands(): Set<Command> {
        return commands.values.sortedBy { it.name }.toSet()
    }
}
