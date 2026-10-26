package encoreTest.backstage

import encore.backstage.command.Command
import encore.backstage.command.CommandDispatcher
import encore.backstage.command.ExampleCommand
import encore.backstage.command.types.*
import game.context.ServerContext
import kotlinx.coroutines.test.runTest
import testUtils.TestFancam
import testUtils.assertDoesNotFail
import kotlin.test.*

/**
 * Command dispatcher test and example of command implementation [encoreTest.backstage.ExampleGiveCommand].
 *
 * Does not need to test invalid request since parser already validates that.
 */
class CommandDispatcherTest {
    private val context = ServerContext.createForTest()

    @BeforeTest
    fun setup() {
        TestFancam.create()
    }

    @Test
    fun `argument creation success`() {
        assertDoesNotFail {
            Argument("abc", "ABC", "ABCDEFG", ArgumentType.String, "123")
            Argument("abc", "ABC", "ABCDEFG", ArgumentType.String)
        }
    }

    @Test
    fun `argument expected type mismatch with default should throw`() {
        assertFailsWith<IllegalArgumentException> {
            Argument("a", "a", "a", ArgumentType.String, 12)
        }
    }

    @Test
    fun `arguments ensureIds success`() {
        assertDoesNotFail {
            listOf(
                Argument("a", "a", "a", ArgumentType.String),
                Argument("b", "a", "a", ArgumentType.String),
            ).ensureIds()
        }
    }

    @Test
    fun `arguments ensureIds with duplicate id should throw`() {
        assertFailsWith<IllegalArgumentException> {
            listOf(
                Argument("dupe", "a", "a", ArgumentType.String),
                Argument("dupe", "a", "a", ArgumentType.String),
            ).ensureIds()
        }
    }

