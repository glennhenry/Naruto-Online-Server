import encore.fancam.Fancam
import encore.fancam.Tags
import encore.utils.toJsonString
import encore.utils.xml.XmlNode
import encore.utils.xml.get
import io.ktor.utils.io.streams.asInput
import io.netty.handler.codec.compression.ZlibDecoder
import kotlinx.coroutines.test.runTest
import org.w3c.dom.Element
import testUtils.TestFancam
import java.io.File
import java.util.zip.InflaterInputStream
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Playground for quick testing and code run
 *
 * .\gradlew test --tests "Playground.play" --console=plain
 */
class Playground {
    @BeforeTest
    fun setup() {
        TestFancam.create()
    }

    @Test
    fun play() = runTest {
        val cfgXml = "assets/game/EN_NarutoAlpha6.00Build304/config/arena/ArenaBadgeCFG.xml"
        XmlDataFormat(File(cfgXml)).also { it.decode() }
    }
}

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

/**
 * Represent the XML data format used in Naruto Online assets.
 *
 * - These XML files are ZLIB compressed XML.
 * - This class provides [decode] mechanism and generic API
 *   to access the XML data in key-value manner.
 */
class XmlDataFormat(private val file: File) {
    init {
        require(file.exists() && file.isFile) {
            "XmlDataFormat error: file doesn't exist or it isn't a file. File: ${file.path}"
        }
    }

    fun decode() {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = builder.parse(InflaterInputStream(file.inputStream()))

    }
}





