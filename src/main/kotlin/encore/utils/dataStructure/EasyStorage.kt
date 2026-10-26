package encore.utils.dataStructure

import encore.utils.support.className
import encore.utils.toJsonString

/**
 * A globally available key-value pair storage made for
 * easy access of storing and retrieving any kind of data.
 *
 * This utility is useful to store any small or temporary data
 * from anywhere and allow access from any part of the server system.
 *
 * Use this for components that doesn't have a dedicated storage,
 * repository, or dependency channel; and don't justify one at the moment.
 */
object EasyStorage {
    private val store = mutableMapOf<String, Any>()

    /**
     * Get the value associated with [key]
     * @return `null` if not found.
     */
    fun get(key: String): Any? {
        return store[key]
    }

    /**
     * Get the value associated with [key]
     * @throws IllegalStateException if not found.
     */
    fun require(key: String): Any {
        return store[key] ?: error("Key '$key' required but not found in storage.")
    }

    /**
     * Associate the [key] with [value].
     * If [key] was already paired before, this will overwrite it.
     *
     * @return The previous value or `null` if the key wasn't present before.
     */
    fun put(key: String, value: Any): Any? {
        return store.put(key, value)
    }

    /**
     * Delete the [key] entry.
     * @return The removed value of [key] or `null` if nothing was removed.
     */
    fun delete(key: String): Any? {
        return store.remove(key)
    }

    /**
     * Peek through the contents of the storage and returns a formatted string.
     *
     * The string format relies on reflection to print any kind
     * of values including nested ones. The format will be in JSON string.
     *
     * This is mostly used to debug unknown and arbitrary objects.
     *
     * @param limit To limit the amount of values to be printed.
     */
    fun peekContents(limit: Int = 20): String {
        var remaining = minOf(store.size, limit)
        var num = 1
        return buildString {
            appendLine("Found ${store.size} entries on storage.")
            appendLine("Showing $remaining results:")
            val iterator = store.iterator()
            while (iterator.hasNext() && remaining > 0) {
                val (k, v) = iterator.next()
                appendLine("($num) $k (${v.className()}): ${v.toJsonString()}")
                remaining -= 1
                num += 1
            }
        }
    }
}
