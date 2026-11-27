package game.protocol

import encore.network.fanchant.Fanchant
import encore.utils.hexString

/**
 * Represent Naruto Online fallback message.
 *
 * This handler is created to catch any `NarutoFanchant` with
 * unrecognized commandId, without it getting swallowed into
 * the generic `Fanchant` that is handled by `AllRounderHandler`.
 *
 * This fanchant intentionally uses `naruto-fallback` as type so
 * it will always be handled by [NarutoFallbackHandler]. Its actual
 * command ID can be seen from [toString] or [socket].
 *
 * Unless if a particular command ID is registered at [NarutoFanchantGuide],
 * but it has no handler, then the [NarutoFallbackHandler] won't be able to
 * catch it.
 */
data class FallbackNarutoFanchant(
    val socket: SocketProtocol
) : Fanchant {
    override val type: String = "naruto-fallback"
    override fun toString(): String {
        val name = ProtocolCmd.fromId(socket.commandId)
        return "(commandId=$name(${name?.id}), " +
                "index=${socket.index}, bytes(32)=${
                    socket.bytes.take(32).toByteArray().hexString()
                })"
    }
}
