package com.jab.util;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * @Description :
 * @Author : goshawker@yeah.net
 * @Date : 2023-02-14 11:04
 */

public class XmlUtils {

    //config.xml
    private static Document config = null;

    public static NamedNodeMap[] getNodeAttributes(Document document, String TagName) {
        Vector<NamedNodeMap> vector = new Vector<>();
        NodeList lst = document.getElementsByTagName(TagName);
        for (int i = 0; i < lst.getLength(); i++) {
            Node node = lst.item(i);
            vector.add(node.getAttributes());
        }
        return vector.toArray(new NamedNodeMap[0]);
    }

    public static NamedNodeMap[] parseConfigXml(String TagName, boolean reload) throws ParserConfigurationException, IOException, SAXException {
        if (reload || config == null) {
            InputStream is = ClassLoader.getSystemResourceAsStream("config.xml");
            config = parse(is);
        }
        return getNodeAttributes(config, TagName);
    }

    public static NamedNodeMap[] parseItem() throws ParserConfigurationException, IOException, SAXException {
        return parseConfigXml("item", false);
    }

    public static NamedNodeMap[] parseItems() throws ParserConfigurationException, IOException, SAXException {
        return parseConfigXml("items", false);
    }

    public static NamedNodeMap[] parseFile() throws ParserConfigurationException, IOException, SAXException {
        return parseConfigXml("file", false);
    }

    public static NamedNodeMap[] parseDatabase() throws ParserConfigurationException, IOException, SAXException {
        return parseConfigXml("database", false);
    }

    public static NamedNodeMap[] parseConfigXml(String TagName) throws ParserConfigurationException, IOException, SAXException {
        return parseConfigXml(TagName, false);
    }

    /**
     * Parses the inputStream.
     *
     * @param is the is
     * @return the document
     * @throws ParserConfigurationException the parser configuration exception
     * @throws IOException                  Signals that an I/O exception has occurred.
     * @throws SAXException                 the sAX exception
     */
    public static Document parse(InputStream is)
            throws ParserConfigurationException, IOException, SAXException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(is);
    }



    public static String getNodeValue(NamedNodeMap map, String nodeName, boolean toEmptyString) {
        if (map.getNamedItem(nodeName) == null) {
            return null;
        } else {
            return getNodeValue(map, nodeName);
        }
    }

    public static String getNodeValue(NamedNodeMap map, String nodeName) {
        if (map == null || map.getNamedItem(nodeName) == null) {
            return "";
        } else {
            Node node = map.getNamedItem(nodeName);
            return node.getNodeValue();
        }
    }

}