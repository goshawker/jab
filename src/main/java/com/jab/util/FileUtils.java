package com.jab.util;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


/**
 * @Author: goshawker@yeah.net
 * @Description:
 * @Date: 2022/9/12 11:02
 * @Version: 1.0
 */
public class FileUtils {

  /**
   * The _log.
   */
  static Logger log = LogManager.getLogger(FileUtils.class);

  /**
   * _copy file.
   *
   * @param source            the source
   * @param target            the target
   * @param noExistCreateFile the no exist create file
   * @return true, if successful
   * @throws FileNotFoundException the file not found exception
   * @throws IOException           Signals that an I/O exception has occurred.
   */
  public static boolean copyFile(File source, File target, boolean noExistCreateFile) throws FileNotFoundException, IOException {
    if (!target.exists()) {
      if (noExistCreateFile) {
        target.createNewFile();
      } else {
        throw new FileNotFoundException();
      }
    }
    long i = IOUtils.copyLarge(new FileInputStream(source), new FileOutputStream(target));
    return i > 0;
  }

  /**
   * Gets the resource as stream.
   *
   * @param resource the resource
   * @return the resource as stream
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static InputStream getResourceAsStream(String resource) throws IOException {
    InputStream inputStream = null;
    inputStream = FileUtils.class.getResourceAsStream(resource);
    // TODO Auto-generated catch block
    if (inputStream == null && !resource.startsWith("/")) {
//			_log.error("Could not find resource "+resource +" ,find resource from Jar retry.");
      resource = "/".concat(resource);
    } else {
//			_log.info("Find resource "+resource +" in classpath");
    }
    inputStream = FileUtils.class.getResourceAsStream(resource);
    if (inputStream == null) {
      log.error("Could not find resource " + resource);
      throw new FileNotFoundException(resource);
    } else {
//			_log.debug("Find resource "+resource +" in Jar.");
    }
    return inputStream;
  }


  public static boolean generateAction(IntrospectedTable table, String namespace, String targetProject) {
    // TODO Auto-generated method stub
    StringBuffer form = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    String _package = "";
    if (namespace.startsWith("/")) {
      _package = namespace.substring(1).replaceAll("/", ".");
    } else {
      _package = namespace.replaceAll("/", ".");
    }
    String classname = "Action";
    String datetime = DateUtils.newDateTime();
    String vo = table.getBaseRecordType();

    for (IntrospectedColumn column : table.getAllColumns()) {
      String propertyname = column.getJavaProperty();
    }
    try {
      //get template
      InputStream is = getResourceAsStream("template/action/action.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#package", _package);
      template = template.replaceAll("#classname", classname);
      template = template.replaceAll("#datetime", datetime);
      template = template.replaceAll("#vo", vo);

      String fileDir = targetProject;
      if (File.separator.equals("\\")) {
        fileDir += namespace.replaceAll("/", "\\\\");
      } else {
        fileDir += namespace.replaceAll("\\\\", "/");
      }
      File dir = new File(fileDir);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      String file = fileDir + File.separator + classname + ".java";
      File f = new File(file);
      if (f.exists()) {
        System.out.println("Existing file " + file + " was overwritten");
        f.delete();
      } else {
        System.out.println("Generating  file " + file + " was created");
      }
      f.createNewFile();
      writeFile(f, template, "UTF-8");
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }


  public static boolean generateValueObject() throws Exception {
    StringBuffer valueObjectName = new StringBuffer();
    StringBuffer valueObjectField = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    NamedNodeMap[] args = XmlUtils.parseItem();
    for (NamedNodeMap map : args) {
      String id = XmlUtils.getNodeValue(map, "id", true).toLowerCase();
      String type = XmlUtils.getNodeValue(map, "type", true).toLowerCase();
      String default_ = XmlUtils.getNodeValue(map, "default", true);
      valueObjectField.append("\t \t private ");
      if (type.equals("text") || type.equals("select")) {
        valueObjectField.append("String ");
      } else if (type.equals("number")) {
        valueObjectField.append("BigDecimal ");
      } else if (type.equals("date")) {
        valueObjectField.append("Date ");
      }
      valueObjectField.append(id + " ;\r\n");
    }
    args = XmlUtils.parseItems();
    String namespace = XmlUtils.getNodeValue(args[0], "namespace");
    String tableName = XmlUtils.getNodeValue(args[0], "tableName");
    valueObjectName.append(tableName);
    try {
      HashMap<String, String> replace = new HashMap<>();
      replace.put("#VALUEOBJECTNAME#", valueObjectName.toString());
      replace.put("#VALUEOBJECTFIELD#", valueObjectField.toString());
      autoGenerateHtml("template/vo.java.template", replace, tableName + ".java");
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  public static boolean generateUpdateHtml() throws Exception {
    // TODO Auto-generated method stub
    StringBuffer formfield = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    Vector<NamedNodeMap> primarykeys = new Vector<>();
    NamedNodeMap[] args = XmlUtils.parseItem();
    String updatestr = "";
    for (NamedNodeMap map : args) {
      String id = XmlUtils.getNodeValue(map, "id", true).toLowerCase();
      String lable = XmlUtils.getNodeValue(map, "lable", true).toLowerCase();
      String type = XmlUtils.getNodeValue(map, "type", true).toLowerCase();
      String length = XmlUtils.getNodeValue(map, "length", true).toLowerCase();
      String default_ = XmlUtils.getNodeValue(map, "default", true);
      String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
      String options = XmlUtils.getNodeValue(map, "options", true);
      String readOnly = "";
      if (!StringUtils.isEmptyOrNull(primarykey)) {
        readOnly = "readonly";
        lable = "<font style=\"color:red\">" + lable + "</font>";
        primarykeys.add(map);
        updatestr += id + "='+data[i]." + id + "+" + "'&";
      }
      String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\"   " + readOnly + ">";
      if (type.equalsIgnoreCase("date")) {
        fieldStr = "<input type=\"date\"  name=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
      } else if (type.equalsIgnoreCase("number")) {
        fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\"   \"+readOnly+\">";
      } else if (type.equalsIgnoreCase("select")) {
        String html = "";
        html += "<select  id=\"" + id + "\"    name=\"" + id + "\" value=\"" + default_ + "\"  style=\"width:" + Integer.parseInt(length) + "px\"   \"+readOnly+\"> \r\t";
        String[] optons_ = options.split("\\|");
        html += "<option value=\"\"></option> \r\t";
        for (int i = 0; i < optons_.length; i++) {
          html += "<option value=\"" + optons_[i] + "\">" + optons_[i] + "</option> \r\t";
        }
        html += "</select>";
        fieldStr = html;
      }
      formfield.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
      formfield.append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n");
      formfield.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
      formfield.append(blank9).append(blank2).append(fieldStr).append("\r\n");
      formfield.append(blank9).append("</td>").append("\r\n");
      formfield.append(blank7).append("</tr>").append("\r\n");
    }
    NamedNodeMap[] items = XmlUtils.parseItems();
    String namespace = XmlUtils.getNodeValue(items[0], "namespace");
    try {
      HashMap<String, String> replace = new HashMap<>();
      replace.put("#NAMESPACE#", namespace);
      replace.put("#FORMFIELD#", formfield.toString());
      autoGenerateHtml("template/update.html.template", replace, "update.html");
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  public static boolean generateNewHtml() throws Exception {
    // TODO Auto-generated method stub
    StringBuffer formfield = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");

    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    Vector<NamedNodeMap> primarykeys = new Vector<>();
    NamedNodeMap[] args = XmlUtils.parseItem();
    String updatestr = "";
    for (NamedNodeMap map : args) {
      String id = XmlUtils.getNodeValue(map, "id", true).toLowerCase();
      String lable = XmlUtils.getNodeValue(map, "lable", true).toLowerCase();
      String type = XmlUtils.getNodeValue(map, "type", true).toLowerCase();
      String length = XmlUtils.getNodeValue(map, "length", true).toLowerCase();
      String default_ = XmlUtils.getNodeValue(map, "default", true);
      String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
      String options = XmlUtils.getNodeValue(map, "options", true);
      if (!StringUtils.isEmptyOrNull(primarykey)) {
        primarykeys.add(map);
        updatestr += id + "='+data[i]." + id + "+" + "'&";
      }
      String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\">";
      if (type.equalsIgnoreCase("date")) {
        fieldStr = "<input type=\"date\"  name=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
      } else if (type.equalsIgnoreCase("number")) {
        fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
      } else if (type.equalsIgnoreCase("select")) {
        String html = "";
        html += "<select  id=\"" + id + "\"    name=\"" + id + "\" value=\"" + default_ + "\"  style=\"width:" + Integer.parseInt(length) + "px\" > \r\t";
        String[] optons_ = options.split("\\|");
        html += "<option value=\"\"></option> \r\t";
        for (int i = 0; i < optons_.length; i++) {
          html += "<option value=\"" + optons_[i] + "\">" + optons_[i] + "</option> \r\t";
        }
        html += "</select>";
        fieldStr = html;
      }
      formfield.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
      formfield.append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n");
      formfield.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
      formfield.append(blank9).append(blank2).append(fieldStr).append("\r\n");
      formfield.append(blank9).append("</td>").append("\r\n");
      formfield.append(blank7).append("</tr>").append("\r\n");
    }
    NamedNodeMap[] items = XmlUtils.parseItems();
    String namespace = XmlUtils.getNodeValue(items[0], "namespace");
    try {
      HashMap<String, String> replace = new HashMap<>();
      replace.put("#NAMESPACE#", namespace);
      replace.put("#FORMFIELD#", formfield.toString());
      autoGenerateHtml("template/new.html.template", replace, "new.html");
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  public static boolean generateMainHtml() throws Exception {
    StringBuffer queryCondition = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    StringBuffer queryResultTitle = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    StringBuffer queryResultData = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    Vector<NamedNodeMap> primarykeys = new Vector<>();
    NamedNodeMap[] args = XmlUtils.parseItem();
    //更新主键
    String updatestr = "";
    //请求参数
    String requstparameters = "";
    for (NamedNodeMap map : args) {
      String id = XmlUtils.getNodeValue(map, "id", true).toLowerCase();
      String lable = XmlUtils.getNodeValue(map, "lable", true).toLowerCase();
      String type = XmlUtils.getNodeValue(map, "type", true).toLowerCase();
      String length = XmlUtils.getNodeValue(map, "length", true).toLowerCase();
      String default_ = XmlUtils.getNodeValue(map, "default", true);
      String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
      String options = XmlUtils.getNodeValue(map, "options", true);
      if (!StringUtils.isEmptyOrNull(primarykey)) {
        primarykeys.add(map);
        updatestr += id + "='+data[i]." + id + "+" + "'&";
      }
      requstparameters += id + "='document.getElementById('" + id + "')" + "'&";
      //query conditions.
      String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\">";
      if (type.equalsIgnoreCase("date")) {
        fieldStr = "<input type=\"date\"  name=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
      } else if (type.equalsIgnoreCase("number")) {
        fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
      } else if (type.equalsIgnoreCase("select")) {
        String html = "";
        html += "<select  id=\"" + id + "\"    name=\"" + id + "\" value=\"" + default_ + "\"  style=\"width:" + Integer.parseInt(length) + "px\" > \r\t";
        String[] optons_ = options.split("\\|");
        html += "<option value=\"\"></option> \r\t";
        for (int i = 0; i < optons_.length; i++) {
          html += "<option value=\"" + optons_[i] + "\">" + optons_[i] + "</option> \r\t";
        }
        html += "</select>";
        fieldStr = html;
      }
      queryCondition.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
      queryCondition.append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n");
      queryCondition.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
      queryCondition.append(blank9).append(blank2).append(fieldStr).append("\r\n");
      queryCondition.append(blank9).append("</td>").append("\r\n");
      queryCondition.append(blank7).append("</tr>").append("\r\n");
      //query result title
      queryResultTitle.append(blank9 + blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >").append("\r\n");
      queryResultTitle.append(blank9 + blank7).append(blank2).append(id).append("\r\n");
      queryResultTitle.append(blank9 + blank7).append("</td>").append("\r\n");
      //query result data
      String format = "";
      if (type.equalsIgnoreCase("datetime")) {
        format = " format=\"yyyy-MM-dd HH:mm:ss\" ";
      }
      queryResultData.append(blank7).append("trHtml +=\"<td class='editGrid'  align='left'>").append("\"");
      queryResultData.append("+data[i]." + id + "+");
      queryResultData.append("\"</td>\"").append(";\r\n");
    }
    //query result title.operation
    queryResultTitle.append(blank9 + blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >").append("\r\n");
    queryResultTitle.append(blank9 + blank7).append(blank2).append("Operation").append("\r\n");
    queryResultTitle.append(blank9 + blank7).append("</td>").append("\r\n");

    NamedNodeMap[] items = XmlUtils.parseItems();
    String namespace = XmlUtils.getNodeValue(items[0], "namespace");
    try {
      HashMap<String, String> replace = new HashMap<>();
      replace.put("#NAMESPACE#", namespace.concat(""));
      replace.put("#DONEW#", namespace.concat(""));
      replace.put("#QUERYCONDITION#", queryCondition.toString());
      replace.put("#GRIDHEAD#", queryResultTitle.toString());
      replace.put("#GRIDDATA#", queryResultData.toString());
      replace.put("#REQUSTPARAMETERS#", requstparameters);
      replace.put("#UPDATESTR#", updatestr.endsWith("&") ? updatestr.substring(0, updatestr.length() - 1) : updatestr);
      autoGenerateHtml("template/main.html.template", replace, "main.html");
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  private static void autoGenerateHtml(String templateFile, HashMap<String, String> replace, String newName) throws ParserConfigurationException, IOException, SAXException {
    InputStream is = getResourceAsStream(templateFile);
    String template = IOUtils.toString(is);
    for (String key : replace.keySet()) {
      template = template.replaceAll(key, replace.get(key));
    }
    NamedNodeMap[] args = XmlUtils.parseConfigXml("file");
    String saveDir = XmlUtils.getNodeValue(args[0], "saveDir");
    String overwrite = XmlUtils.getNodeValue(args[0], "overwrite");
    String fileDir = "";
    if (File.separator.equals("\\")) {
      fileDir += saveDir.replaceAll("/", "\\\\");
    } else {
      fileDir += saveDir.replaceAll("\\\\", "/");
    }
    File dir = new File(fileDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    String file = fileDir + File.separator + newName;
    File f = new File(file);
    if (f.exists() && overwrite.equals("true")) {
      System.out.println("Existing file " + file + " was overwritten");
      f.delete();
    } else {
      System.out.println("Generating  file " + file + " was created");
    }
    f.createNewFile();
    writeFile(f, template, "UTF-8");
  }

  /**
   * Writes, or overwrites, the contents of the specified file
   *
   * @param file
   * @param content
   */
  public static void writeFile(File file, String content, String fileEncoding) throws IOException {
    FileOutputStream fos = new FileOutputStream(file, false);
    OutputStreamWriter osw;
    if (fileEncoding == null) {
      osw = new OutputStreamWriter(fos);
    } else {
      osw = new OutputStreamWriter(fos, fileEncoding);
    }

    BufferedWriter bw = new BufferedWriter(osw);
    bw.write(content);
    bw.close();
    System.out.println("file  " + file + "  created.");
  }
}