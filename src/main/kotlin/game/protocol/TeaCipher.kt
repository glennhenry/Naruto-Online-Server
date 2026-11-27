package game.protocol

import java.nio.ByteBuffer
import java.nio.ByteOrder

val TEAEncryptKey = "d796c2542916b456".toByteArray()

/**
 * Encryption and decryption utility which leverages
 * the TEA algorithm.
 *
 * beware: entirely from AI mode, but untested!
 */
class TeaCipher(key: ByteArray = TEAEncryptKey) {
    private val k = IntArray(4)

    companion object {
        private const val DELTA = 0x9E3779B9.toInt()
        private const val ROUNDS = 16
    }

    init {
        require(key.size == 16) { "TEA requires a 128-bit (16-byte) key." }
        val buffer = ByteBuffer.wrap(key).order(ByteOrder.BIG_ENDIAN)
        for (i in 0 until 4) {
            k[i] = buffer.int
        }
    }

    private fun encryptBlock(v0In: Int, v1In: Int): Pair<Int, Int> {
        var v0 = v0In
        var v1 = v1In
        var sum = 0

        for (i in 0 until ROUNDS) {
            sum += DELTA
            v0 += ((v1 shl 4) + k[0]) xor (v1 + sum) xor ((v1 ushr 5) + k[1])
            v1 += ((v0 shl 4) + k[2]) xor (v0 + sum) xor ((v0 ushr 5) + k[3])
        }
        return Pair(v0, v1)
    }

    private fun decryptBlock(v0In: Int, v1In: Int): Pair<Int, Int> {
        var v0 = v0In
        var v1 = v1In
        var sum = DELTA * ROUNDS

        for (i in 0 until ROUNDS) {
            v1 -= ((v0 shl 4) + k[2]) xor (v0 + sum) xor ((v0 ushr 5) + k[3])
            v0 -= ((v1 shl 4) + k[0]) xor (v1 + sum) xor ((v1 ushr 5) + k[1])
            sum -= DELTA
        }
        return Pair(v0, v1)
    }

    fun encrypt(data: ByteArray): ByteArray {
        val paddedSize = ((data.size + 7) / 8) * 8
        val padded = data.copyOf(paddedSize)
        val inBuf = ByteBuffer.wrap(padded).order(ByteOrder.BIG_ENDIAN)
        val outBuf = ByteBuffer.allocate(paddedSize).order(ByteOrder.BIG_ENDIAN)

        while (inBuf.hasRemaining()) {
            val (c0, c1) = encryptBlock(inBuf.int, inBuf.int)
            outBuf.putInt(c0)
            outBuf.putInt(c1)
        }
        return outBuf.array()
    }

    fun decrypt(cipherText: ByteArray): ByteArray {
        require(cipherText.size % 8 == 0) { "Ciphertext length must be a multiple of 8 bytes." }
        val inBuf = ByteBuffer.wrap(cipherText).order(ByteOrder.BIG_ENDIAN)
        val outBuf = ByteBuffer.allocate(cipherText.size).order(ByteOrder.BIG_ENDIAN)

        while (inBuf.hasRemaining()) {
            val (p0, p1) = decryptBlock(inBuf.int, inBuf.int)
            outBuf.putInt(p0)
            outBuf.putInt(p1)
        }
        return outBuf.array()
    }
}