    @Test
    fun `argument inputs creation success`() {
        assertDoesNotFail {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String),
                    Argument("b", "a", "a", ArgumentType.String),
                ),
                inputArguments = mapOf(
                    "a" to "abc",
                    "b" to "123",
                )
            )
        }
    }

    @Test
    fun `argument inputs creation fails on missing argument should throw`() {
        assertFailsWith<IllegalArgumentException> {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String),
                    Argument("b", "a", "a", ArgumentType.String),
                ),
                inputArguments = mapOf(
                    "a" to "abc",
                    // "b" to "123",
                )
            )
        }
    }

    @Test
    fun `argument inputs creation fails on missing argument but has default success`() {
        assertDoesNotFail {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String),
                    Argument("b", "a", "a", ArgumentType.String, "abc"),
                ),
                inputArguments = mapOf(
                    "a" to "abc",
                    // "b" to "123",
                )
            )
        }
    }

    @Test
    fun `argument inputs creation fails on invalid type 1 should throw`() {
        assertFailsWith<IllegalArgumentException> {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String),
                    Argument("b", "a", "a", ArgumentType.Int),
                ),
                inputArguments = mapOf(
                    "a" to "abc",
                    "b" to "abc",
                )
            )
        }
    }

    @Test
    fun `argument inputs creation fails on invalid type strictly should throw`() {
        assertFailsWith<IllegalArgumentException> {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String),
                    Argument("b", "a", "a", ArgumentType.Int),
                ),
                inputArguments = mapOf(
                    "a" to "abc",
                    "b" to "12",
                )
            )
        }
    }

    @Test
    fun `argument inputs creation success when expected string got number`() {
        assertDoesNotFail {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String),
                ),
                inputArguments = mapOf(
                    "a" to "123"
                )
            )
        }
    }

    @Test
    fun `argument inputs creation success when expected string got double`() {
        assertDoesNotFail {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String),
                ),
                inputArguments = mapOf(
                    "a" to "1.0"
                )
            )
        }
    }

    @Test
    fun `argument inputs creation success when expected string got weird characters`() {
        assertDoesNotFail {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String),
                ),
                inputArguments = mapOf(
                    "a" to "_#$(@afowenfioa"
                )
            )
        }
    }

    @Test
    fun `argument inputs get success`() {
        val args = ArgumentInputs(
            commandId = "abc",
            expectedArguments = listOf(
                Argument("a", "a", "a", ArgumentType.String),
                Argument("b", "a", "a", ArgumentType.Int),
            ),
            inputArguments = mapOf(
                "a" to "abc",
                "b" to 12,
            )
        )
        assertEquals("abc", args.getString("a"))
        assertEquals(12, args.getInt("b"))
    }

    @Test
    fun `argument inputs get fail on invalid key`() {
        val args = ArgumentInputs(
            commandId = "abc",
            expectedArguments = listOf(
                Argument("a", "a", "a", ArgumentType.String),
                Argument("b", "a", "a", ArgumentType.String),
            ),
            inputArguments = mapOf(
                "a" to "abc",
                "b" to "def",
            )
        )
        assertFailsWith<IllegalArgumentException> {
            args.getString("c")
        }
    }

    @Test
    fun `argument inputs get fail on invalid type`() {
        val args = ArgumentInputs(
            commandId = "abc",
            expectedArguments = listOf(
                Argument("a", "a", "a", ArgumentType.String),
                Argument("b", "a", "a", ArgumentType.String),
            ),
            inputArguments = mapOf(
                "a" to "abc",
                "b" to "def",
            )
        )
        assertFailsWith<IllegalArgumentException> {
            args.getInt("b")
        }
    }

    @Test
    fun `argument inputs get and creation success to  use input when expected string and has default, but got number`() {
        assertDoesNotFail {
            ArgumentInputs(
                commandId = "abc",
                expectedArguments = listOf(
                    Argument("a", "a", "a", ArgumentType.String, ""),
                ),
                inputArguments = mapOf(
                    "a" to "3.0"
                    // note: using 3.0 without string is still valid
                    // there is no way to distinguish a JSON-decoded 3.0
                    // whether it is intended as a string or double
                )
            ).getString("a")
        }
    }

    @Test
    fun `command dispatcher register success`() = runTest {
        val dispatcher = CommandDispatcher()
        dispatcher.register(createCommand("cmd1"))
        dispatcher.register(createCommand("cmd2"))
        dispatcher.register(createCommand("cmd3"))
        dispatcher.getAllRegisteredCommandsId().containsAll(listOf("cmd1", "cmd2", "cmd3"))
    }

    @Test
    fun `command dispatcher register duplicate commandId fails`() = runTest {
        val dispatcher = CommandDispatcher()
        dispatcher.register(createCommand("cmd1"))
        dispatcher.register(createCommand("cmd2"))
        assertFailsWith<IllegalArgumentException> {
            dispatcher.register(createCommand("cmd2"))
        }
    }

    private fun createCommand(id: String): Command {
        return object : Command {
            override val id: String = id
            override val name: String = id.uppercase()
            override val description: String = "TestCommand of $id"
            override val arguments: List<Argument> = listOf()
            override suspend fun execute(serverContext: ServerContext, args: ArgumentInputs): CommandResult {
                return CommandResult.Success("execute() called on command ($id)")
            }
        }
    }

    @Test
    fun `command dispatcher handleCommand unregistered command returns command not found`() = runTest {
        val dispatcher = CommandDispatcher()
        val request = CommandRequest("cmd", mapOf())
        val result = dispatcher.handleCommand(request, context)
        assertTrue(result is CommandResult.CommandNotFound)
    }

    @Test
    fun `command dispatcher handleCommand with required arguments returns success`() = runTest {
        val dispatcher = CommandDispatcher()
        dispatcher.register(ExampleCommand())
        val request =
            CommandRequest(
                "xxx-example",
                mapOf(
                    "a" to "abc", "b" to "def", "c" to 1,
                    "d" to 2, "e" to 1.0, "f" to 1.0,
                    "g" to true, "h" to true
                )
            )
        val result = dispatcher.handleCommand(request, context)
        assertTrue(result is CommandResult.Success)
    }

    @Test
    fun `command dispatcher handleCommand with optional arguments returns success`() = runTest {
        val dispatcher = CommandDispatcher()
        dispatcher.register(ExampleCommand())

        val request =
            CommandRequest(
                "xxx-example",
                mapOf(
                    "a" to "abc", "b" to "def", "c" to 1,
                    "d" to 2, "e" to 1.0, "f" to 1.0,
                    "g" to true, "h" to true, "i" to 1
                )
            )
        val result = dispatcher.handleCommand(request, context)
        assertTrue(result is CommandResult.Success)
    }

    @Test
    fun `command dispatcher handleCommand simulates uncaught exception returns error`() = runTest {
        val dispatcher = CommandDispatcher()
        dispatcher.register(ExampleCommand())

        val request =
            CommandRequest(
                "xxx-example",
                mapOf(
                    "a" to "abc", "b" to "def", "c" to 1,
                    "d" to 2, "e" to 1.0, "f" to 1.0,
                    "g" to true, "h" to true, "i" to 2
                )
            )
        val result = dispatcher.handleCommand(request, context)
        assertTrue(result is CommandResult.Error)
    }

    @Test
    fun `command dispatcher handleCommand simulates failure returns execution failure`() = runTest {
        val dispatcher = CommandDispatcher()
        dispatcher.register(ExampleCommand())

        val request =
            CommandRequest(
                "xxx-example",
                mapOf(
                    "a" to "abc", "b" to "def", "c" to 1,
                    "d" to 2, "e" to 1.0, "f" to 1.0,
                    "g" to true, "h" to true, "i" to 3
                )
            )
        val result = dispatcher.handleCommand(request, context)
        assertTrue(result is CommandResult.ExecutionFailure)
    }
}
