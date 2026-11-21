package game.amf

import flex.messaging.io.PropertyProxyRegistry
import flex.messaging.io.SerializationContext
import flex.messaging.io.amf.ASObject
import flex.messaging.io.amf.Amf3Input
import kotlin.collections.set

/**
 * Custom Amf3Input implementation:
 * - Use `HashMap` instead of the default `Hashtable`
 *   which is a restricted Java class in the [SerializationContext].
 * - Custom logic for creating object instance. Used when an expected
 *   remote class listed on the AMF message doesn't exist, and
 *   we have to fallback to a particular class which is `ASObject`.
 */
class CustomAmf3Input(context: SerializationContext) : Amf3Input(context) {
    private val asObjectProxy by lazy {
        PropertyProxyRegistry.getProxyAndRegister(ASObject())
    }

    override fun createObjectInstance(params: Array<Any?>): Any {
        val className = params[0] as? String
        val instance = ASObject().apply {
            if (!className.isNullOrEmpty()) {
                type = className
            }
        }
        if (params.size > 1) {
            params[1] = asObjectProxy
        }
        return instance
    }

    override fun readDictionary(): Any {
        val ref = readUInt29()
        if ((ref and 1) == 0) {
            return getObjectReference(ref shr 1)
        }
        val length = ref shr 1
        `in`.readBoolean() // usingWeakKeys

        val map = HashMap<Any?, Any?>(length)
        objectTable.add(map)
        for (i in 0 until length) {
            map[readObject()] = readObject()
        }
        return map
    }
}
