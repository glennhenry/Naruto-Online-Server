package game.protocol

import encore.network.fanchant.Fanchant
import encore.network.handler.FanchantHandler
import game.protocol.message.PbMessage

/**
 * Represent Naruto Online message.
 *
 * The Naruto Online message payload is encoded using ProtoBuf.
 * This fanchant implementation wraps Naruto Online message
 * including its payload and wire details.
 *
 * ### Handling
 *
 * This single message provides sufficient information for handling.
 * It includes a unique player ID and a server ID, which can accurately
 * determine the in-game player character since each player can only have
 * a single character on each server.
 *
 * Every message handler should inherit [FanchantHandler]
 * and declare `NarutoFanchant` as its `T`, while also providing
 * another type for [payload].
 *
 * Example: `FanchantHandler<NarutoFanchant<LoginRequest>>`
 *
 * **Important:** The handler should declare its type by using
 * any of the opcodes from [ProtocolCmd] and declare it through
 * [cmdtype] for compatibility.
 *
 * @param command Identifier that indicates the operation of this message.
 * @param serial Represent the packet's sequence number.
 * @param playerId Player unique identifier (also known as uin)
 * @param role unknown ???
 * @param serverId Denotes the server ID this packet is coming from
 * @param payload The payload, should be [PbMessage].
 */
data class NarutoFanchant<T>(
    val command: ProtocolCmd,
    val serial: Int,
    val playerId: Int,
    val role: Int,
    val serverId: Int,
    val payload: T
) : Fanchant {
    // instead of using plain opcode for handler routing,
    // construct a readable representation for better logging
    override val type: String = cmdtype(command)
}

/**
 * Creates a string type for handler routing purpose.
 *
 * To avoid our server logging random opcodes number, we use
 * the constant's name to better represent it.
 *
 * Handler should declare type using this.
 */
fun cmdtype(protocolCmd: ProtocolCmd): String {
    return "${protocolCmd.name}(${protocolCmd.id})"
}
