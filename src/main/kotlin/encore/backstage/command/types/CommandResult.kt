package encore.backstage.command.types

/**
 * Represents the outcome of executing a server command.
 */
sealed class CommandResult(val message: String = "") {
    /**
     * The command executed successfully.
     */
    class Success(message: String) : CommandResult(message) {
        override fun toString(): String = "CommandResult.Success"
    }

    /**
     * Command execution fails due to a logical or domain error (e.g., player not found, invalid item).
     */
    class ExecutionFailure(message: String) : CommandResult(message) {
        override fun toString(): String = "CommandResult.ExecutionFailure"
    }

    /**
     * The command is not registered.
     */
    class CommandNotFound(message: String) : CommandResult(message) {
        override fun toString(): String = "CommandResult.CommandNotFound"
    }

    /**
     * An unexpected error that is uncaught within the command's execution logic.
     */
    class Error(message: String) : CommandResult(message) {
        override fun toString(): String = "CommandResult.Error"
    }
}
