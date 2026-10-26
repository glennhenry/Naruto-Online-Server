package game.config

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
    val dummy: Int = 0
)
