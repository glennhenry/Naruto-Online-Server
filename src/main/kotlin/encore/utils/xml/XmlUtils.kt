package encore.utils.xml

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

val BasicBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

/**
 * XML Utilities
 */
object XmlUtils {
    /**
     * Converts the XML [document] into raw XML string.
     * @param omitDeclaration Whether to omit XML version declaration.
     */
    fun toRawString(document: Document?, omitDeclaration: Boolean): String {
        val tf = TransformerFactory.newInstance().newTransformer()
        if (omitDeclaration) {
            tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        }
        val writer = StringWriter()
        tf.transform(DOMSource(document), StreamResult(writer))
        return writer.toString()
    }

    /**
     * Converts DOM [Element] representation into [XmlNode].
     */
    fun toXmlNode(element: Element): XmlNode {
        val name = element.tagName
        val attributes = mutableMapOf<String, String>()

        // attributes
        for (i in 0 until element.attributes.length) {
            val attr = element.attributes.item(i)
            val key = attr.nodeName
            val value = attr.nodeValue
            attributes[key] = value
        }

        val elementChildren = (0 until element.childNodes.length)
            .mapNotNull { element.childNodes.item(it) as? Element }

        // value
        val value: String? = if (elementChildren.isNotEmpty()) {
            null
        } else {
            element.textContent.trim().ifEmpty { null }
        }

        // children
        val children = mutableListOf<XmlNode>()
        for (child in elementChildren) {
            children.add(toXmlNode(child))
        }

        return XmlNode(name, value, attributes, children)
    }
}
