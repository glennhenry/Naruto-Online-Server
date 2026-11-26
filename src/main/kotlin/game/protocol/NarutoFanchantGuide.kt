package game.protocol

import encore.fancam.Fancam
import encore.network.fanchant.Fanchant
import encore.network.fanchant.guide.DecodeResult
import encore.network.fanchant.guide.FanchantGuide
import encore.serialization.Protobuf
import encore.utils.startsWithBytes
import game.login.LoginRequest
import java.io.DataInputStream

/**
 * Decode Naruto Online messages and materialize it into
 * one of the registered message in [materialize], else use
 * the [FallbackNarutoFanchant].
 *
 * It also decrypt the data as needed.
 */
class NarutoFanchantGuide : FanchantGuide<SocketProtocol> {
    override fun verify(data: ByteArray): Boolean {
        return data.startsWithBytes(byteArrayOf(0x09, 0x01))
    }

    override fun tryDecode(data: ByteArray): DecodeResult<SocketProtocol> {
        val input = DataInputStream(data.inputStream())
        input.readShort() // magic 2305
        val bytesLen = input.readInt()
        // client writes as UInt, and there is commandId above 2,1 billion
        val commandId = input.readInt()
        val index = input.readInt()
        input.readInt() // 1
        input.readDouble() // 0
        val selfuin = input.readInt()
        val selfRole = input.readShort()
        val serId = input.readShort()
        input.readInt() // 0
        val bytes = input.readNBytes(bytesLen)

        return DecodeResult.Success(
            SocketProtocol(
                commandId = commandId.toUInt(),
                index = index,
                uin = selfuin,
                role = selfRole.toInt(),
                serverId = serId.toInt(),
                bytes = bytes
            )
        )
    }

    override fun materialize(decoded: SocketProtocol): Fanchant {
        val bytes = if (decoded.commandId != ProtocolCmdDef.PT_LOGIN ||
            decoded.commandId != ProtocolCmdDef.CMD_GETENCRYPT_LIST
        ) {
            // decrypt TEA...
            decoded.bytes
        } else {
            decoded.bytes
        }
        val msg = when (decoded.commandId) {
            ProtocolCmdDef.PT_LOGIN -> Protobuf.decode<LoginRequest>(bytes)
            else -> {
                Fancam.warn {
                    "Unregistered commandId: '${decoded.commandId}'; " +
                            "please check ProtocolCmdDef and register it here."
                }
                FallbackNarutoFanchant(decoded)
            }
        }
        return msg
    }
}
