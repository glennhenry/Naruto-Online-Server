package encore.backstage.command.types

/**
 * Describe an argument of a command.
 *
 * A valid argument:
 * - [id] must be unique with other arguments.
 * - If [defaultValue] is filled, it must match [type].
 *
 * @property id Unique identifier of the argument. Must be unique to other arguments.
 *              This will be used as the key to access input arguments by command implementations.
 * @property name Display name of the argument.
 * @property description Describe the details of this argument and/or give an example.
 *                       This will be used for the text input's placeholder.
 * @property type Expected type of this argument.
 * @property defaultValue Optional default value for this argument.
 *                        Leaving this null means the argument is required.
 *                        Setting a non-null value turns this argument into optional
 *                        that will be defaulted if no input is given.
 * @throws IllegalArgumentException during init on type mismatch between [type] and [defaultValue].
 */
data class Argument(
    val id: String,
    val name: String,
    val description: String,
    val type: ArgumentType,
    val defaultValue: Any? = null
) {
    init {
        fun errMsg(id: String, type: ArgumentType, defaultValue: Any): String =
            "Type mismatch of command argument '$id' with expected type " +
                    "'$type' but got '${defaultValue::class.simpleName}'"

        when (type) {
            ArgumentType.String -> {
                if (defaultValue != null) {
                    require(defaultValue is String) { errMsg(id, type, defaultValue) }
                }
            }

            ArgumentType.Int -> {
                if (defaultValue != null) {
                    require(defaultValue is Int) { errMsg(id, type, defaultValue) }
                }
            }

            ArgumentType.Double -> {
                if (defaultValue != null) {
                    require(defaultValue is Double) { errMsg(id, type, defaultValue) }
                }
            }

            ArgumentType.Boolean -> {
                if (defaultValue != null) {
                    require(defaultValue is Boolean) { errMsg(id, type, defaultValue) }
                }
            }
        }
    }
}

/**
 * Ensure all ids of [Argument] are unique.
 * @throws IllegalArgumentException if duplicate ID is found.
 */
fun List<Argument>.ensureIds() {
    val map = mutableMapOf<String, String>()
    for ((id, name) in this) {
        val existing = map.put(id, name)
        if (existing != null) {
            throw IllegalArgumentException(
                "Duplicate argument IDs found between '$name' and '$existing' for id=$id"
            )
        }
    }
}
