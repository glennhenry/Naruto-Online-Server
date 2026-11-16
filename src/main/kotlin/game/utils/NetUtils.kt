package game.utils

/**
 * Ensure the following strings starts with HTTP declaration.
 * @param useHttps
 */
fun String.ensureHttp(useHttps: Boolean = true): String {
    val trimmed = this.trim()
    return when {
        trimmed.startsWith("https://", ignoreCase = true) -> if (useHttps) trimmed else "http://" + trimmed.substring(8)
        trimmed.startsWith("http://", ignoreCase = true) -> if (useHttps) "https://" + trimmed.substring(7) else trimmed
        else -> if (useHttps) "https://$trimmed" else "http://$trimmed"
    }
}
