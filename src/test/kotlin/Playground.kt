import game.amf.AmfDecoder
import org.tukaani.xz.LZMAInputStream
import org.w3c.dom.Document
import testUtils.TestFancam
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.File
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.test.BeforeTest
import kotlin.test.Test

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
    fun play() {
        val rescfg = File("workspace/resource.cfg")
        ResourceReader(rescfg).decode()
    }
}

fun Int.bytesToMb(): Double {
    return this.toDouble() / 1024 / 1024
}

fun ByteArray.sizeInMb(): String {
    return "(${"%.2f".format(this.size.bytesToMb())} MB)"
}

fun Int.sizeInMb(): String {
    return "(${"%.2f".format(this.bytesToMb())} MB)"
}

fun Long.bytesToMb(): Double {
    return this.toDouble() / 1024 / 1024
}

fun Long.sizeInMb(): String {
    return "(${"%.2f".format(this.bytesToMb())} MB)"
}

fun File.sizeInMb(): String {
    return this.length().sizeInMb()
}

class ResourceReader(private val resourceCfgFile: File) {
    init {
        require(resourceCfgFile.exists()) {
            "Fail to read resource.cfg: file doesn't exists"
        }
    }

    fun decode() {
        println("resource.cfg: ${resourceCfgFile.length()} ${resourceCfgFile.sizeInMb()}")

        val decompressed = LZMAInputStream(
            resourceCfgFile.inputStream()
        ).use { it.readAllBytes() }
        println("Decompressed: ${decompressed.size} ${decompressed.sizeInMb()}")

        val input = ByteArrayInputStream(decompressed)
        val data = DataInputStream(input)

        val amfLength = data.readInt()

        println("amfLength: $amfLength ${amfLength.sizeInMb()}")

        val versionMap = AmfDecoder().decode(data)

//        println(versionMap?.toJsonString())

        val hasXml = data.readUnsignedByte() != 0
        var xml: String? = null

        if (hasXml) {
            val xmlLength = data.readInt()
            require(xmlLength <= input.available()) {
                "Invalid XML length: $xmlLength (available=${input.available()})"
            }

            val xmlBytes = ByteArray(xmlLength)
            data.readFully(xmlBytes)

            xml = xmlBytes.toString(Charsets.UTF_8)
        }

        val factory = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = factory.parse(xml?.byteInputStream())

        println(XmlUtils.toRawString(document))
    }
}

object XmlUtils {
    fun toRawString(doc: Document?): String {
        val tf = TransformerFactory.newInstance()
        val transformer: Transformer = tf.newTransformer()
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        val writer = StringWriter()
        transformer.transform(DOMSource(doc), StreamResult(writer))
        return writer.toString()
    }
}
