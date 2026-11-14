package game.assets.versioning

import game.assets.common.AssetsPath
import game.assets.common.VersionTag
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

/**
 * An entry consisting core Naruto Online resources required
 * by the game to start.
 *
 * This is derived from the XML data located in the latter section
 * of `resource.cfg`. The essential resources are divided into three:
 *
 * - [flash]
 * - [assets]
 * - [config]
 *
 * Each of them follows a grouping called `archive` that is associated
 * with a `.pkg` file.
 *
 * @param xml The raw XML from `resource.cfg`.
 * @param version Version tag of the source `resource.cfg`.
 */
class ArchivePack(private val xml: String, val version: VersionTag) {
    val flash: List<Archive>
    val assets: List<Archive>
    val config: List<Archive>

    init {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = builder.parse(InputSource(StringReader(xml))).documentElement

        flash = parse(doc.getElementsByTagName("flash").item(0))
        assets = parse(doc.getElementsByTagName("assets").item(0))
        config = parse(doc.getElementsByTagName("config").item(0))
    }

    private fun parse(element: Node): List<Archive> {
        val result = mutableListOf<Archive>()

        val archiveNodes = element.childNodes
        for (i in 0 until archiveNodes.length) {
            val archive = archiveNodes.item(i) as? Element ?: continue

            val num = archive.getAttribute("num").toInt()
            val id = archive.getAttribute("id").toInt()
            val path = AssetsPath(archive.getAttribute("url"))
            val t = archive.getAttribute("t")

            val items = mutableListOf<AssetsPath>()
            val itemsNodes = archiveNodes.item(i).childNodes
            for (j in 0 until itemsNodes.length) {
                val itemElement = itemsNodes.item(j) as? Element ?: continue
                items.add(AssetsPath(itemElement.getAttribute("url")))
            }

            result.add(Archive(num, id, path, t, items))
        }

        return result
    }

    /**
     * Returns the total items spanning across `flash`, `assets`, and `config` archives.
     */
    fun size(): Int {
        return flash.size() + assets.size() + config.size()
    }

    /**
     * Export this archive pack into XML in the respective [outputDir].
     */
    fun exportToXml(outputDir: String = "workspace/output") {
        val output = File(outputDir, "archives.xml-${version.tag}.xml")
        output.writeText(xml)
    }
}
