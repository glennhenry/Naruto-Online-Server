package game.assets.versioning

import encore.utils.xml.XmlNode
import game.assets.common.AssetsPath
import game.assets.common.VersionTag
import java.io.File

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
class ArchivePack(xml: String, val version: VersionTag) {
    val flash: List<Archive>
    val assets: List<Archive>
    val config: List<Archive>
    val node: XmlNode = XmlNode.fromString(xml)

    init {
        flash = parse(node["flash"])
        assets = parse(node["assets"])
        config = parse(node["config"])
    }

    private fun parse(element: XmlNode?): List<Archive> {
        requireNotNull(element) { "Null element" }
        val result = mutableListOf<Archive>()

        for (archive in element.children()) {
            val num = archive.requireAttr("num").toInt()
            val id = archive.requireAttr("id").toInt()
            val path = AssetsPath(archive.attr("url") ?: "")
            val t = archive.attr("t")

            val items = mutableListOf<AssetsPath>()
            for (item in archive.children()) {
                items.add(AssetsPath(item.requireAttr("url")))
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
        val output = File(outputDir, "archives.xml")
        output.writeText(node.toXmlString())
    }
}
