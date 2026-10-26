package encore.fancam.formatter

import preciseDetailedDateFormatter
import encore.fancam.events.SimpleTrackEvent
import encore.fancam.events.TrackEvent
import encore.serialization.toJsonElement
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Formatter implementation for track event tailored for file output.
 *
 * Format is implemented by converting the track event into [SimpleTrackEvent]
 * and encoding it to JSON string.
 */
class TrackEventFancamFormatter : FancamFormatter<TrackEvent> {
    private val jsonSerializer = Json { prettyPrint = true }

    override fun format(event: TrackEvent): String {
        val jsonElement = SimpleTrackEvent(
            name = event.name,
            datetime = preciseDetailedDateFormatter.format(event.timestamp),
            data = event.data,
            tags = event.tags,
            source = event.source,
            note = event.note()
        ).toJsonElement(useReflection = true)
        return jsonSerializer.encodeToString(
            JsonElement.serializer(), jsonElement
        )
    }
}
