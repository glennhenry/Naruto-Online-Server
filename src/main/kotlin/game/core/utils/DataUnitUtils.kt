package game.core.utils

import java.io.File

/**
 * Convert this amount of bytes into MB.
 */
fun Int.bytesToMb(): Double {
    return this.toDouble() / 1024 / 1024
}

/**
 * Convert this amount of bytes into MB.
 */
fun Long.bytesToMb(): Double {
    return this.toDouble() / 1024 / 1024
}

/**
 * Format this amount of bytes in MB.
 *
 * e.g., `(3.22 MB)`
 */
fun Int.formatSizeInMb(): String {
    return "(${"%.2f".format(this.bytesToMb())} MB)"
}

/**
 * Format this amount of bytes in MB.
 *
 * e.g., `(3.22 MB)`
 */
fun Long.formatSizeInMb(): String {
    return "(${"%.2f".format(this.bytesToMb())} MB)"
}

/**
 * Format this array size in MB.
 *
 * e.g., `(3.22 MB)`
 */
fun ByteArray.formatSizeInMb(): String {
    return "(${"%.2f".format(this.size.bytesToMb())} MB)"
}

/**
 * Format this file size in MB.
 *
 * e.g., `(3.22 MB)`
 */
fun File.formatSizeInMb(): String {
    return this.length().formatSizeInMb()
}
