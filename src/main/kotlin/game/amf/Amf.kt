package game.amf

import flex.messaging.io.SerializationContext
import flex.messaging.io.amf.Amf3Output
import flex.messaging.validators.ClassDeserializationValidator
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * AMF encoder and decoder, a wrapper for Apache Flex API.
 */
object Amf {
    /**
     * Creates a [SerializationContext] for `AmfInput`.
     * This context is automatically set when this object is constructed.
     */
    fun createContext(): SerializationContext {
        return SerializationContext().apply {
            deserializationValidator = ClassDeserializationValidator().apply {
            }
            createASObjectForMissingType = true
        }
    }

    /**
     * Encode [obj] into AMF3 format.
     */
    fun encode(obj: Any?): ByteArray {
        val outStream = ByteArrayOutputStream()
        val amfOut = Amf3Output(createContext())
        amfOut.setOutputStream(outStream)
        amfOut.writeObject(obj)
        amfOut.flush()
        return outStream.toByteArray()
    }

    /**
     * Decode [bytes] which is in AMF3 format into [T].
     * Utilizes [CustomAmf3Input].
     */
    inline fun <reified T> decode(bytes: ByteArray): T? {
        val inStream = ByteArrayInputStream(bytes)
        val amfIn = CustomAmf3Input(createContext())
        amfIn.setInputStream(inStream)
        return amfIn.readObject() as? T
    }

    /**
     * Decode [inputStream] which is in AMF3 format into [T].
     * Utilizes [CustomAmf3Input].
     */
    inline fun <reified T> decode(inputStream: ByteArrayInputStream): T? {
        val amfIn = CustomAmf3Input(createContext())
        amfIn.setInputStream(inputStream)
        return amfIn.readObject() as? T
    }

    /**
     * Decode [bytes] which is in AMF3 format into [Any].
     * Utilizes [CustomAmf3Input].
     */
    fun decodeAny(bytes: ByteArray): Any? {
        val inStream = ByteArrayInputStream(bytes)
        val amfIn = CustomAmf3Input(createContext())
        amfIn.setInputStream(inStream)
        return amfIn.readObject()
    }

    /**
     * Decode [inputStream] which is in AMF3 format into [Any].
     * Utilizes [CustomAmf3Input].
     */
    fun decodeAny(inputStream: ByteArrayInputStream): Any? {
        val amfIn = CustomAmf3Input(createContext())
        amfIn.setInputStream(inputStream)
        return amfIn.readObject()
    }
}
