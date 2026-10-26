package encore.backstage.command.core

import encore.backstage.command.Command
import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import encore.utils.dataStructure.EasyStorage
import encore.utils.support.className
import encore.utils.toJsonString
import game.context.ServerContext

/**
 * Command to get a particular item stored in [EasyStorage].
 */
class GetEasyStorageItemCommand : Command {
    override val id: String = "get-storage-item"
    override val name: String = "Get storage item"
    override val description: String = """
        Retrieve an item from the easy storage by its key.
        Will return the value of the key printed as JSON string.
    """.trimIndent()
    override val arguments: List<Argument> = listOf(
        Argument("key", "Key", "The key of the item", ArgumentType.String)
    )

    override suspend fun execute(
        serverContext: ServerContext,
        args: ArgumentInputs
    ): CommandResult {
        val key = args.getString("key")
        val value = EasyStorage.get(key)
        return CommandResult.Success(
            buildString {
                if (value == null) {
                    appendLine("Key '$key' not found on storage.")
                } else {
                    appendLine("Found '$key' (${value.className()}): ${value.toJsonString()}")
                }
            }
        )
    }
}
