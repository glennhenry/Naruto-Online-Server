package encore.utils.xml

import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader

/**
 * Provides minimal API as a wrapper over plain [Element] DOM.
 *
 * This class provides access to:
 * - [name]: node's tag name
 * - [value]: text content if available
 * - [attributes]: node's attributes as key-value pair
 * - [children]: the child nodes represented as a list of [XmlNode].
 *
 * Use [fromString] or [fromFile] to construct.
 */
class XmlNode(
    private val name: String,
    private val value: String?,
    private val attributes: Map<String, String>,
    private val children: List<XmlNode>
) {
    /**
     * e.g., `<element>` has value of "element".
     * @return The node's tag name.
     */
    fun name(): String = name

    /**
     * An `XmlNode` will not have a value if:
     * - Has one or more [children].
     * - Empty tag like `<element/>` or `<element></element>`
     * @return The node's text content if available.
     */
    fun value(): String? = value

    /**
     * Require [value] to not be `null`, otherwise throw an [IllegalStateException].
     * @return The node's text content.
     * @throws IllegalStateException
     */
    fun requireValue(): String = value ?: error(
        "Expected a value but got null from node '$name'; " +
                "hasChildren=${children.isNotEmpty()}"
    )

    /**
     * e.g., `<element abc=1 def=2>` has value `{abc=1, def=2}`
     *
     * Duplicate attribute key will be overwritten.
     *
     * @return The node's attributes as key-value pair map.
     */
    fun attributes(): Map<String, String> = attributes

    /**
     * Returns `true` this node has one or more attributes.
     */
    fun hasAttributes(): Boolean {
        return children.isNotEmpty()
    }

    /**
     * Returns the number of attributes this node has.
     */
    fun numAttributes(): Int {
        return attributes.size
    }

    /**
     * @return The child nodes which may be empty.
     */
    fun children(): List<XmlNode> = children

    /**
     * Returns `true` this node has one or more children.
     */
    fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }

    /**
     * Returns the number of children this node has.
     */
    fun numChildren(): Int {
        return children.size
    }

    /**
     * Get the attribute identified by [name]
     * @return The attribute's value as `String` or `null` if not found.
     */
    fun attr(name: String): String? {
        return attributes[name]
    }

    /**
     * Require [attr] of [name] to not be `null`, otherwise throw an [IllegalStateException].
     * @return The attribute's value.
     * @throws IllegalStateException
     */
    fun requireAttr(name: String): String = attr(name) ?: error(
        "Expected attr '$name' but got null from node '${this.name}'; " +
                "attributes: [${attributes.entries.joinToString()}]"
    )


    /**
     * Get a child node identified by its node tag name.
     *
     * If there are multiple children of the same name, the first by
     * XML declaration order will be returned.
     *
     * @return The child node if exists.
     */
    operator fun get(name: String): XmlNode? {
        return children.firstOrNull { it.name == name }
    }

    /**
     * Require a child node of [name], otherwise throw an [IllegalStateException].
     * @throws IllegalStateException
     * @return The child node.
     */
    fun requireGet(name: String): XmlNode {
        return get(name) ?: error("Expected a child of name '$name' but not found")
    }

    override fun toString(): String {
        return "XmlNode(name=$name, value=$value, " +
                "attributes=[${attributes.entries.joinToString()}], " +
                "${children.size} children)"
    }

    /**
     * Converts this node into XML string used for debugging purpose.
     *
     * For example:
     * ```
     * <parent>
     *   <child>1</child>
     * </parent>
     * ```
     *
     * @param indent Pre-indent before each lines.
     */
    fun toXmlString(indent: Int = 0): String {
        val padding = "  ".repeat(indent)

        val attrs = if (attributes.isNotEmpty()) {
            " " + attributes.entries.joinToString(" ") {
                """${it.key}="${it.value}""""
            }
        } else {
            ""
        }

        // Node has children
        if (children.isNotEmpty()) {
            val childContent = children.joinToString("\n") {
                it.toXmlString(indent + 1)
            }

            return "$padding<$name$attrs>\n$childContent\n$padding</$name>"
        }

        // Empty node
        if (value.isNullOrEmpty()) {
            return "$padding<$name$attrs/>"
        }

        // Node with text value
        return "$padding<$name$attrs>$value</$name>"
    }

    companion object {
        /**
         * Create [XmlNode] from [xml] string.
         */
        fun fromString(xml: String): XmlNode {
            return XmlUtils.toXmlNode(
                BasicBuilder.parse(InputSource(StringReader(xml)))
                    .documentElement
            )
        }

        /**
         * Create [XmlNode] from [file].
         */
        fun fromFile(file: File): XmlNode {
            require(file.exists() && file.isFile) { "XmlNode.fromFile: not a file ${file.path}" }
            return XmlUtils.toXmlNode(
                BasicBuilder.parse(file.inputStream()).documentElement
            )
        }
    }
}

/**
 * Allows you to chain [get] or `[]` operator without `null` checks.
 *
 * e.g., element["a"]["b"]["c"]
 *
 * **Important!** you must add import `encore.utils.xml.get`.
 */
operator fun XmlNode?.get(name: String): XmlNode? = this?.get(name)
