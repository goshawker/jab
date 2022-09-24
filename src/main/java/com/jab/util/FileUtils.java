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
import java.math.BigDecimal;
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
   * Checks if is existed.
   *
   * @param pathName the path name
   * @return true, if is existed
   */
  public static boolean isExisted(String pathName) {
    File file = new File(pathName);
    return file.exists();
  }

  /**
   * Removes the.
   *
   * @param pathName the path name
   * @return true, if successful
   */
  public static boolean remove(String pathName) {
    File file = new File(pathName);
    return file.delete();
  }

  /**
   * Rename.
   *
   * @param oldPathName the old path name
   * @param newPathName the new path name
   * @return true, if successful
   */
  public static boolean rename(String oldPathName, String newPathName) {
    File from = new File(oldPathName);
    return from.renameTo(new File(newPathName));
  }

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

  /**
   * Gets the system environment variable separate char.
   *
   * @return string
   */
  public static String getSystemEnvironmentVariableSeparateChar() {
    if (ValidateUtils.isWindows()) {
      return ";";
    } else {
      return ":";
    }
  }

  /**
   * _get classpath char.
   *
   * @return the string
   */
  public static String getSystemClasspathChar() {
    if (ValidateUtils.isWindows()) {
      return "%CLASSPATH%";
    } else {
      return "$CLASSPATH";
    }
  }


  public static boolean generateConfiguration(IntrospectedTable table, String namespace, String targetProject) {
    // TODO Auto-generated method stub

    String _package = "";
    if (namespace.startsWith("/")) {
      _package = namespace.substring(1).replaceAll("/", ".");
    } else {
      _package = namespace.replaceAll("/", ".");
    }

    String classname = "struts-" + table.getBaseRecordType().replaceAll("\\.", "_");
    String packagename = table.getBaseRecordType().toLowerCase();
    packagename = packagename.substring(packagename.lastIndexOf(".") + 1);
    try {
      //get template
      InputStream is = getResourceAsStream("template/configuration/struts.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#packagename", packagename);
      template = template.replaceAll("#namespace", namespace);
      template = template.replaceAll("#package", _package);


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
      String file = fileDir + File.separator + classname + ".xml";
      File f = new File(file);
      if (f.exists()) {
        System.out.println("Existing file " + file + " was overwritten");
        f.delete();
      } else {
        System.out.println("Generating  file " + file + " was created");
      }
      f.createNewFile();
      writeFile(f, template, "UTF-8");
      System.out.println("\r\n <!--Important ::  Add follow auto-generated struts configuration To src/struts.xml -->");
      String mapper = " <include file=\"" + classname + ".xml" + "\"></include>";
      System.out.println(mapper);
      System.out.println("\r\n <!--Important :: (Menu Enter Url)--> \r\n " + namespace + "/query!init");
      System.out.println("\r\n");
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  public static boolean generateDeleteAction(IntrospectedTable table, String namespace, String targetProject) {
    // TODO Auto-generated method stub
    // TODO Auto-generated method stub


    StringBuffer form = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    String _package = "";
    String vo = table.getBaseRecordType();
    if (namespace.startsWith("/")) {
      _package = namespace.substring(1).replaceAll("/", ".");
    } else {
      _package = namespace.replaceAll("/", ".");
    }

    String classname = "DeleteAction";
    String datetime = DateUtils.newDateTime();
    String deleteid = table.getDeleteByPrimaryKeyStatementId();
    try {
      //get template
      InputStream is = getResourceAsStream("template/action/delete.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#package", _package);
      template = template.replaceAll("#classname", classname);
      template = template.replaceAll("#vo", vo);
      template = template.replaceAll("#datetime", datetime);
      template = template.replaceAll("#deleteid", deleteid);


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

  public static boolean generateUpdateAction(IntrospectedTable table, String namespace, String targetProject) {
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

    String classname = "UpdateAction";
    String datetime = DateUtils.newDateTime();
    String updateid = table.getUpdateByPrimaryKeyStatementId();
    String initid = table.getSelectByPrimaryKeyStatementId();
    String vo = table.getBaseRecordType();
    try {
      //get template
      InputStream is = getResourceAsStream("template/action/update.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#package", _package);
      template = template.replaceAll("#classname", classname);
      template = template.replaceAll("#initid", initid);
      template = template.replaceAll("#datetime", datetime);
      template = template.replaceAll("#updateid", updateid);
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

  public static boolean generateNewAction(IntrospectedTable table,
                                          String namespace, String targetProject) {
    // TODO Auto-generated method stub

    StringBuffer form = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    String _package = "";
    String pkinit = "// TODO Auto-generated \r\n";
    pkinit += blank2 + "//Initialization Primary Key Propertity! \r\n";
    String vo = table.getBaseRecordType();
    if (namespace.startsWith("/")) {
      _package = namespace.substring(1).replaceAll("/", ".");
    } else {
      _package = namespace.replaceAll("/", ".");
    }
    List<IntrospectedColumn> list = table.getPrimaryKeyColumns();
    //JDBCConnectionConfiguration jcc = table.getContext().getJdbcConnectionConfiguration();
    //HashMap pk  = DbUtils._buildPKValues(jcc.getDriverClass(),jcc.getConnectionURL(),jcc.getUserId(),jcc.getPassword(), table.getTableConfiguration().getTableName());
    for (int i = 0; i < list.size(); i++) {
      String typename = list.get(i).getJavaProperty();
      int type = list.get(i).getJdbcType();
      pkinit += blank2 + "if(StringUtils.isEmptyOrNull(var.get" + typename.replaceFirst(String.valueOf(typename.charAt(0)), String.valueOf(typename.charAt(0)).toUpperCase()) + "()))\r\n";
      if (type == java.sql.Types.NUMERIC
              || type == java.sql.Types.INTEGER
              || type == java.sql.Types.BIGINT
              || type == java.sql.Types.FLOAT
              || type == java.sql.Types.DECIMAL
              || type == java.sql.Types.DOUBLE) {

        pkinit += blank2 + blank2 + "var.set" + typename.replaceFirst(String.valueOf(typename.charAt(0)), String.valueOf(typename.charAt(0)).toUpperCase()) + "(DbUtils.getAutoIncrementLsb(\"" + table.getTableConfiguration().getTableName() + "\", \"" + list.get(i).getActualColumnName() + "\"));\r\n";
      } else if (type >= 91 && type <= 93) {
        pkinit += blank2 + blank2 + "var.set" + typename.replaceFirst(String.valueOf(typename.charAt(0)), String.valueOf(typename.charAt(0)).toUpperCase()) + "(new java.util.Date());\r\n";

      } else {
        pkinit += blank2 + blank2 + "var.set" + typename.replaceFirst(String.valueOf(typename.charAt(0)), String.valueOf(typename.charAt(0)).toUpperCase()) + "(DbUtils.getRandomStringLsb().substring(0," + list.get(i).getLength() + "));\r\n";
      }
    }
    String classname = "NewAction";
    String datetime = DateUtils.newDateTime();
    try {
      //get template
      InputStream is = getResourceAsStream("template/action/new.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#package", _package);
      template = template.replaceAll("#classname", classname);
      template = template.replaceAll("#vo", vo);
      template = template.replaceAll("#datetime", datetime);
      template = template.replaceAll("#initPk", pkinit);
      template = template.replaceAll("#newid", table.getInsertSelectiveStatementId());


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

  public static boolean generateQueryAction(IntrospectedTable table,
                                            String namespace, String targetProject) {
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
    List<IntrospectedColumn> list = table.getPrimaryKeyColumns();


    String classname = "QueryAction";
    String datetime = DateUtils.newDateTime();
    String queryId = table.getSelectByPrimaryKeyQueryId();
    //String queryId = table.getSelectByNoNPrimaryKeyStatementId();
    try {
      //get template
      InputStream is = getResourceAsStream("template/action/query.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#package", _package);
      template = template.replaceAll("#classname", classname);

      template = template.replaceAll("#datetime", datetime);
      template = template.replaceAll("#queryid", queryId);


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

  public static boolean generateUpdateJsp(IntrospectedTable table, String namespace, String targetProject) {
    // TODO Auto-generated method stub

    StringBuffer form = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");

    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    for (IntrospectedColumn column : table.getAllColumns()) {
      String propertyname = column.getJavaProperty();

      String readonly = table.getPrimaryKeyColumns().contains(column) ? "readonly=\"true\"" : "";
      int length = column.getLength();
      int type = column.getJdbcType();
      StringBuffer fieldStr = new StringBuffer();
      fieldStr.append("<s:textfield " + readonly + " name=\"var." + propertyname + "\" size=\"" + propertyname + "\" size=\"" + length + "\" maxlength=\"" + (length) + "\"></s:textfield>");
      if (type == java.sql.Types.DATE || type == java.sql.Types.TIMESTAMP) {
        fieldStr.setLength(0);
        fieldStr.append("<sx:datetimepicker    displayFormat=\"yyyy-MM-dd\" name=\"var." + propertyname + "\"    cssStyle=\"width:200px\" ></sx:datetimepicker>");
      }
      if (table.getPrimaryKeyColumns().contains(column)) {
        fieldStr.setLength(0);

        if (type >= 91 && type <= 93) {
          fieldStr.append("<s:hidden name=\"var." + propertyname + "\"  format=\"yyyy-MM-dd HH:mm:ss\"></s:hidden>");
          fieldStr.append("<s:property value=\"var." + propertyname + "\"   format=\"yyyy-MM-dd HH:mm:ss\" />");
        } else {
          fieldStr.append("<s:hidden name=\"var." + propertyname + "\" ></s:hidden>");
          fieldStr.append("<s:property value=\"var." + propertyname + "\" />");
        }

      }

      form.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
      form.append(blank9).append("<td class=\"repCndLb\">").append(column.getActualColumnName()).append(":</td>").append("\r\n");
      form.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
      form.append(blank9).append(blank2).append(fieldStr).append("\r\n");
      form.append(blank9).append("</td>").append("\r\n");
      form.append(blank7).append("</tr>").append("\r\n");
    }


    try {
      //get template
      InputStream is = getResourceAsStream("template/jsp/update.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#namespace", namespace);
      template = template.replaceAll("#form", form.toString());


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
      String file = fileDir + File.separator + "update.jsp";
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

  public static boolean generateNewJsp(IntrospectedTable table, String namespace, String targetProject) {
    // TODO Auto-generated method stub


    StringBuffer form = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");

    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    for (IntrospectedColumn column : table.getAllColumns()) {
      String propertyname = column.getJavaProperty();
      int length = column.getLength();
      int type = column.getJdbcType();
      String readonly = table.getPrimaryKeyColumns().contains(column) ? "readonly=\"true\"" : "";
      String tips = "";

      String fieldStr = "<s:textfield name=\"var." + propertyname + "\" size=\"" + propertyname + "\" size=\"" + length + "\" maxlength=\"" + (length) + "\"></s:textfield>";
      if (type == java.sql.Types.DATE || type == java.sql.Types.TIMESTAMP) {
        fieldStr = "<sx:datetimepicker  displayFormat=\"yyyy-MM-dd\" name=\"var." + propertyname + "\"    cssStyle=\"width:200px\" ></sx:datetimepicker>";
      }

      if (table.getPrimaryKeyColumns().contains(column)) {
        tips = "<font color='red'>&nbsp;*&nbsp;Auto-generated If The Input Is Empty</font>";
      }

      form.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
      form.append(blank9).append("<td class=\"repCndLb\">").append(column.getActualColumnName()).append(":</td>").append("\r\n");
      form.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
      form.append(blank9).append(blank2).append(fieldStr).append(tips).append("\r\n");
      form.append(blank9).append("</td>").append("\r\n");
      form.append(blank7).append("</tr>").append("\r\n");
    }


    try {
      //get template
      InputStream is = getResourceAsStream("template/jsp/new.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#namespace", namespace);
      template = template.replaceAll("#form", form.toString());


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
      String file = fileDir + File.separator + "new.jsp";
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

  public static boolean generateMainJsp(IntrospectedTable table, String namespace, String targetProject) {
    // TODO Auto-generated method stub

    StringBuffer queryCondition = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    StringBuffer queryResultTitle = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    StringBuffer queryResultData = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    String blank7 = "						";
    String blank2 = "	";
    String blank9 = blank7 + blank2;
    for (IntrospectedColumn column : table.getAllColumns()) {
      String propertyname = column.getJavaProperty();
      int length = column.getLength();
      int type = column.getJdbcType();
      String fieldStr = "<s:textfield name=\"var." + propertyname + "\" id=\"" + propertyname + "\" size=\"" + length + "\" maxlength=\"" + (length + 1) + "\"></s:textfield>";
      if (type == java.sql.Types.DATE || type == java.sql.Types.TIMESTAMP) {
        fieldStr = "<sx:datetimepicker displayFormat=\"yyyy-MM-dd\" name=\"var." + propertyname + "\"    cssStyle=\"width:200px\" ></sx:datetimepicker>";
      }
      queryCondition.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
      queryCondition.append(blank9).append("<td class=\"repCndLb\">").append(column.getActualColumnName()).append(":</td>").append("\r\n");
      queryCondition.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
      queryCondition.append(blank9).append(blank2).append(fieldStr).append("\r\n");
      queryCondition.append(blank9).append("</td>").append("\r\n");
      queryCondition.append(blank7).append("</tr>").append("\r\n");


      //query result title
      queryResultTitle.append(blank9 + blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >").append("\r\n");
      queryResultTitle.append(blank9 + blank7).append(blank2).append(propertyname).append("\r\n");
      queryResultTitle.append(blank9 + blank7).append("</td>").append("\r\n");

      //query result data
      String format = "";
      if (column.getJdbcType() >= 91 && column.getJdbcType() <= 93) {
        format = " format=\"yyyy-MM-dd HH:mm:ss\" ";
      }
      queryResultData.append(blank9 + blank7).append("<td class=\"editGrid\"  align=\"left\">").append("\r\n");
      queryResultData.append(blank9 + blank7).append(blank2).append("<s:property value=\"#list.").append(propertyname).append("\"  " + format + "/>").append("\r\n");
      queryResultData.append(blank9 + blank7).append("</td>").append("\r\n");
    }
    //query result title.operation
    queryResultTitle.append(blank9 + blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >").append("\r\n");
    queryResultTitle.append(blank9 + blank7).append(blank2).append("Operation").append("\r\n");
    queryResultTitle.append(blank9 + blank7).append("</td>").append("\r\n");


    StringBuffer linkParameters = new StringBuffer();
    for (IntrospectedColumn column : table.getPrimaryKeyColumns()) {
      String propertyname = column.getJavaProperty();
      String format = "";
      if (column.getJdbcType() >= 91 && column.getJdbcType() <= 93) {
        format = " format=\"yyyy-MM-dd HH:mm:ss\" ";
      }
      linkParameters.append(blank9).append("<s:param name=\"var.").append(propertyname).append("\" value=\"#list.").append(propertyname).append("\"  " + format + "> </s:param>").append("\r\n");
    }

    try {
      //get template
      InputStream is = getResourceAsStream("template/jsp/main.template");
      String template = IOUtils.toString(is);
      template = template.replaceAll("#namespace", namespace);
      template = template.replaceAll("#queryCondition", queryCondition.toString());
      template = template.replaceAll("#queryresulttitle", queryResultTitle.toString());
      template = template.replaceAll("#queryresultdata", queryResultData.toString());
      template = template.replaceAll("#linkparameters", linkParameters.toString());
      template = template.replaceAll("#colspancount", String.valueOf(table.getAllColumns().size()));

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
      String file = fileDir + File.separator + "main.jsp";
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
    StringBuffer valueObjectName = new StringBuffer("");
    StringBuffer valueObjectField = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
    NamedNodeMap[] args = XmlUtils.parseItem();
    for (NamedNodeMap map : args) {
      String id = XmlUtils.getNodeValue(map,"id",true).toLowerCase();
      String type = XmlUtils.getNodeValue(map,"type",true).toLowerCase();
      String default_ = XmlUtils.getNodeValue(map,"default",true);
      valueObjectField.append("\t \t private ");
      if(type.equals("text") || type.equals("select")){
        valueObjectField.append("String ");
      }else if(type.equals("number")){
        valueObjectField.append("BigDecimal ");
      }else if(type.equals("date")){
        valueObjectField.append("Date ");
      }
      valueObjectField.append(id+" ;\r\n");
    }

    args = XmlUtils.parseItems();
    String namespace = XmlUtils.getNodeValue(args[0], "namespace");
    String tableName = XmlUtils.getNodeValue(args[0], "tableName");
    valueObjectName.append(tableName);
    try {

      HashMap<String,String> replace = new HashMap<>();
      replace.put("#VALUEOBJECTNAME#", valueObjectName.toString());
      replace.put("#VALUEOBJECTFIELD#", valueObjectField.toString());
      autoGenerateHtml("template/valueobject/valueobject.template",replace,tableName+".java");
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
    String updatestr="";
    for (NamedNodeMap map : args) {
      String id = XmlUtils.getNodeValue(map,"id",true).toLowerCase();
      String lable = XmlUtils.getNodeValue(map,"lable",true).toLowerCase();
      String type = XmlUtils.getNodeValue(map,"type",true).toLowerCase();
      String length = XmlUtils.getNodeValue(map,"length",true).toLowerCase();
      String default_ = XmlUtils.getNodeValue(map,"default",true);
      String primarykey = XmlUtils.getNodeValue(map,"primarykey",true).toLowerCase();
      String options = XmlUtils.getNodeValue(map,"options",true);
      if(!StringUtils.isEmptyOrNull(primarykey)){
        primarykeys.add(map);
        updatestr += id+"='+data[i]."+id+"+"+"'&";
      }
      //query conditions.
      String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id+ "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\">";
      if (type.toLowerCase().equals("date")) {
        fieldStr = "<input type=\"date\"  name=\"" + id + "\" value=\"" + default_ +"\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
      }else if(type.toLowerCase().equals("number")){
        fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\""+default_+ "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
      }else if(type.toLowerCase().equals("select")){
        String html="";
        html += "<select  id=\"" + id + "\"    name=\"" + id + "\" value=\""+default_+"\"  style=\"width:" + Integer.parseInt(length)  + "px\" > \r\t";
        String[] optons_ =  options.split("\\|");
        html += "<option value=\"\"></option> \r\t";
        for (int i = 0; i < optons_.length; i++) {
          html += "<option value=\""+optons_[i]+"\">"+optons_[i]+"</option> \r\t";
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
      if (type.toLowerCase().equals("datetime")) {
        format = " format=\"yyyy-MM-dd HH:mm:ss\" ";
      }
      queryResultData.append(blank7).append("trHtml +=\"<td class='editGrid'  align='left'>").append("\"");
      queryResultData.append("+data[i]."+id+"+");
      queryResultData.append("\"</td>\"").append(";\r\n");
    }
    //query result title.operation
    queryResultTitle.append(blank9 + blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >").append("\r\n");
    queryResultTitle.append(blank9 + blank7).append(blank2).append("Operation").append("\r\n");
    queryResultTitle.append(blank9 + blank7).append("</td>").append("\r\n");
    NamedNodeMap[] items = XmlUtils.parseItems();
    String namespace = XmlUtils.getNodeValue(items[0], "namespace");
    try {
      HashMap<String,String> replace = new HashMap<>();
      replace.put("#NAMESPACE#", namespace.concat(""));
      replace.put("#DONEW#", namespace.concat(""));
      replace.put("#QUERYCONDITION#", queryCondition.toString());
      replace.put("#GRIDHEAD#", queryResultTitle.toString());
      replace.put("#GRIDDATA#", queryResultData.toString());
      replace.put("#UPDATESTR#", updatestr.endsWith("&")?updatestr.substring(0,updatestr.length()-1): updatestr.toString());

      autoGenerateHtml("template/html/main.template",replace,"main.html");
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }


  private static void autoGenerateHtml(String templateFile, HashMap<String,String> replace, String newName) throws ParserConfigurationException, IOException, SAXException {
    InputStream is = getResourceAsStream(templateFile);
    String template = IOUtils.toString(is);
    for (String key:replace.keySet()) {
      template = template.replaceAll(key, replace.get(key));
    }
    NamedNodeMap[] args  = XmlUtils.parseConfigXml("file");
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
    System.out.println("file  " + file + "  created." );
  }


}