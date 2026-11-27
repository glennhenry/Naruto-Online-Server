package game.startup.others

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

/**
 * GetEncryptList response
 * @property isEncrypt Whether further communication between client and server
 *                     should be encrypted or not.
 * @property exceptionCmd The list of commands that is excluded from encryption.
 */
@Serializable
data class GetEncryptListResponse(
    val ret: RetInfo = RetInfo(0, ""),
    val isEncrypt: Boolean,
    val exceptionCmd: List<UInt>
)
