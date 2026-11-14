package game.assets.mirror

import encore.fancam.Fancam
import encore.fancam.INDENT
import game.assets.common.AssetsPath
import game.assets.common.BaseAssetsDir
import game.assets.common.VersionTag
import game.assets.versioning.RemoteAssetsEntry
import game.utils.formatSizeInMb
import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

/**
 * Represent all available assets on local directory.
 *
 * Compared to [RemoteAssetsEntry] that mirrors `resource.cfg`'s
 * version map, this index is built from all files stored under [assetsDir].
 *
 * The index may or may not conform to the version map, it can contain
 * more or less assets, older or even newer assets.
 *
 * It also tracks potentially duplicate assets of different version.
 * For example, the same `entry.swf` in version 6.0 which is also present in version 6.1.
 *
 * @property assetsDir Base assets directory.
 */
class LocalAssetsIndex(private val assetsDir: String = BaseAssetsDir) {
    private val map = mutableMapOf<String, MutableList<LocalAssetsEntry>>()
    private var totalSize = 0L

    private enum class LocalIndexBuildStatus {
        NotBuilt, Building, Built
    }

    private var status = LocalIndexBuildStatus.NotBuilt

    /**
     * Start scanning the local directory to build the index,
     * which may take few seconds.
     */
    fun build() {
        if (status == LocalIndexBuildStatus.Building) {
            Fancam.warn { "Local index is being built" }
            return
        }
        if (status == LocalIndexBuildStatus.Built) {
            Fancam.warn { "Local index is already built" }
            return
        }
        status = LocalIndexBuildStatus.Building
        Fancam.info { "Start building local index" }

        val elapsed = measureTimeMillis {
            try {
                val dir = File(assetsDir)
                require(dir.isDirectory) {
                    "Invalid assetsDir 'assetsDir': not a directory or does not exist"
                }
                dir.walk().filter { it.isFile }.forEach { file ->
                    val relativePath = file.relativeTo(dir).invariantSeparatorsPath
                    val (version, pureAssetsPath) = relativePath.split('/', limit = 2)

                    map.getOrPut(pureAssetsPath) { mutableListOf() }.add(
                        LocalAssetsEntry(
                            path = AssetsPath(pureAssetsPath),
                            version = VersionTag(version)
                        )
                    )

                    totalSize += file.length()
                }
            } catch (e: Exception) {
                Fancam.error(e, "local_idx") { "Scandal detected while buildling local index" }
                status = LocalIndexBuildStatus.NotBuilt
                return
            }
        }

        Fancam.info {
            buildString {
                appendLine(
                    "Built local index after " +
                            elapsed.milliseconds.toString(DurationUnit.SECONDS, decimals = 2)
                )
                append(
                    "$INDENT Got ${numEntries()} entries, local size: ${totalSize.formatSizeInMb()}."
                )
            }
        }

        status = LocalIndexBuildStatus.Built
    }

    /**
     * Returns every assets entry of [url] or empty if not found.
     */
    fun getEntry(url: String): List<LocalAssetsEntry> {
        return map[url].orEmpty()
    }

    /**
     * Get the latest entry of [url] or `null` if not found.
     *
     * Compared to [getEntry], this automatically pick the
     * one latest version of the assets.
     */
    fun getLatestEntry(url: String): LocalAssetsEntry? {
        return map[url]
            .orEmpty()
            .reduceOrNull { prev, entry ->
                if (prev.version.isNewerThan(entry.version)) prev else entry
            }
    }

    /**
     * Returns the amount of assets entries.
     */
    fun numEntries(): Int {
        return map.size
    }

    /**
     * Return all entries of local index.
     *
     * Result: a map of assets path into available entries.
     */
    fun entries(): Map<String, List<LocalAssetsEntry>> {
        return map
    }

    /**
     * Returns the sum of all entry size in bytes.
     */
    fun totalSizeBytes(): Long {
        return totalSize
    }
}
