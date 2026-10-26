package encore.backstage.command.core

import detailedDateFormatter
import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import encore.time.TimeCenter
import game.context.ServerContext

/**
 * Command to get the details of session by its token.
 */
class VerifySessionCommand : Command {
    override val id: String = "verify-session"
    override val name: String = "Verify session"
    override val description: String = """
        Verify a session by its token and return its details.
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("token", "Token", "Session unique token", ArgumentType.String)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val token = args.getString("token")
        if (token.isBlank()) {
            return CommandResult.ExecutionFailure("Please fill the session token.")
        }

        val ses = serverContext.subunits.session.getSession(token)
        if (ses != null) {
            return CommandResult.Success(
                buildString {
                    appendLine("Session valid:")
                    appendLine("  userId: ${ses.userId}")
                    appendLine("  token: ${ses.token}")
                    appendLine("  issuedAt: ${detailedDateFormatter.format(ses.issuedAt)}")
                    appendLine("  expiresAt: ${detailedDateFormatter.format(ses.expiresAt)}")
                    appendLine("  lifetime: ${TimeCenter.formatToDurationString(ses.lifetime)}")
                    appendLine("  baseDuration: ${ses.baseDuration.inWholeMinutes} minutes")
                }
            )
        } else {
            return CommandResult.Success(
                "Session with token '$token' not found or invalid."
            )
        }
    }
}
