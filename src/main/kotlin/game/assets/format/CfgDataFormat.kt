package game.assets.format

import encore.fancam.Fancam
import encore.utils.toJsonString
import game.amf.AmfDecoder
import game.assets.common.ResourceDecodeStatus
import java.io.File
import java.util.zip.InflaterInputStream

/**
 * Represent the CFG data format used in Naruto Online assets (excluding `resource.cfg`).
 *
 * The CFG data found in Naruto Online assets is Zlib-compressed + Amf3 encoded dictionary.
 *
 * - Call [decode] before usage.
 * - Gain access to the Amf object via [get].
 * - [exportToFile] output decoded JSON representation
 *   in the given `directoryPath`.
 */
class CfgDataFormat(private val file: File) : DataFormat {
    init {
        require(file.exists() && file.isFile) {
            "CfgDataFormat error: file doesn't exist or it isn't a file. File: ${file.path}"
        }
    }

    private var status = ResourceDecodeStatus.NotDecoded

    private var _map: Map<String, Any>? = null
    val map: Map<String, Any>
        get() = _map ?: error("CfgDataFormat is not initialized yet, please call decode() first.")

    override fun decode() {
        if (status == ResourceDecodeStatus.Decoding) {
            Fancam.warn { "CfgDataFormat of '${file.name}' decode is in process" }
            return
        }
        if (status == ResourceDecodeStatus.DecodeCompleted) {
            Fancam.warn { "CfgDataFormat of '${file.name}' is already decoded" }
            return
        }

        try {
            val bytes = InflaterInputStream(file.inputStream()).use { it.readBytes() }
            @Suppress("UNCHECKED_CAST")
            _map = AmfDecoder().decode(bytes) as? Map<String, Any>
                ?: error("CfgDataFormat error: ${file.name} may not be Amf3 dictionary")
        } catch (e: Exception) {
            Fancam.error(e, "cfg_format") { "Scandal detected while decoding CfgDataFormat of '${file.name}'" }
            status = ResourceDecodeStatus.NotDecoded
            return
        } finally {
            status = ResourceDecodeStatus.DecodeCompleted
        }
    }

    /**
     * Retrieve the value of [key] from the Amf3 dictionary.
     */
    fun get(key: String): Any? {
        return map[key]
    }

    /**
     * Retrieve the original Amf3 dictionary from the decoded file.
     */
    fun dictionary(): Map<String, Any> = map

    override fun exportToFile(directoryPath: String) {
        val output = File(directoryPath, file.nameWithoutExtension + ".json")
        output.writeText(map.toJsonString())
    }
}
