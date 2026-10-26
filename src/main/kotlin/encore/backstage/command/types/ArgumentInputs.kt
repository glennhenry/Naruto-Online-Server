package encore.backstage.command.types

import encore.utils.support.className
import encore.backstage.command.Command

/**
 * Represent the clean input arguments of a command invocation.
 *
 * The constructor runs verification of:
 * - Ensure arguments with no default exists
 * - Ensure input argument's type match as the expected
 *
 * Command implementation can retrieve each argument's value with [get].
 * Through this method, **it is guaranteed for all arguments to be present**.
 *
 * @param inputArguments The raw input arguments.
 * @property commandId The `commandId` this input corresponds to.
 * @property expectedArguments The argument schema of the command. This is
 *                             [Command.arguments] the which should already be verified.
 * @throws IllegalArgumentException when required argument isn't found in [inputArguments]
 *                                  or when any of argument's type does not match the expected.
 */
class ArgumentInputs(
    private val commandId: String,
    private val expectedArguments: List<Argument>,
    inputArguments: Map<String, Any?>
) {
    private val arguments = mutableMapOf<String, Any>()

    init {
        for ((id, _, _, type, defaultValue) in expectedArguments) {
            val inputArg = inputArguments[id]
            if (inputArg == null && defaultValue == null) {
                throw IllegalArgumentException(
                    "Missing a required argument " +
                            "'$id' for command '$commandId'"
                )
            }
            when (type) {
                // if type is string, any values are valid unless its blank string
                ArgumentType.String -> {
                    if (inputArg != null && inputArg.toString().isNotBlank()) {
                        arguments[id] = inputArg.toString()
                    } else {
                        arguments[id] = defaultValue!!
                    }
                    continue
                }

                ArgumentType.Int -> {
                    if (inputArg !is Int && defaultValue == null) {
                        throw IllegalArgumentException(
                            "Type mismatch on argument '$id' for command '$commandId'; " +
                                    "expecting '${type.name}' got '${inputArg.className()}': $inputArg"
                        )
                    }
                }

                ArgumentType.Double -> {
                    if (inputArg !is Double && defaultValue == null) {
                        throw IllegalArgumentException(
                            "Type mismatch on argument '$id' for command '$commandId'; " +
                                    "expecting '${type.name}' got '${inputArg.className()}': $inputArg"
                        )
                    }
                }

                ArgumentType.Boolean -> {
                    if (inputArg !is Boolean && defaultValue == null) {
                        throw IllegalArgumentException(
                            "Type mismatch on argument '$id' for command '$commandId'; " +
                                    "expecting '${type.name}' got '${inputArg.className()}': $inputArg"
                        )
                    }
                }
            }
            arguments[id] = inputArg ?: defaultValue!!
        }
    }

    /**
     * Get the [String]-typed argument identified by [id].
     *
     * It is guaranteed that argument will present and it's type
     * will match as the expected, unless [id] does not conform to
     * listed argument's ids.
     *
     * @param id Identifier of the argument, which is [Argument.id].
     * @return The argument's value.
     * @throws IllegalArgumentException if argument exist but doesn't match the expected type.
     */
    fun getString(id: String): String {
        val value = get(id)
        val casted = value as? String
        if (casted == null) {
            val expected = expectedArguments.find { it.id == id }!!
            if (expected.type != ArgumentType.String) {
                throw IllegalArgumentException(
                    "Argument '$id' is not String-typed but '${expected.type}'"
                )
            } else {
                throw IllegalStateException(
                    "Unexpected error: argument '$id' can't be casted to String when it is expected to be; " +
                            "all expected arguments: ${expectedArguments.joinToString()}; " +
                            "This failure should have happened during constructor."
                )
            }
        }
        return casted
    }

    /**
     * Get the [Int]-typed argument identified by [id].
     *
     * It is guaranteed that argument will present and it's type
     * will match as the expected, unless [id] does not conform to
     * listed argument's ids.
     *
     * @param id Identifier of the argument, which is [Argument.id].
     * @return The argument's value.
     * @throws IllegalArgumentException if argument exist but doesn't match the expected type.
     */
    fun getInt(id: String): Int {
        val value = get(id)
        val casted = value as? Int
        if (casted == null) {
            val expected = expectedArguments.find { it.id == id }!!
            if (expected.type != ArgumentType.Int) {
                throw IllegalArgumentException(
                    "Argument '$id' is not Int-typed but '${expected.type}'"
                )
            } else {
                throw IllegalStateException(
                    "Unexpected error: argument '$id' can't be casted to Int when it is expected to be; " +
                            "all expected arguments: ${expectedArguments.joinToString()}; " +
                            "This failure should have happened during constructor."
                )
            }
        }
        return casted
    }

    /**
     * Get the [Double]-typed argument identified by [id].
     *
     * It is guaranteed that argument will present and it's type
     * will match as the expected, unless [id] does not conform to
     * listed argument's ids.
     *
     * @param id Identifier of the argument, which is [Argument.id].
     * @return The argument's value.
     * @throws IllegalArgumentException if argument exist but doesn't match the expected type.
     */
    fun getDouble(id: String): Double {
        val value = get(id)
        val casted = value as? Double
        if (casted == null) {
            val expected = expectedArguments.find { it.id == id }!!
            if (expected.type != ArgumentType.Double) {
                throw IllegalArgumentException(
                    "Argument '$id' is not Double-typed but '${expected.type}'"
                )
            } else {
                throw IllegalStateException(
                    "Unexpected error: argument '$id' can't be casted to Double when it is expected to be; " +
                            "all expected arguments: ${expectedArguments.joinToString()}; " +
                            "This failure should have happened during constructor."
                )
            }
        }
        return casted
    }

    /**
     * Get the [Boolean]-typed argument identified by [id].
     *
     * It is guaranteed that argument will present and it's type
     * will match as the expected, unless [id] does not conform to
     * listed argument's ids.
     *
     * @param id Identifier of the argument, which is [Argument.id].
     * @return The argument's value.
     * @throws IllegalArgumentException if argument exist but doesn't match the expected type.
     */
    fun getBoolean(id: String): Boolean {
        val value = get(id)
        val casted = value as? Boolean
        if (casted == null) {
            val expected = expectedArguments.find { it.id == id }!!
            if (expected.type != ArgumentType.Boolean) {
                throw IllegalArgumentException(
                    "Argument '$id' is not Boolean-typed but '${expected.type}'"
                )
            } else {
                throw IllegalStateException(
                    "Unexpected error: argument '$id' can't be casted to Boolean when it is expected to be; " +
                            "all expected arguments: ${expectedArguments.joinToString()}; " +
                            "This failure should have happened during constructor."
                )
            }
        }
        return casted
    }

    private fun get(id: String): Any {
        val value = arguments[id] ?: run {
            // requested argument not found
            // check if the argument id is in the schema
            if (expectedArguments.find { it.id == id } == null) {
                // argument id is not in schema, this means wrong input
                throw IllegalArgumentException("Argument '$id' not found on command '$commandId'")
            } else {
                // argument id is in schema, but not in clean arguments
                // unexpected error: constructor should have thrown an error earlier
                throw IllegalStateException(
                    "Unexpected absence of argument '$id' on command '$commandId'; " +
                            "all clean arguments: $arguments"
                )
            }
        }
        return value
    }
}
