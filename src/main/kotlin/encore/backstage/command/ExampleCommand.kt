package encore.backstage.command

import encore.backstage.command.types.Argument
import encore.backstage.command.types.ArgumentInputs
import encore.backstage.command.types.ArgumentType
import encore.backstage.command.types.CommandResult
import game.context.ServerContext

/**
 * An example implementation of command.
 * Does nothing meaningful beside introducing patterns and testing command invocation.
 */
class ExampleCommand : Command {
    override val id: String = "xxx-example"
    override val name: String = "Example"
    override val description: String = "This is merely a test command, does nothing actually."
    override val arguments: List<Argument> = listOf(
        Argument("a", "Argument A", "An example of required string argument", ArgumentType.String),
        Argument("b", "Argument B", "An example of optional string argument", ArgumentType.String, "DefaultString"),
        Argument("c", "Argument C", "An example of required integer argument", ArgumentType.Int),
        Argument("d", "Argument D", "An example of optional integer argument", ArgumentType.Int, 2),
        Argument("e", "Argument E", "An example of required double argument", ArgumentType.Double),
        Argument("f", "Argument F", "An example of optional double argument", ArgumentType.Double, 3.0),
        Argument("g", "Argument G", "An example of required boolean argument", ArgumentType.Boolean),
        Argument("h", "Argument H", "An example of optional boolean argument", ArgumentType.Boolean, true),
        Argument("i", "Argument I", "e.g., i=2 for uncaught exception, i=3 for execution failure", ArgumentType.Int, 1)
    )

    override suspend fun execute(serverContext: ServerContext, args: ArgumentInputs): CommandResult {
        val a = args.getString("a")
        val b = args.getString("b")
        val c = args.getInt("c")
        val d = args.getInt("d")
        val e = args.getDouble("e")
        val f = args.getDouble("f")
        val g = args.getBoolean("g")
        val h = args.getBoolean("h")
        val i = args.getInt("i")

        if (i == 2) throw Exception("Uncaught exception from i=2")
        if (i == 3) return CommandResult.ExecutionFailure("Execution failure from i=3")

        return CommandResult.Success("Executed 'test' command with args: a=$a, b=$b, c=$c, d=$d, e=$e, f=$f, g=$g, h=$h, i=$i")
    }
}
