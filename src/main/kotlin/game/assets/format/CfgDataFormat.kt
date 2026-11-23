package game.assets.format

import encore.fancam.Fancam
import encore.serialization.toAmfValue
import encore.utils.jsonPrettyPrinter
import encore.utils.toJsonString
import game.amf.Amf
import game.assets.common.ResourceDecodeStatus
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

/**
 * Represent the CFG data format used in Naruto Online assets (excluding `resource.cfg`).
 *
 * The CFG data found in Naruto Online assets is Zlib-compressed + Amf3 encoded dictionary.
 * Exception: some CFG are uncompressed and contains Amf3 payload directly.
 * This class supports that too.
 *
 * - Call [decode] before usage.
 * - Gain access to the Amf dictionary via [get].
 * - [exportToFile] output decoded Amf dictionary in
 *   JSON representation to the given `directoryPath`.
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
            val bytes = file.inputStream().buffered().use { stream ->
                stream.mark(2)
                val header = stream.readNBytes(2)
                stream.reset()
                val isZlib = header.contentEquals(byteArrayOf(0x78, 0xda.toByte()))
                val dataStream = if (isZlib) InflaterInputStream(stream) else stream
                dataStream.readBytes()
            }
            _map = Amf.decode<Map<String, Any>>(bytes)
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
        File(directoryPath).mkdirs()
        val output = File(directoryPath, file.nameWithoutExtension + ".json")
        output.writeText(map.toJsonString())
    }

    companion object {
        /**
         * Encode JSON string back into CFG format.
         * @return Encoded bytes.
         */
        fun encode(jsonString: String): ByteArray {
            val output = ByteArrayOutputStream()
            val map = jsonPrettyPrinter.parseToJsonElement(jsonString)
            Amf.encode(map.toAmfValue()).inputStream().buffered().use { input ->
                DeflaterOutputStream(output.buffered()).use { deflaterOut ->
                    input.copyTo(deflaterOut)
                }
            }

            return output.toByteArray()
        }

        /**
         * Encode a plain JSON file back into CFG format.
         *
         * This is typically used for editing assets file where
         * the file is decoded, edited, then encoded back.
         *
         * Find the output in [outputDir].
         */
        fun encodeToFile(jsonFile: File, outputDir: String = "workspace/output") {
            val target = File(outputDir, jsonFile.name)
            target.writeBytes(encode(jsonFile.readText()))
            Fancam.info { "Encoded '${jsonFile.name}' to ${target.path}" }
        }
    }
}
