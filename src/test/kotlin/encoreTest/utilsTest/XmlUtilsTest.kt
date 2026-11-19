package encoreTest.utilsTest

import encore.utils.xml.XmlNode
import encore.utils.xml.get
import kotlin.test.Test
import kotlin.test.assertEquals

class XmlUtilsTest {
    @Test
    fun `test toXmlNode`() {
        val xml = """
            <parent id="1">
              <items>
                <item>a</item>
                <item>b</item>
                <item3 id="2">c</item3>
                <item4></item4>
                <item5/>
              </items>
            </parent>
        """.trimIndent()
        val node = XmlNode.fromString(xml)
        assertEquals("parent", node.name())
        assertEquals(1, node.attributes().size)
        assertEquals("1", node.attr("id"))
        assertEquals(null, node.value())
        assertEquals(1, node.children().size)
        assertEquals(5, node["items"]?.children()?.size)
        assertEquals("a", node["items"]["item"]?.value())
        assertEquals("c", node["items"]["item3"]?.value())
        assertEquals("2", node["items"]["item3"]?.attr("id"))
        assertEquals(null, node["items"]["item4"]?.value())
        assertEquals(null, node["items"]["item5"]?.value())
    }
}
