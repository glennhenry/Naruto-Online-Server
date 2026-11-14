package game.core.assets

import game.core.amf.AmfDecoder
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Represent the Naruto Online assets version map inside the `resource.cfg`.
 *
 * The version map is an entry of all Naruto Online assets
 * with information like size, version, and URL.
 *
 * This class is not constructed directly as `resource.cfg`
 * must be decoded beforehand with [ResourceCfg.decode].
 *
 * @param decoded Decoded result of version map using [AmfDecoder].
 */
class VersionMap(decoded: Map<*, *>) {
    private val map = mutableMapOf<String, AssetsEntry>()

    /**
     * Represents which version does this version map belongs to.
     *
     * It is derived from the version of `entry.swf`, which is
     * usually same version as `resource.cfg`, and is the latest
     * within all assets in the map.
     */
    lateinit var version: VersionTag

    private var totalSize: Long = 0

    init {
        @Suppress("UNCHECKED_CAST")
        decoded as Map<String, Any>
        decoded.forEach { (url, value) ->
            // one edge case where value is not a map, it's literally "version": "..."
            if (value is Map<*, *>) {
                val tag = VersionTag(requireNotNull(value["tag"]) {
                    "VersionMap creation error: tag null for value '$value'"
                } as String)

                // the version of resource.cfg is not listed in the file
                // but a heuristic can determine it via entry.swf version
                // as it is always updated to download the newer resource.cfg
                if (url == "entry.swf") {
                    version = tag
                }

                map[url] = AssetsEntry(
                    url = AssetsUrl(url),
                    tag = tag,
                    size = (requireNotNull(value["size"]) {
                        "VersionMap creation error: size null for value '$value'"
                    } as Int).also { totalSize += it },
                    ver = requireNotNull(value["ver"]) {
                        "VersionMap creation error: ver null for value '$value'"
                    } as Int,
                    belong = value["belong"] as? String
                )
            }
        }
    }

    /**
     * Returns the assets entry of [url] or `null` if not found.
     */
    fun getEntry(url: String): AssetsEntry? {
        return map[url]
    }

    /**
     * Returns the amount of assets entries.
     */
    fun numEntries(): Int {
        return map.size
    }

    /**
     * Return all entries in the version map.
     */
    fun entries(): List<AssetsEntry> {
        return map.values.toList()
    }

    /**
     * Returns the sum of all entry size in bytes.
     */
    fun totalSizeBytes(): Long {
        return totalSize
    }

    @Serializable
    private data class AssetsEntryJson(
        val size: Int,
        val belong: String? = null,
        val tag: String,
        val ver: Int,
        val url: String
    )

    private val jsonPrinter = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
        encodeDefaults = true
    }

    /**
     * Export the version map into JSON in the respective [outputDir].
     */
    fun exportToJson(outputDir: String = "workspace/output") {
        val output = File(outputDir, "resource.cfg-${version.tag}.json")
        output.writeText(
            jsonPrinter.encodeToString(map.mapValues { (_, entry) ->
                AssetsEntryJson(
                    url = entry.url.url,
                    tag = entry.tag.tag,
                    size = entry.size,
                    ver = entry.ver,
                    belong = entry.belong
                )
            })
        )
    }
}
