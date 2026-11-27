package game.protocol.response

import encore.serialization.Protobuf
import game.protocol.definition.NarutoFanchant
import game.protocol.TeaCipher
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

/**
 * Helper function to construct server response for client.
 *
 * This involves packaging the given payload of ProtoBuf format
 * and optionally encrypting them. The [fanchant] reference is
 * also needed to write the packet wire details.
 *
 * @param T Type of payload, should be ProtoBuf data class which is
 *          marked with `@Serializable`.
 * @param payload The payload.
 * @param fanchant Fanchant object received from handler context.
 * @param type Optional type number for client. This is typically only
 *             used for specific messages.
 * @param encrypt Whether to encrypt the payload. Server should only encrypt
 *                a particular message when the client is informed.
 */
inline fun <reified T> response(
    payload: T?, fanchant: NarutoFanchant<*>,
    type: Int = 0, encrypt: Boolean = true
): ByteArray {
    val outputBytes = ByteArrayOutputStream()
    val output = DataOutputStream(outputBytes)

    output.writeShort(2305)

    val pbBytes = if (payload != null) {
        if (encrypt) {
            TeaCipher().encrypt(Protobuf.encode(payload))
        } else {
            Protobuf.encode(payload)
        }
    } else {
        byteArrayOf()
    }

    output.writeInt(pbBytes.size)
    output.writeInt(fanchant.command.id.toInt())
    output.writeInt(fanchant.serial)
    output.writeInt(type)

    // client expects the header to be 38 bytes
    // even when the actually being used header is only 18 bytes
    // write 20 dummy bytes here
    output.write(ByteArray(20) { 0 })
    output.write(pbBytes)
    output.flush()
    return outputBytes.toByteArray()
}
