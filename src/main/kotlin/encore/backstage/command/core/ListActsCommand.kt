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
 * Command to list all running stage acts on the server.
 */
class ListActsCommand : Command {
    override val id: String = "list-acts"
    override val name: String = "List acts"
    override val description: String = """
        List all stage acts running on the server.
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("limit", "Limit", "Limit the results", ArgumentType.Int, 20)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val limit = args.getInt("limit")
        val totalActs = serverContext.stageActDirector.getNumRunningActs()
        val acts = serverContext.stageActDirector.getAllRunningActs(limit)
        val now = TimeCenter.now()

        return CommandResult.Success(
            buildString {
                appendLine("Total running acts: $totalActs")
                appendLine("Showing results of ${acts.size} acts:")
                appendLine("now: ${detailedDateFormatter.format(now)}")
                acts.forEachIndexed { idx, act ->
                    appendLine("${idx + 1})   actId: ${act.actId}")
                    appendLine("     ownerId: ${act.ownerId}")
                    appendLine(
                        "     startedAt: ${detailedDateFormatter.format(act.startedAt)} (${
                            TimeCenter.formatToDurationString(
                                (now - act.startedAt)
                            )
                        } ago)"
                    )
                    appendLine(
                        "     firstPerformAt: ${detailedDateFormatter.format(act.firstPerformAt)} (${
                            TimeCenter.formatToDurationString(
                                (now - act.firstPerformAt)
                            )
                        } ago)"
                    )
                    appendLine(
                        "     prevPerformAt: ${detailedDateFormatter.format(act.prevPerformAt)} (${
                            TimeCenter.formatToDurationString(
                                (now - act.prevPerformAt)
                            )
                        } ago)"
                    )
                    appendLine(
                        "     nextPerformAt: ${detailedDateFormatter.format(act.nextPerformAt)} (in ${
                            TimeCenter.formatToDurationString(
                                (act.nextPerformAt - now)
                            )
                        })"
                    )
                    appendLine("     performCount: ${act.performCount}")
                    appendLine("     ——class: ${act.actClassName}")
                    appendLine("     ——describe: ${act.metadata}")
                    appendLine()
                }
            }
        )
    }
}
