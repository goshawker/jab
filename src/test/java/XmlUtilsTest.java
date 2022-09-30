import com.jab.util.XmlUtils;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: goshawker@yeah.net
 * @Description:
 * @Date: 2022/09/17 10:47:08
 * @Version: 1.0
 */
public class XmlUtilsTest {

  public static void main(String[] args) {
    try {
      NamedNodeMap[] vector = XmlUtils.parseItems();
      for (NamedNodeMap map : vector) {
        String id = XmlUtils.getNodeValue(map, "id");
        String lable = XmlUtils.getNodeValue(map, "lable");
        String type = XmlUtils.getNodeValue(map, "type");
        String length = XmlUtils.getNodeValue(map, "length");
        String default_ = XmlUtils.getNodeValue(map, "default");
        String primarykey = XmlUtils.getNodeValue(map, "primarykey");
        System.out.printf("id:%s, lable:%s, type:%s, length:%s, default:%s, primarykey:%s; ", id, lable, type, length, default_, primarykey);
        System.out.println();
      }
      vector = XmlUtils.parseFile();
      String saveDir = XmlUtils.getNodeValue(vector[0], "saveDir");
      System.out.println(saveDir);

      vector = XmlUtils.parseItems();
      String namespace = XmlUtils.getNodeValue(vector[0], "namespace");
      System.out.println(namespace);


      vector = XmlUtils.parseDatabase();
      String driver = XmlUtils.getNodeValue(vector[0], "driver");
      String ip = XmlUtils.getNodeValue(vector[0], "ip");
      String port = XmlUtils.getNodeValue(vector[0], "port");
      String instance = XmlUtils.getNodeValue(vector[0], "instance");
      String user = XmlUtils.getNodeValue(vector[0], "user");
      String pwd = XmlUtils.getNodeValue(vector[0], "pwd");
      System.out.printf("%s %s %s %s %s %s ",driver,ip,port,instance,user,pwd);

    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }

  }
}
