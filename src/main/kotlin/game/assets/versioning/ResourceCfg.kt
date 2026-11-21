package game.assets.versioning

import encore.fancam.Fancam
import encore.fancam.INDENT
import encore.serialization.toAmfValue
import encore.time.millisFormatInSeconds
import encore.utils.formatSizeInMb
import encore.utils.jsonPrettyPrinter
import game.amf.Amf
import game.assets.common.ResourceDecodeStatus
import game.assets.common.VersionTag
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromStream
import org.tukaani.xz.LZMA2Options
import org.tukaani.xz.LZMAInputStream
import org.tukaani.xz.LZMAOutputStream
import java.io.*
import kotlin.system.measureTimeMillis

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

                val amf = requireNotNull(Amf.decode<Map<String, Any>>(input)) {
                    "Amf deserialization is failed on resource.cfg"
                }
                _versionMap = VersionMap(amf)

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
            } finally {
                status = ResourceDecodeStatus.DecodeCompleted
            }
        }

        Fancam.info {
            buildString {
                appendLine("Finished decoding resource.cfg after ${elapsed.millisFormatInSeconds()}")
                appendLine(
                    "$INDENT Got ${versionMap.numEntries()} entries, " +
                            "remote size: ${versionMap.totalSizeBytes().formatSizeInMb()}, " +
                            "version: ${versionMap.version.tag}"
                )
                appendLine(
                    "$INDENT Got ${versionMap.numOptionalEntries()} optional entries " +
                            versionMap.totalOptionalSizeBytes().formatSizeInMb()
                )
                append(
                    "$INDENT Got a total of ${archives.size()} items in archives " +
                            "(flash=${archives.flash.size()}, assets=${archives.assets.size()}, " +
                            "config=${archives.config.size()})"
                )
            }
        }
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

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        fun encodeToFile(
            versionMapJson: File, archivesXml: File,
            outputDir: String = "workspace/output"
        ) {
            val amfDict = versionMapJson.inputStream().buffered(65536).use {
                jsonPrettyPrinter.decodeFromStream<JsonElement>(it)
            }.toAmfValue()

            // @TODO This is so slow! it can take 1-2 minutes
            val versionMapBytes = Amf.encode(amfDict)

            val output = ByteArrayOutputStream(8 * 1024 * 1024)
            val data = DataOutputStream(output)

            // amf length
            data.writeInt(versionMapBytes.size)
            // amf payload
            data.write(versionMapBytes)
            // has XML
            data.writeByte(1)
            // XML
            val xmlBytes = archivesXml.readBytes()
            data.writeInt(xmlBytes.size)
            data.write(xmlBytes)
            data.flush()

            File(outputDir).mkdirs()
            val target = File(outputDir, "resource.cfg")

            val bytes = output.toByteArray()
            LZMAOutputStream(
                target.outputStream().buffered(),
                LZMA2Options(LZMA2Options.PRESET_DEFAULT),
                bytes.size.toLong()
            ).use { output -> output.write(bytes) }
        }
    }
}
