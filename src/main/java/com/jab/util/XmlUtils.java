package com.jab.util;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    public static Document buildDocument() throws ParserConfigurationException, IOException, SAXException {
        if (config == null) {
            InputStream is = ClassLoader.getSystemResourceAsStream("config.xml");
            config = parse(is);
        }
        return config;
    }


    public static NamedNodeMap[] parseConfigXml(String TagName, boolean reload) throws ParserConfigurationException, IOException, SAXException {
        if (reload) {
            config = buildDocument();
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

    public static String getNodeValue(NamedNodeMap map, String nodeName) {
        if (map == null || map.getNamedItem(nodeName) == null) {
            return "";
        } else {
            Node node = map.getNamedItem(nodeName);
            return node.getNodeValue();
        }
    }

    /**
     * 获取所有子节点
     * @param childNodes
     * @return
     */
    public static ArrayList<Node>  getChildNodes(NodeList childNodes,String tagName) {
        ArrayList<Node> arrayList = new ArrayList<>();
        //遍历子节点集合
        for (int i = 0; i < childNodes.getLength(); i++) {
            //获取父节点下每一个子节点
            Node item = childNodes.item(i);
            if (tagName!=null && item.getNodeName().equals(tagName)){
                arrayList.add(item);
            }

//            //判断该节点类型是否为元素节点。这里可以避免下一步操作因空格符导致的类转换失败
//            if (item.getNodeType() == Node.ELEMENT_NODE) {
//                //将Node类型转换为Element类型
//                Element ele = (Element) item;
//                //输出标签名称和指定属性值
//                System.out.println(ele.getTagName() + "\t" + ele.getAttribute("name"));
//            }
        }
        return arrayList;
    }

    /**
     * 获取所有的节点
     * @param tagName 指定Tag
     * @return
     * @throws Exception
     */
    public static ArrayList<Node> getNodesByTagName(String tagName) throws Exception {
        ArrayList<Node> arrayList = new ArrayList<>();
        Element root = XmlUtils.buildDocument().getDocumentElement();
        //获取根节点下的子节点集合
        NodeList childNodes = root.getChildNodes();
        //调用遍历子节点集合的方法
        for (int i = 0; i < childNodes.getLength(); i++) {
            //获取父节点下每一个子节点
            Node item = childNodes.item(i);
            //判断该节点类型是否为元素节点。这里可以避免下一步操作因空格符导致的类转换失败
            if (item.getNodeName().equals(tagName)){
                arrayList.add(item);
            }
        }
        return arrayList;
    }

    /**
     * print content for config.xml
     */
    public static void print(){
        try {
            ArrayList<Node> arrayList = XmlUtils.getNodesByTagName("items");
            for (int i = 0; i < arrayList.size(); i++) {
                Node node = arrayList.get(i);
                NamedNodeMap namedNodeMaps = node.getAttributes();
                String namespace = XmlUtils.getNodeValue(namedNodeMaps, "namespace").toLowerCase();
                String tableName = XmlUtils.getNodeValue(namedNodeMaps, "tableName").toLowerCase();
                System.out.printf("\n namespace:%s tableName:%s \n",namespace,tableName);

                ArrayList<Node> childs = getChildNodes(node.getChildNodes(),"item");
                for (int j = 0; j < childs.size(); j++) {
                    Node child = childs.get(j);
                    String id = XmlUtils.getNodeValue(child.getAttributes(), "id").toLowerCase();
                    String lable = XmlUtils.getNodeValue(child.getAttributes(), "lable").toLowerCase();

                    String type = XmlUtils.getNodeValue(child.getAttributes(), "type").toLowerCase();
                    String length = XmlUtils.getNodeValue(child.getAttributes(), "length").toLowerCase();

                    String default2 = XmlUtils.getNodeValue(child.getAttributes(), "default").toLowerCase();
                    String primarykey = XmlUtils.getNodeValue(child.getAttributes(), "primarykey").toLowerCase();
                    System.out.printf("\t id:%s lable:%s type:%s length:%s default:%s primarykey:%s  \n",id,lable,type,length,default2,primarykey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            print();
//            saveDir = saveDir.concat(namespace);
//            String fileDir = "";
//            if (File.separator.equals("\\")) {
//                fileDir += saveDir.replaceAll("/", "\\\\");
//            } else {
//                fileDir += saveDir.replaceAll("\\\\", "/");
//            }
//            String file = fileDir + File.separator + "file";
//            System.out.printf(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}