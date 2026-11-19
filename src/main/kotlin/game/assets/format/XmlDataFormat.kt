package game.assets.format

import encore.fancam.Fancam
import encore.utils.xml.XmlNode
import encore.utils.xml.XmlUtils
import game.assets.common.ResourceDecodeStatus
import java.io.File
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Represent the XML data format used in Naruto Online assets.
 *
 * The XML data found in Naruto Online assets is Zlib-compressed XML.
 *
 * - Call [decode] before usage.
 * - Gain access to the XML data via [node].
 * - [exportToFile] **re-read** the file and output decoded XML representation
 *   in the given `directoryPath`.
 */
class XmlDataFormat(private val file: File) : DataFormat {
    init {
        require(file.exists() && file.isFile) {
            "XmlDataFormat error: file doesn't exist or it isn't a file. File: ${file.path}"
        }
    }

    private var status = ResourceDecodeStatus.NotDecoded

    private var _node: XmlNode? = null

    /**
     * [XmlNode]
     */
    val node: XmlNode
        get() = _node
            ?: error("XmlDataFormat uninitialized, please call decode() first.")

    override fun decode() {
        if (status == ResourceDecodeStatus.Decoding) {
            Fancam.warn { "XmlDataFormat of '${file.name}' decode is in process" }
            return
        }
        if (status == ResourceDecodeStatus.DecodeCompleted) {
            Fancam.warn { "XmlDataFormat of '${file.name}' is already decoded" }
            return
        }

        try {
            val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc = builder.parse(InflaterInputStream(file.inputStream()))
            _node = XmlUtils.toXmlNode(doc.documentElement)
        } catch (e: Exception) {
            Fancam.error(e, "xml_format") { "Scandal detected while decoding XmlDataFormat of '${file.name}'" }
            status = ResourceDecodeStatus.NotDecoded
            return
        } finally {
            status = ResourceDecodeStatus.DecodeCompleted
        }
    }

    override fun exportToFile(directoryPath: String) {
        InflaterInputStream(file.inputStream().buffered()).use { input ->
            File(directoryPath, file.name).outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
    }

    companion object {
        /**
         * Encode a plain XML file back into Zlib compressed XML format.
         *
         * This is typically used for editing assets file where
         * the file is decoded, edited, then encoded back.
         *
         * Find the output in [outputDir].
         */
        fun encode(xmlFile: File, outputDir: String = "workspace/output") {
            val target = File(outputDir, xmlFile.name)
            xmlFile.inputStream().buffered().use { input ->
                DeflaterOutputStream(target.outputStream().buffered()).use { deflaterOut ->
                    input.copyTo(deflaterOut)
                }
            }

            println("Encoded '${xmlFile.name}' to ${target.path}")
        }
    }
}
