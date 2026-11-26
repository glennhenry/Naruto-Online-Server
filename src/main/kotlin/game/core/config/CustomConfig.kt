package game.core.config

import encore.annotation.runtime.VenueKey
import kotlinx.serialization.Serializable

/**
 * Custom application config definition.
 *
 * Place every venue-supplied configuration here, also annotate with [VenueKey].
 * After that, modify `venue.xml` accordingly from this data class entries.
 *
 * All field is preferred to be immutable.
 */
@Serializable
data class CustomConfig(
    /**
     * The current version of Naruto Online.
     * This affects the version of `resource.cfg` and `entry.swf`.
     */
    @VenueKey("version")
    val version: String = ""
)
