package game.protocol

/**
 * Represent the wire-level representation of Naruto Online messages.
 *
 * @property commandId Message's opcode
 * @property index Message's serial number
 * @property uin userId
 * @property role user role ???
 * @property serverId Server ID
 * @property bytes Payload bytes which may be encrypted.
 */
data class SocketProtocol(
    val commandId: UInt,
    val index: Int,
    val uin: Int,
    val role: Int,
    val serverId: Int,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SocketProtocol

        if (commandId != other.commandId) return false
        if (index != other.index) return false
        if (uin != other.uin) return false
        if (role != other.role) return false
        if (serverId != other.serverId) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = commandId.hashCode()
        result = 31 * result + index
        result = 31 * result + uin
        result = 31 * result + role
        result = 31 * result + serverId
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
