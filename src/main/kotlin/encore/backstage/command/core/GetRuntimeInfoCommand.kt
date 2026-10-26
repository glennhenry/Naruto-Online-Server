package encore.backstage.command.core

import encore.EncoreIdentity
import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandResult
import game.GameIdentity
import game.context.ServerContext

/**
 * Command to get the [RuntimeInfo] of server.
 */
class GetRuntimeInfoCommand : Command {
    override val id: String = "get-runtime-info"
    override val name: String = "Get runtime info"
    override val description: String = """
        Get runtime details about the server.
    """.trimIndent()
    override val arguments: List<Argument> = emptyList()

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val info = getRuntimeInfo()

        return CommandResult.Success(
            buildString {
                appendLine("encoreVersion: ${info.encoreVersion}")
                appendLine("serverName: ${info.serverName}")
                appendLine("serverVersion: ${info.serverVersion}")
                appendLine("javaVersion: Java ${info.javaVersion}")
                appendLine("timezone: ${info.timezone}")
                appendLine("osDetails: ${info.osDetails}")
            }
        )
    }
}

/**
 * Produce the server [RuntimeInfo].
 */
fun getRuntimeInfo(): RuntimeInfo {
    val javaVersion = System.getProperty("java.version")
    val javaDate = System.getProperty("java.version.date")
    val timezone = System.getProperty("user.timezone")
    val osName = System.getProperty("os.name")
    val osVersion = System.getProperty("os.version")
    val osArch = System.getProperty("os.arch")

    return RuntimeInfo(
        encoreVersion = "${EncoreIdentity.Codename} ${EncoreIdentity.Version} (${EncoreIdentity.VersionDate})",
        serverName = GameIdentity.Title,
        serverVersion = GameIdentity.Version,
        javaVersion = "$javaVersion (${javaDate})",
        timezone = timezone,
        osDetails = "$osName $osVersion $osArch"
    )
}

/**
 * Server runtime information.
 *
 * @property encoreVersion e.g., `1.3.0`
 * @property serverName e.g., `Dead Zone Server`
 * @property serverVersion e.g., `1.0.5`
 * @property javaVersion e.g., `25.0.2 (2026-01-20)`
 * @property timezone e.g., `Asia/Jakarta`
 * @property osDetails e.g., `Windows 11.0 amd64`
 */
data class RuntimeInfo(
    val encoreVersion: String,
    val serverName: String,
    val serverVersion: String,
    val javaVersion: String,
    val timezone: String,
    val osDetails: String
)
