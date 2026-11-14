package game.amf

/**
 * Byte markers for AMF3 format.
 */
object Amf3Marker {
    const val UNDEFINED: Byte = 0x00
    const val NULL: Byte = 0x01
    const val BOOLEAN_FALSE: Byte = 0x02
    const val BOOLEAN_TRUE: Byte = 0x03
    const val INTEGER: Byte = 0x04
    const val DOUBLE: Byte = 0x05
    const val STRING: Byte = 0x06
    const val XMLDOCUMENT: Byte = 0x07
    const val DATE: Byte = 0x08
    const val ARRAY: Byte = 0x09
    const val OBJECT: Byte = 0x0A
    const val XML: Byte = 0x0B
    const val BYTE_ARRAY: Byte = 0x0C
    const val DICTIONARY: Byte = 0x11
}
