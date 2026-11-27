package game.protocol.message

import game.login.LoginRequest
import game.protocol.NarutoFanchant

/**
 * Represent the domain message representation, which client
 * encodes using ProtoBuf.
 *
 * Every class that is used for request/response data must
 * inherit this for compatibility with request representation
 * [NarutoFanchant] and to create a response using [createResponse].
 *
 * The class definition should strictly follow the ProtoBuf definition
 * on client, in the same order, and marked as `@Serializable`.
 *
 * See example [LoginRequest]
 */
interface PbMessage
