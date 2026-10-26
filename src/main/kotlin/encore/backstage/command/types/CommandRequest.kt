package encore.backstage.command.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

/**
 * Represent a raw command request sent by the backstage tool.
 *
 * This model is produced from HTTP request, so it is unverified:
 * - The [commandId] may not correspond to the registered commands.
 * - The [arguments] isn't guaranteed to contain every required arguments.
 * - Each type of arguments isn't guaranteed to match the argument's expected type.
 */
@Serializable
data class CommandRequest(
    val commandId: String,
    @Serializable(SimpleAnyMapSerializer::class)
    val arguments: Map<String, Any>
)

object SimpleAnyMapSerializer : KSerializer<Map<String, Any>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SimpleAnyMap")

    override fun serialize(encoder: Encoder, value: Map<String, Any>) {
        throw UnsupportedOperationException("Not intended for serialization")
    }

    override fun deserialize(decoder: Decoder): Map<String, Any> {
        val jsonDecoder = decoder as? JsonDecoder ?: error("This serializer only works with JSON")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        return jsonObject.mapValues { (_, value) -> jsonElementToNative(value) }
    }

    private fun jsonElementToNative(element: JsonElement): Any {
        return when (element) {
            is JsonPrimitive -> {
                element.intOrNull?.let { return it }
                element.doubleOrNull?.let { return it }
                element.booleanOrNull?.let { return it }
                element.content
            }
            is JsonObject -> element.mapValues { jsonElementToNative(it.value) }
            is JsonArray -> element.map { jsonElementToNative(it) }
        }
    }
}
