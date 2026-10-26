package encoreTest.utilsTest

import encore.utils.dataStructure.CircularList
import kotlin.test.Test
import kotlin.test.assertEquals

class CircularListTest {
    @Test
    fun test() {
        val list = CircularList<Int>(5)
        list.add(1)
        assertEquals(listOf(1), list.get(1))
        assertEquals(listOf(1), list.get(6))
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)
        assertEquals(listOf(5, 4), list.get(2))
        assertEquals(listOf(5, 4, 3, 2, 1), list.get(5))
        list.add(6)
        assertEquals(listOf(6, 5, 4), list.get(3))
        assertEquals(listOf(6, 5, 4, 3, 2), list.get(5))
    }
}
