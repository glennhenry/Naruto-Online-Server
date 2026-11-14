package game.amf

import java.io.ByteArrayInputStream
import java.io.DataInputStream

/**
 * AMF format serializer and deserializer.
 * Supports AMF3, not AMF0 or Flash remoting.
 *
 * Only support synchronous operation i.e., multiple decoding at a time is not supported.
 *
 * Reference:
 * - [Wikipedia](https://en.wikipedia.org/wiki/Action_Message_Format).
 * - [Official spec](http://web.archive.org/web/20220122035930/https://www.adobe.com/content/dam/acom/en/devnet/pdf/amf-file-format-spec.pdf).
 */
class AmfDecoder {
    private val stringTable = mutableListOf<String>()
    private val objectTable = mutableListOf<Any?>()
    private val traitTable = mutableListOf<Amf3Trait>()

    private data class Amf3Trait(
        val className: String,
        val externalizable: Boolean,
        val dynamic: Boolean,
        val sealedNames: List<String>
    )

    private fun resetTables() {
        stringTable.clear()
        objectTable.clear()
        traitTable.clear()
    }

    fun decode(bytes: ByteArray): Any? {
        resetTables()
        val input = DataInputStream(ByteArrayInputStream(bytes))
        return readAmf3Value(input)
    }

    fun decode(input: DataInputStream): Any? {
        resetTables()
        return readAmf3Value(input)
    }

    /**
     * Compared to [decode], this doesn't reset the string/object/trait table.
     */
    fun decodeNext(bytes: ByteArray): Any? {
        val input = DataInputStream(ByteArrayInputStream(bytes))
        return readAmf3Value(input)
    }

    /**
     * Compared to [decode], this doesn't reset the string/object/trait table.
     */
    fun decodeNext(input: DataInputStream): Any? {
        return readAmf3Value(input)
    }

    // not implemented: XMLDOCUMENT
    private fun readAmf3Value(input: DataInputStream): Any? {
        return when (val typeMarker = input.readByte()) {
            Amf3Marker.UNDEFINED -> null
            Amf3Marker.NULL -> null
            Amf3Marker.BOOLEAN_FALSE -> false
            Amf3Marker.BOOLEAN_TRUE -> true
            Amf3Marker.INTEGER -> readU29(input)
            Amf3Marker.DOUBLE -> input.readDouble()
            Amf3Marker.STRING -> readAmf3String(input)
            Amf3Marker.DATE -> readAmf3Date(input)
            Amf3Marker.ARRAY -> readAmf3Array(input)
            Amf3Marker.OBJECT -> readAmf3Object(input)
            Amf3Marker.BYTE_ARRAY -> readAmf3ByteArray(input)
            Amf3Marker.XML -> readAmf3Xml(input)
            Amf3Marker.XMLDOCUMENT -> error("XMLDocument '0x${typeMarker.toString(16)}' is not supported")
            Amf3Marker.DICTIONARY -> readAmf3Dictionary(input)
            else -> error("Unsupported AMF3 marker 0x${typeMarker.toString(16)}")
        }
    }

    private fun readU29(input: DataInputStream): Int {
        var b = input.readUnsignedByte()

        if (b < 0x80) {
            return b
        }

        var value = (b and 0x7F) shl 7
        b = input.readUnsignedByte()

        if (b < 0x80) {
            return value or b
        }

        value = (value or (b and 0x7F)) shl 7
        b = input.readUnsignedByte()

        if (b < 0x80) {
            return value or b
        }

        value = (value or (b and 0x7F)) shl 8
        b = input.readUnsignedByte()

        return value or b
    }

    private fun readAmf3String(input: DataInputStream): String {
        val header = readU29(input)

        if ((header and 1) == 0) {
            val index = header ushr 1
            return stringTable[index]
        }

        val length = header ushr 1
        if (length == 0) {
            return ""
        }

        val value = String(input.readNBytes(length), Charsets.UTF_8)
        stringTable.add(value)
        return value
    }

