package encore.backstage.command.core

import detailedDateFormatter
import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import encore.session.SessionSubunit
import encore.session.UserSession
import encore.time.TimeCenter
import game.context.ServerContext

/**
 * Command to list the active sessions of [SessionSubunit].
 */
class ListSessionCommand : Command {
    override val id: String = "list-session"
    override val name: String = "List sessions"
    override val description: String = """
        List all active sessions. This does not verify the sessions' validity.
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("limit", "Limit", "Limit the results", ArgumentType.Int, 20)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val limit = args.getInt("limit")
        val sb = StringBuilder()

        val sessions = serverContext.subunits.session.getAllActiveSessions(limit)
        if (sessions.isEmpty()) {
            sb.appendLine("No sessions.")
        } else {
            sb.appendLine("Found ${sessions.size} sessions:")
        }
        sessions.forEachIndexed { idx, ses ->
            sb.appendLine("${idx + 1}) ${formatSessionInfo(ses)}")
            sb.appendLine()
        }

        return CommandResult.Success(sb.toString())
    }
}

/**
 * Format [UserSession] into human-readable list.
 */
fun formatSessionInfo(ses: UserSession): String {
    val isExpired = if (TimeCenter.now() > ses.expiresAt) {
        "yes"
    } else {
        "no"
    }
    return """
        userId=${ses.userId}
           token=${ses.token}
           issuedAt=${detailedDateFormatter.format(ses.issuedAt)}
           expiresAt=${detailedDateFormatter.format(ses.expiresAt)}
           isExpired=$isExpired
    """.trimIndent()
}
