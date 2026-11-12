package game.core.assets

import encore.fancam.Fancam
import encore.fancam.INDENT
import game.core.amf.AmfDecoder
import game.core.utils.formatSizeInMb
import org.tukaani.xz.LZMAInputStream
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

/**
 * Abstract access to the `resource.cfg` file.
 *
 * The `resource.cfg` is a special file that contains an entry
 * of all Naruto Online assets and an XML data consisting assets
 * packages needed to initialize the game.
 *
 * Call `decode()` first before any usage.
 *
 * Notably:
 * - [versionMap]
 * - [archives]
 */
class ResourceCfg(private val file: File) {
    init {
        require(file.exists()) {
            "Fail to read resource.cfg: file doesn't exists"
        }
    }

    private enum class ResourceDecodeStatus {
        NotDecoded, Decoding, DecodeCompleted
    }

    private var status = ResourceDecodeStatus.NotDecoded

    private var _versionMap: VersionMap? = null

    /**
     * [VersionMap]
     */
    val versionMap: VersionMap
        get() = _versionMap ?: error(
            "ResourceCfg is uninitialized, please call decode() first."
        )

    private var _archives: ArchivePack? = null

    /**
     * [ArchivePack]
     */
    val archives: ArchivePack
        get() = _archives ?: error(
            "ResourceCfg is uninitialized, please call decode() first."
        )

    /**
     * Start decoding the `resource.cg`, which may take a few seconds.
     */
    fun decode() {
        if (status == ResourceDecodeStatus.Decoding) {
            Fancam.warn { "resource.cfg decode is in process" }
            return
        }
        if (status == ResourceDecodeStatus.DecodeCompleted) {
            Fancam.warn { "resource.cfg is already decoded" }
            return
        }
        status = ResourceDecodeStatus.Decoding
        Fancam.info { "Start decoding resource.cfg ${file.length()} bytes ${file.formatSizeInMb()}" }

        val elapsed = measureTimeMillis {
            try {
                val decompressed = LZMAInputStream(file.inputStream()).use { it.readAllBytes() }
                val input = ByteArrayInputStream(decompressed)
                val data = DataInputStream(input)
                val amfLength = data.readInt()
                val xmlLengthApprox = decompressed.size - amfLength - 5

                Fancam.debug {
                    "Decompressed: ${decompressed.size} bytes, " +
                            "amfLength: $amfLength bytes, " +
                            "xmlLength: $xmlLengthApprox bytes"
                }

                val amf = AmfDecoder().decode(data)
                _versionMap = VersionMap(amf as Map<*, *>)

                val hasXml = data.readUnsignedByte() != 0
                if (hasXml) {
                    val xmlLength = data.readInt()
                    require(xmlLength <= input.available()) {
                        "Invalid XML length: $xmlLength (available=${input.available()})"
                    }

                    val xmlBytes = ByteArray(xmlLength)
                    data.readFully(xmlBytes)

                    _archives = ArchivePack(xmlBytes.toString(Charsets.UTF_8), versionMap.version)
                }
            } catch (e: Exception) {
                Fancam.error(e, "res_reader") { "Scandal detected while decoding resource.cfg" }
                status = ResourceDecodeStatus.NotDecoded
                return
            }
        }

        Fancam.info {
            buildString {
                appendLine(
                    "Finished decoding resource.cfg after " +
                            elapsed.milliseconds.toString(DurationUnit.SECONDS, decimals = 2) +
                            " with ${versionMap.numEntries()} entries (${versionMap.version.tag})"
                )
                append(
                    "$INDENT Got a total of ${archives.size()} items in archives " +
                            "(flash=${archives.flash.size()}, assets=${archives.assets.size()}, " +
                            "config=${archives.config.size()})"
                )
            }
        }

        status = ResourceDecodeStatus.DecodeCompleted
    }

    /**
     * Returns the version of this `resource.cfg`.
     *
     * The version of resource CFG represents the version tag
     * from which the CFG file was downloaded. The version of CFG
     * is the highest to any version of files in the version map,
     * except for few files like `entry.swf` where it's same.
     *
     * It's the same value as [VersionMap.version].
     */
    fun getVersion(): VersionTag {
        return versionMap.version
    }
}
