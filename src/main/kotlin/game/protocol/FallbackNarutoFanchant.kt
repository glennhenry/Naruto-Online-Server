package game.protocol

import encore.utils.hexString

/**
 * Represent Naruto Online fallback message.
 */
class FallbackNarutoFanchant(private val socket: SocketProtocol) : NarutoFanchant {
    override val type: String = "-1"
    override fun toString(): String {
        return "(commandId=${socket.commandId}, " +
                "index=${socket.index}, bytes(32)=${
                    socket.bytes.take(32).toByteArray().hexString()
                })"
    }
}
