package game.protocol

import encore.network.fanchant.Fanchant
import game.login.LoginRequest

/**
 * Represent Naruto Online message.
 *
 * The Naruto Online message is encoded using ProtoBuf.
 * Each fanchant implementation is not intended to be constructed directly,
 * but through decoding pipeline by [NarutoFanchantGuide].
 * The pipeline involves ProtoBuf decoding at the end.
 *
 * **Important:** each fanchant must describe the fields
 * accordingly as client. Because ProtoBuf cares about field order,
 * you should also pay attention to its declaration. Typically,
 * you put those fields in the constructor.
 *
 * See example: [LoginRequest]
 */
interface NarutoFanchant : Fanchant
