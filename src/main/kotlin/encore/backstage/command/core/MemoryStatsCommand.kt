package encore.backstage.command.core

import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandResult
import game.context.ServerContext
import kotlinx.serialization.Serializable

/**
 * Command to get the information of server memory.
 */
class MemoryStatsCommand : Command {
    override val id: String = "mem-stats"
    override val name: String = "Memory stats"
    override val description: String = """
        Get the server memory usage stats.
    """.trimIndent()
    override val arguments: List<Argument> = emptyList()

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val stats = getMemoryStats()
        return CommandResult.Success(
            buildString {
                appendLine("max JVM memory: ${stats.maxMemory / 1_000_000} MB")
                appendLine("current JVM memory: ${stats.totalMemory / 1_000_000} MB")
                appendLine("free JVM memory: ${stats.freeMemory / 1_000_000} MB")
                appendLine("used JVM memory: ${stats.usedMemory / 1_000_000} MB")
            }
        )
    }
}

/**
 * Produce the server [MemoryStats].
 */
fun getMemoryStats(): MemoryStats {
    val runtime = Runtime.getRuntime()
    val maxMemory = runtime.maxMemory()
    val totalMemory = runtime.totalMemory()
    val freeMemory = runtime.freeMemory()
    val usedMemory = totalMemory - freeMemory
    return MemoryStats(maxMemory, totalMemory, freeMemory, usedMemory)
}

/**
 * Details about server's memory.
 *
 * **Note**: Each of the values are in bytes.
 *
 * @property maxMemory Maximum memory the JVM will attempt to use.
 * @property totalMemory The total amount of memory currently in JVM.
 * @property freeMemory The amount of free memory in the JVM.
 * @property usedMemory The amount of used memory in the JVM.
 */
@Serializable
data class MemoryStats(
    val maxMemory: Long,
    val totalMemory: Long,
    val freeMemory: Long,
    val usedMemory: Long,
)
