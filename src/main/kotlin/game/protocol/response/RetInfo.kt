package game.protocol.response

import kotlinx.serialization.Serializable

/**
 * Representation the client's request outcome, included in many server responses.
 *
 * Depending on the client's expectation, the server should use the
 * appropriate error code. Use the error code 0 if no error occured.
 */
@Serializable
data class RetInfo(val code: Int, val msg: String)
