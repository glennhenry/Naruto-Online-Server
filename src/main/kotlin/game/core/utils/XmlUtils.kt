package game.core.utils

import org.w3c.dom.Document
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

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
}
