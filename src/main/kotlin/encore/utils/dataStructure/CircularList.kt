package encore.utils.dataStructure

import kotlin.math.min

/**
 * Create a circular list of [size].
 *
 * The circular list is:
 * - A list with limited size.
 * - Provide retrieval of the last N elements.
 * - Adding more element beyond its size will overwrite the oldest element.
 *
 * Operation:
 * - [add]: to add an element into the list.
 * - [get]: get the last N elements of the list. The last element added
 *          to the list is the first element returned in the result.
 *
 * Example:
 *
 * ```
 * val list = CircularList<String>(5)
 * list.add("a")
 * list.get(1)    // ["a"]
 *
 * list.add("b")
 * list.add("c")
 * list.add("d")
 * list.add("e")
 * list.get(2)    // ["e", "d"]
 * list.get(5)    // ["e", "d", "c", "b", "a"]
 *
 * list.add("f")
 * list.get(5)    // ["f", "e", "d", "c", "b"]
 * ```
 *
 * @param T The type of element.
 * @property size The size of the list.
 */
class CircularList<T>(val size: Int) {
    private val arr = mutableListOf<T>()
    private var nextElementIndex = 0

    /**
     * Add [item] to the list.
     *
     * When the list is full, the oldest element will be overwritten.
     */
    fun add(item: T) {
        if (nextElementIndex == size) {
            nextElementIndex = 0
            arr[nextElementIndex] = item
        } else {
            arr.add(item)
        }
        nextElementIndex++
    }

    /**
     * Get as much as [amount] from the list.
     *
     * A higher number of `amount` than the actual number of elements
     * on the list or more than the list capacity will just return every
     * elements of the list.
     */
    fun get(amount: Int): List<T> {
        var latestElementIndex = nextElementIndex - 1
        val res = mutableListOf<T>()
        val amountToTake = min(arr.size, amount)
        while (res.size < amountToTake) {
            res.add(arr[latestElementIndex])
            latestElementIndex--
            if (latestElementIndex == -1) {
                latestElementIndex = size - 1
            }
        }
        return res
    }

    /**
     * Returns `true` if the list is empty.
     */
    fun isEmpty(): Boolean {
        return arr.isEmpty()
    }

    override fun toString(): String {
        return arr.toString()
    }
}
