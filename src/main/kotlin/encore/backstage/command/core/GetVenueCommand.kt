package encore.backstage.command.core

import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.CommandResult
import encore.utils.toJsonString
import encore.venue.Venue
import game.context.ServerContext

/**
 * Command to retrieve all [Venue]'s configuration in JSON.
 */
class GetVenueCommand : Command {
    override val id: String = "get-venue"
    override val name: String = "Get venue config"
    override val description: String = """
        Retrieve the server active Venue configuration. 
        This does not include secret values.
    """.trimIndent()
    override val arguments: List<Argument> = emptyList()

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val venueJson = mapOf(
            "encore" to Venue.encore,
            "custom" to Venue.custom
        ).toJsonString()

        return CommandResult.Success(venueJson)
    }
}


