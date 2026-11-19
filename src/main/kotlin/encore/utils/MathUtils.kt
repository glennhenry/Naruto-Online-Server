package encore.utils

import com.toxicbakery.bcrypt.Bcrypt
import kotlin.io.encoding.Base64

/**
 * Hash the given [s] string using the Bcrypt function
 * with the specified salt [rounds].
 *
 * @return Hashed string in Base64 representation.
 */
fun hash(s: String, rounds: Int = 10): String {
    return Base64.encode(Bcrypt.hash(s, rounds))
}
