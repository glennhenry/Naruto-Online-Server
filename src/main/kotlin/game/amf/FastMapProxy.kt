package game.amf

import flex.messaging.io.AbstractProxy
import flex.messaging.io.amf.ASObject

/**
 * Solution for slow AMF encoding:
 * - When AMF encoder encounter a type, it reflects
 *   to know type information, create trait information, and allocates
 *   array to store keys for map-like objects.
 * - When the same object is encountered multiple times, up to 10k+,
 *   it can be very slow.
 * - We register a custom proxy for ASObject/HashMap dedicatedly
 *   so that it doesn't require the same process for every objects.
 *
 * This proxy is apparently required for `HashMap` to improve the
 * performance drastically (1 min -> 0.5 s).
 *
 * Mostly needed to encode resource.cfg; Decode back works,
 * but still speculating, and not sure if this will work for other cases.
 */
class FastMapProxy : AbstractProxy(null) {
    override fun getDefaultInstance(): Any = ASObject()

    override fun getPropertyNames(instance: Any): List<String> {
        val map = instance as Map<*, *>
        return map.keys.mapNotNull { it?.toString() }
    }

    override fun getType(instance: Any?, propertyName: String?): Class<*>? {
        val value = (instance as? Map<*, *>)?.get(propertyName)
        return value?.javaClass ?: Any::class.java
    }

    override fun getValue(instance: Any, propertyName: String): Any? {
        return (instance as Map<*, *>)[propertyName]
    }

    override fun setValue(instance: Any, propertyName: String, value: Any?) {
        @Suppress("UNCHECKED_CAST")
        (instance as MutableMap<String, Any?>)[propertyName] = value
    }

    override fun getAlias(instance: Any): String? {
        return (instance as? ASObject)?.type
    }
}
