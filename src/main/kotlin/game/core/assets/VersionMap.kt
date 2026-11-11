package game.core.assets

import game.core.amf.AmfDecoder

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

    init {
        @Suppress("UNCHECKED_CAST")
        (decoded as Map<String, Map<String, Any>>).forEach { (url, value) ->
            map[url] = AssetsEntry(
                url = AssetsUrl(url),
                tag = VersionTag(requireNotNull(value["tag"]) {
                    "VersionMap creation error: tag null for value '$value'"
                } as String),
                size = requireNotNull(value["size"]) {
                    "VersionMap creation error: size null for value '$value'"
                } as Int,
                ver = requireNotNull(value["ver"]) {
                    "VersionMap creation error: ver null for value '$value'"
                } as Int,
                belong = value["belong"] as? String
            )
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
}