    private fun readAmf3Object(input: DataInputStream): Map<String, Any?> {
        val header = readU29(input)

        // 0___: Object reference
        if ((header and 1) == 0) {
            val objectIndex = header ushr 1
            @Suppress("UNCHECKED_CAST")
            return objectTable[objectIndex] as Map<String, Any?>
        }
        // 1___: Inline object
        val trait: Amf3Trait

        // 10__: Trait reference
        if ((header and 2) == 0) {
            val traitIndex = header ushr 2
            trait = traitTable[traitIndex]
        } else {
            // 11__: Inline trait
            val externalizable = (header and 4) != 0
            val dynamic = (header and 8) != 0
            val sealedCount = header ushr 4

            if (externalizable) {
                error("Unsupported externalizable object")
            }

            val className = readAmf3String(input)
            val sealedNames = mutableListOf<String>()

            repeat(sealedCount) {
                sealedNames += readAmf3String(input)
            }

            trait = Amf3Trait(
                className = className,
                externalizable = false,
                dynamic = dynamic,
                sealedNames = sealedNames
            )

            traitTable.add(trait)
        }

        val result = linkedMapOf<String, Any?>()
        // @TODO temporarily disabled
        // result["className"] = trait.className
        objectTable.add(result)

        for (name in trait.sealedNames) {
            result[name] = readAmf3Value(input)
        }

        if (trait.dynamic) {
            while (true) {
                val name = readAmf3String(input)
                if (name.isEmpty()) break
                result[name] = readAmf3Value(input)
            }
        }

        return result
    }

    private fun readAmf3Date(input: DataInputStream): Double {
        val header = readU29(input)

        if ((header and 1) == 0) {
            val dateIndex = header ushr 1
            return objectTable[dateIndex] as Double
        }

        val value = input.readDouble()
        objectTable.add(value)
        return value
    }

    // the array can be dense (an array of values) or associative (key-value paired)
    // the order is: dense length, associate part, dense part
    private fun readAmf3Array(input: DataInputStream): List<Any?> {
        val header = readU29(input)

        if ((header and 1) == 0) {
            val arrayIndex = header ushr 1
            return objectTable[arrayIndex] as List<Any?>
        }

        val densePortionLength = header ushr 1
        val result = mutableListOf<Any?>()

        var name = readAmf3String(input)
        while (name != "") {
            val value = readAmf3Value(input)
            result.add(name to value)
            name = readAmf3String(input)
        }

        repeat(densePortionLength) {
            result.add(readAmf3Value(input))
        }

        objectTable.add(result)
        return result
    }

    private fun readAmf3ByteArray(input: DataInputStream): ByteArray {
        val header = readU29(input)

        if ((header and 1) == 0) {
            val byteArrayIndex = header ushr 1
            return objectTable[byteArrayIndex] as ByteArray
        }


        val length = header ushr 1
        val result = ByteArray(length) {
            input.readByte()
        }

        objectTable.add(result)
        return result
    }

    private fun readAmf3Xml(input: DataInputStream): String {
        val header = readU29(input)

        if ((header and 1) == 0) {
            error("XML reference not supported")
        }

        val xmlLength = header ushr 1

        val bytes = ByteArray(xmlLength)
        input.readFully(bytes)

        return bytes.toString(Charsets.UTF_8)
    }

    private fun readAmf3Dictionary(input: DataInputStream): Map<Any, Any> {
        val header = readU29(input)

        if ((header and 1) == 0) {
            error("Dictionary reference not supported")
        }

        // pun not intended 😅
        val dictSize = header ushr 1

        // treat weakref and non-weakref as same
        val weakKeysFlag = input.readUnsignedByte()

        val result = mutableMapOf<Any, Any>()
        repeat(dictSize) {
            val key = readAmf3Value(input) as Any
            val value = readAmf3Value(input) as Any
            result[key] = value
        }

        return result
    }
}
