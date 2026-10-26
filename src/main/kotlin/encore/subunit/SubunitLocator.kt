package encore.subunit

import encore.utils.identifier.Ids
import game.context.PlayerContext
import game.context.PlayerSubunits
import game.context.ServerContext
import game.context.ServerSubunits

/**
 * A component that facilitates arbitrary subunits safekeeping.
 *
 * This is used to store the arbitrary amount and various types
 * of subunits that may exist in the server. This does not include
 * [PlayerSubunits] as they are stored in [PlayerContext], while
 * [ServerSubunits] is stored in [ServerContext].
 *
 * Methods:
 * - [save] to store a subunit, returning an ID for later retrieval.
 *   Optionally, use the overload to provide your own ID.
 * - [load] to load back the subunit, using its previously used ID.
 */
class SubunitLocator {
    /**
     * Holds all saved subunits.
     *
     * Access should generally go through [load].
     * Direct modification of this map is not intended.
     */
    val registry = mutableMapOf<String, Subunit<*>>()

    /**
     * Save a subunit in the locator.
     * @return A unique string (UUID) identifying the subunit
     *         to retrieve it back via [load].
     */
    fun save(subunit: Subunit<*>): String {
        val id = Ids.uuid()
        registry[id] = subunit
        return id
    }

    /**
     * Save a subunit in the locator using the provided [id].
     */
    fun save(id: String, subunit: Subunit<*>) {
        registry[id] = subunit
    }

    /**
     * Load a previously saved subunits by its unique [id].
     *
     * @param T Type of the subunit.
     * @param id Unique identifier self-provided or returned from [save].
     * @return The subunit as [T] or `null` if not found.
     * @throws ClassCastException if actual subunit's type differ from [T].
     */
    inline fun <reified T : Subunit<*>> load(id: String): T? {
        val subunit = registry[id] ?: return null
        return subunit as T
    }
}
