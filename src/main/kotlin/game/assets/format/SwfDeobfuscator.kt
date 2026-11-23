package game.assets.format

import encore.fancam.Fancam
import java.io.File
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

/**
 * Utility to de-obfuscate Naruto Online SWFs.
 *
 * Use [export].
 */
class SwfDeobfuscator {
    companion object {
        /**
         * Utility to de-obfuscate Naruto Online SWFs.
         *
         * Naruto Online utilizes a fake SWF header to fool
         * tools like JPEXS. This function transform the SWF
         * into real usable SWF.
         *
         * From [inputFile], we will de-obfuscate this and produce
         * and output in the [outputDir], same file name.
         */
        fun export(inputFile: File, outputDir: String = "workspace/output") {
            val raw = inputFile.readBytes()
            File(outputDir).mkdirs()
            val target = File(outputDir, inputFile.name)

            if (!isObfuscatedSwf(raw)) {
                Fancam.debug { "'${inputFile.name}' is not obfuscated; copying as-is." }
                target.writeBytes(raw)
                return
            }

            // Output size is always 11 bytes smaller (19-byte fake header -> 8-byte real SWF header)
            val deobfuscated = ByteArray(raw.size - 11)

            // Skipped 8 bytes for fake SWF header
            // 1. Reconstruct 3-byte SWF signature ('FWS', 'CWS', or 'ZWS') from even indices: 8, 10, 12, 14, 16, 18
            for (pair in 0 until 3) {
                val low = raw[8 + pair * 4] and 0x0F
                val high = (raw[10 + pair * 4].toInt() and 0x0F) shl 4
                deobfuscated[pair] = low or high.toByte()
            }

            // 2. Copy 1-byte version + 4-byte uncompressed length from odd indices: 9, 11, 13, 15, 17
            for (idx in 0 until 5) {
                deobfuscated[3 + idx] = raw[9 + idx * 2]
            }

            // 3. Copy the remaining payload starting at offset 19 into deobfuscated starting at offset 8
            val bodyOffset = 19
            val bodyLength = raw.size - bodyOffset
            raw.copyInto(
                destination = deobfuscated,
                destinationOffset = 8,
                startIndex = bodyOffset,
                endIndex = raw.size
            )

            // 4. Bitwise-invert every odd byte in the first 128 bytes of the payload
            val scrambleLimit = minOf(128, bodyLength)
            for (i in 1 until scrambleLimit step 2) {
                deobfuscated[8 + i] = deobfuscated[8 + i].inv()
            }

            target.writeBytes(deobfuscated)
        }

        /**
         * Returns `true` if the [raw] is the bytes of obfuscated SWF.
         */
        fun isObfuscatedSwf(raw: ByteArray): Boolean {
            if (raw.size <= 19) return false

            // Starts with "ZWS"
            if (raw[0] != 'Z'.code.toByte() ||
                raw[1] != 'W'.code.toByte() ||
                raw[2] != 'S'.code.toByte()
            ) return false

            // Version must be 0
            if (raw[3] != 0.toByte()) return false

            // Uncompressed length must be 0xFFFFFFFF
            return raw[4] == 0xFF.toByte() &&
                    raw[5] == 0xFF.toByte() &&
                    raw[6] == 0xFF.toByte() &&
                    raw[7] == 0xFF.toByte()
        }
    }
}
