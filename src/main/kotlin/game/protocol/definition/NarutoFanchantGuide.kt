package game.protocol.definition

import encore.fancam.Fancam
import encore.network.fanchant.Fanchant
import encore.network.fanchant.guide.DecodeResult
import encore.network.fanchant.guide.FanchantGuide
import encore.serialization.Protobuf
import encore.utils.startsWithBytes
import game.character.RecommendRoleNameRequest
import game.gdpr.GetRoleRequest
import game.startup.login.LoginRequest
import game.telemetry.ReportLog
import java.io.DataInputStream

/**
 * Decode Naruto Online messages and materialize it into
 * [NarutoFanchant] with one of the payload message in [materialize].
 * If the payload is not recognized, it fallbacks to [FallbackNarutoFanchant].
 */
class NarutoFanchantGuide : FanchantGuide<SocketProtocol> {
    override fun verify(data: ByteArray): Boolean {
        // packet always starts with magic number 2305
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
        val bytes = if (decoded.commandId == ProtocolCmd.PT_LOGIN.id ||
            decoded.commandId == ProtocolCmd.CMD_GETENCRYPT_LIST.id
        ) {
            decoded.bytes
        } else {
            // encryption disabled
            // TeaCipher().decrypt(decoded.bytes)
            decoded.bytes
        }

        val command = ProtocolCmd.fromId(decoded.commandId)

        // map each opcode to protobuf decoding a certain payload class
        // add more here...
        // (and dont forget to create a handler afterward, so it will be routed there)
        val payload = when (command) {
            ProtocolCmd.PT_LOGIN -> Protobuf.decode<LoginRequest>(bytes)
            ProtocolCmd.CMD_GETENCRYPT_LIST -> null
            ProtocolCmd.SYS_REPORT -> Protobuf.decode<ReportLog>(bytes)
            ProtocolCmd.REPORT_GDPR_CHECK -> null
            ProtocolCmd.CMD_GET_ROLE -> Protobuf.decode<GetRoleRequest>(bytes)
            ProtocolCmd.CMD_RECOMMEND_NAME -> Protobuf.decode<RecommendRoleNameRequest>(bytes)
            else -> {
                Fancam.warn {
                    "Discovered unrecognized commandId: '${decoded.commandId}'"
                }
                return FallbackNarutoFanchant(decoded)
            }
        }

        return NarutoFanchant(
            command = command,
            serial = decoded.index,
            playerId = decoded.uin,
            role = decoded.role,
            serverId = decoded.serverId,
            payload = payload
        )
    }
}
