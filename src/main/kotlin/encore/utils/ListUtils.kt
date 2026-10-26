package encore.utils

/**
 * Returns the first [amount] element of the list, or less if
 * the actual size of the list is smaller than [amount].
 */
fun <T> List<T>.peek(amount: Int): List<T> {
    val outSize = minOf(amount, size)
    return subList(0, outSize - 1)
}
