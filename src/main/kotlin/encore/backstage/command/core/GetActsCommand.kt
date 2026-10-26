package encore.backstage.command.core

import detailedDateFormatter
import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import encore.time.TimeCenter
import game.context.ServerContext

class GetActsCommand : Command {
    override val id: String = "get-acts"
    override val name: String = "Get acts"
    override val description: String = """
        Get information about a running stage act by its actId.
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("actId", "Act ID", "Unique identifier of the running stage act", ArgumentType.String)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val actId = args.getString("actId")
        val act = serverContext.stageActDirector.getAct(actId)
        val now = TimeCenter.now()

        if (act == null) {
            return CommandResult.Success("actId '$actId' is not found or not running.")
        } else {
            return CommandResult.Success(
                buildString {
                    appendLine("Found act currently running:")
                    appendLine("now: ${detailedDateFormatter.format(now)}")
                    appendLine("actId: ${act.actId}")
                    appendLine("ownerId: ${act.ownerId}")
                    appendLine(
                        "startedAt: ${detailedDateFormatter.format(act.startedAt)} (${
                            TimeCenter.formatToDurationString(
                                (now - act.startedAt)
                            )
                        } ago)"
                    )
                    appendLine(
                        "firstPerformAt: ${detailedDateFormatter.format(act.firstPerformAt)} (${
                            TimeCenter.formatToDurationString(
                                (now - act.firstPerformAt)
                            )
                        } ago)"
                    )
                    appendLine(
                        "prevPerformAt: ${detailedDateFormatter.format(act.prevPerformAt)} (${
                            TimeCenter.formatToDurationString(
                                (now - act.prevPerformAt)
                            )
                        } ago)"
                    )
                    appendLine(
                        "nextPerformAt: ${detailedDateFormatter.format(act.nextPerformAt)} (in ${
                            TimeCenter.formatToDurationString(
                                (act.nextPerformAt - now)
                            )
                        })"
                    )
                    appendLine("performCount: ${act.performCount}")
                    appendLine("——class: ${act.actClassName}")
                    appendLine("——describe: ${act.metadata}")
                }
            )
        }
    }
}
