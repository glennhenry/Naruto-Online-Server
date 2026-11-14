package game.core.utils

import java.io.File
import java.util.Locale

/**
 * Convert this amount of bytes into MB.
 * @param useBinary if this is `true`, 1 KB is 1024 bytes, else 1000 bytes.
 */
fun Number.bytesToMb(useBinary: Boolean = true): Double {
    val divisor = if (useBinary) 1024.0 * 1024.0 else 1_000_000.0
    return this.toDouble() / divisor
}

/**
 * Format this amount of bytes in MB.
 *
 * e.g., `(3.22 MB)`
 * @param useBinary if this is `true`, 1 KB is 1024 bytes, else 1000 bytes.
 */
fun Number.formatSizeInMb(useBinary: Boolean = true): String {
    val mb = this.bytesToMb(useBinary)
    return String.format(Locale.US, "(%.2f MB)", mb)
}

/**
 * Format this array size in MB.
 *
 * e.g., `(3.22 MB)`
 * @param useBinary if this is `true`, 1 KB is 1024 bytes, else 1000 bytes.
 */
fun ByteArray.formatSizeInMb(useBinary: Boolean = true): String {
    return this.size.formatSizeInMb(useBinary)
}

/**
 * Format this file size in MB.
 *
 * e.g., `(3.22 MB)`
 * @param useBinary if this is `true`, 1 KB is 1024 bytes, else 1000 bytes.
 */
fun File.formatSizeInMb(useBinary: Boolean = true): String {
    return this.length().formatSizeInMb(useBinary)
}
