package com.jab.util;

import com.sun.org.apache.xpath.internal.XPathAPI;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.util.Vector;

/**
 * @Author: goshawker@yeah.net
 * @Description:
 * @Date: 2022/9/12 11:00
 * @Version: 1.0
 */
public class XmlUtils {

  /**
   * The encoding.
   */
  private static final String __encoding = "UTF-8";

  /**
   * Gets the element text.
   *
   * @param element the element
   * @return the element text
   */
  public static String getElementText(Element element) {
    NodeList nl = element.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node c = nl.item(i);
      if ((c instanceof Text)) {
        return ((Text) c).getData();
      }
    }
    return null;
  }

  /**
   * get elements by tagName.
   *
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   */
  public static NodeList getElementsByTagName(Document document, String TagName) {
    return document.getElementsByTagName(TagName);
  }

  /**
   * get node attributes.
   *
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   */
  public static NamedNodeMap[] getNodeAttributes(Document document, String TagName) {
    Vector<NamedNodeMap> vector = new Vector<>();
    NodeList lst = document.getElementsByTagName(TagName);
    for (int i = 0; i < lst.getLength(); i++) {
      Node node = lst.item(i);
      vector.add(node.getAttributes());
    }
    return vector.toArray(new NamedNodeMap[vector.size()]);
  }

  /**
   * prase config.xml and return the item-node attributes.
   *
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   */
  public static NamedNodeMap[] parseConfigXml(String TagName,boolean reload) throws ParserConfigurationException, IOException, SAXException {
    if(reload || config == null) {
      InputStream is = ClassLoader.getSystemResourceAsStream("config.xml");
      config = parse(is);
    }
    return getNodeAttributes(config, TagName);
  }
  /**
   * prase config.xml and return the item-node attributes.
   *
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   */
  public static NamedNodeMap[] parseConfigXml(String TagName) throws ParserConfigurationException, IOException, SAXException {
    return parseConfigXml(TagName,false);
  }
  //config.xml
  private static Document config = null;
  /**
   * New document.
   *
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   */
  public static Document newDocument() throws ParserConfigurationException {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .newDocument();
  }

  /**
   * Parses the file.
   *
   * @param file the file
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException                  Signals that an I/O exception has occurred.
   * @throws SAXException                 the sAX exception
   */
  public static Document parse(File file)
          throws ParserConfigurationException, IOException, SAXException {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(file);
  }

  /**
   * Parses the xml-string.
   *
   * @param xml the xml
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException                  Signals that an I/O exception has occurred.
   * @throws SAXException                 the sAX exception
   */
  public static Document parse(String xml)
          throws ParserConfigurationException, IOException, SAXException {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(new ByteArrayInputStream(xml.getBytes()));
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

  /**
   * Parses the inputSource.
   *
   * @param is the is
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException                  Signals that an I/O exception has occurred.
   * @throws SAXException                 the sAX exception
   */
  public static Document parse(InputSource is)
          throws ParserConfigurationException, IOException, SAXException {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(is);
  }

  /**
   * Parses the url.
   *
   * @param url the url
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException                  Signals that an I/O exception has occurred.
   * @throws SAXException                 the sAX exception
   */
  public static Document parse(URL url) throws ParserConfigurationException,
          IOException, SAXException {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(url.toString());
  }

  /**
   * Prints the.
   *
   * @param document the document
   * @param out      the out
   * @throws TransformerFactoryConfigurationError the transformer factory configuration error
   * @throws TransformerException                 the transformer exception
   */
  public static void print(Document document, OutputStream out)
          throws TransformerFactoryConfigurationError,
          TransformerException {
    print(document, new OutputStreamWriter(out), __encoding);
  }

  /**
   * Prints the.
   *
   * @param document the document
   * @param file     the file
   * @throws IOException                          Signals that an I/O exception has occurred.
   * @throws TransformerFactoryConfigurationError the transformer factory configuration error
   * @throws TransformerException                 the transformer exception
   */
  public static void print(Document document, File file)
          throws IOException, TransformerFactoryConfigurationError,
          TransformerException {
    print(document, new FileWriter(file), __encoding);
  }

  /**
   * Prints the.
   *
   * @param document the document
   * @param file     the file
   * @param encoding the encoding
   * @throws TransformerFactoryConfigurationError the transformer factory configuration error
   * @throws TransformerException                 the transformer exception
   * @throws IOException                          Signals that an I/O exception has occurred.
   */
  public static void print(Document document, String file,
                           String encoding) throws TransformerFactoryConfigurationError,
          TransformerException, IOException {
    print(document, new FileWriter(file), encoding);
  }

  /**
   * Prints the.
   *
   * @param document the document
   * @param file     the file
   * @param encoding the encoding
   * @param publicId the public id
   * @param sysId    the sys id
   * @throws TransformerFactoryConfigurationError the transformer factory configuration error
   * @throws TransformerException                 the transformer exception
   */
  public static void print(Document document, String file,
                           String encoding, String publicId, String sysId)
          throws TransformerFactoryConfigurationError, TransformerException {
    Transformer transformer = TransformerFactory.newInstance()
            .newTransformer();

    transformer.setOutputProperty("method", "xml");
    transformer.setOutputProperty("encoding", encoding);
    transformer.setOutputProperty("indent", "yes");
    transformer.setOutputProperty("doctype-public", publicId);
    transformer.setOutputProperty("doctype-system", sysId);
    transformer.transform(new DOMSource(document), new StreamResult(file));
  }

  /**
   * Prints the.
   *
   * @param document the document
   * @param writer   the writer
   * @param encoding the encoding
   * @throws TransformerFactoryConfigurationError the transformer factory configuration error
   * @throws TransformerException                 the transformer exception
   */
  public static final void print(Document document, Writer writer,
                                 String encoding) throws TransformerFactoryConfigurationError,
          TransformerException {
    Transformer transformer = TransformerFactory.newInstance()
            .newTransformer();

    transformer.setOutputProperty("method", "xml");
    transformer.setOutputProperty("encoding", encoding);
    transformer.setOutputProperty("indent", "yes");
    transformer
            .transform(new DOMSource(document), new StreamResult(writer));
  }

  /**
   * Xpath.
   *
   * @param base  the base
   * @param xpath the xpath
   * @return the node
   * @throws TransformerException the transformer exception
   */
  public static final Node xpath(Node base, String xpath)
          throws TransformerException {
    return XPathAPI.selectSingleNode(base, xpath);
  }

  /**
   * Xpath list.
   *
   * @param base  the base
   * @param xpath the xpath
   * @return the node list
   * @throws TransformerException the transformer exception
   */
  public static NodeList xpathList(Node base, String xpath)
          throws TransformerException {
    return XPathAPI.selectNodeList(base, xpath);
  }

  public static String getNodeValue(NamedNodeMap map, String nodeName) {
    if (map.getNamedItem(nodeName) == null) {
      return null;
    } else {
      Node node = map.getNamedItem(nodeName);
      return node.getNodeValue();
    }
  }

  public static String getNodeValue(NamedNodeMap map, String nodeName,boolean nullToEmpty) {
    if (map.getNamedItem(nodeName) == null) {
      if(nullToEmpty){
        return "";
      }
      return null;
    } else {
      Node node = map.getNamedItem(nodeName);
      return node.getNodeValue();
    }
  }

  public static void main(String[] args) {
    try {
      NamedNodeMap[] vector = parseConfigXml("item");
      for (NamedNodeMap map : vector) {
        String id = getNodeValue(map, "id");
        String lable = getNodeValue(map, "lable");
        String type = getNodeValue(map, "type");
        String length = getNodeValue(map, "length");
        String default_ = getNodeValue(map, "default");
        String primarykey = getNodeValue(map, "primarykey");

        System.out.printf("id:%s, lable:%s, type:%s, length:%s, default:%s, primarykey:%s; ", id, lable, type, length, default_, primarykey);
        System.out.println();
      }
      vector = parseConfigXml("file");
      String saveDir = getNodeValue(vector[0], "saveDir");
      System.out.println(saveDir);

      vector = parseConfigXml("items");
      String namespace = getNodeValue(vector[0], "namespace");
      System.out.println(namespace);

    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }

  }
}