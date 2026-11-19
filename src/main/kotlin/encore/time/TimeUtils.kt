package encore.time

import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

/**
 * Format this amount of milliseconds in seconds.
 *
 * e.g., `1,39s`
 */
fun Long.millisFormatInSeconds(): String {
    return this.milliseconds.toString(DurationUnit.SECONDS, decimals = 2)
}
