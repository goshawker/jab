package com.jab.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


/**
 * @Author: goshawker@yeah.net
 * @Description:
 * @Date: 2022/9/12 11:04
 * @Version: 1.0
 */
public class DBUtils {
//
//  /**
//   * The Constant DB_TYPE_ORACLE.
//   */
//  public static final String DB_TYPE_ORACLE = "oracle";
//  /**
//   * The Constant DB_TYPE_MYSQL.
//   */
//  public static final String DB_TYPE_MYSQL = "mysql";
//  /**
//   * The Constant DB_TYPE_SQLSERVER.
//   */
//  public static final String DB_TYPE_SQLSERVER = "sqlserver";
//  /**
//   * The Constant DB_TYPE_DB2.
//   */
//  public static final String DB_TYPE_DB2 = "db2";
//  /**
//   * The Constant DB_TYPE_SYBASE.
//   */
//  public static final String DB_TYPE_SYBASE = "sybase";
//  /**
//   * The Constant DATE_FORMAT_JAVA.
//   */
//  private static final String DATE_FORMAT_JAVA = "yyyy-MM-dd HH:mm:ss";
//  /**
//   * The Constant ORACLE_DATE_FORMAT_DB.
//   */
//  private static final String ORACLE_DATE_FORMAT_DB = "yyyy-mm-dd hh24:mi:ss";
//  /**
//   * The Constant MYSQL_DATE_FORMAT_DB.
//   */
//  private static final String MYSQL_DATE_FORMAT_DB = "%Y-%m-%d %H:%i:%s";

  private static Connection connection = null;
  private static String driver;
  private static String user;
  private static String pwd;
  private static String url;
  /**
   * The __log.
   */
  static Logger log = LogManager.getLogger(DBUtils.class);

  public static void init() {
    NamedNodeMap[] vector = new NamedNodeMap[0];
    try {
      vector = XmlUtils.parseDatabase();
      String driver = XmlUtils.getNodeValue(vector[0], "driver");
      String ip = XmlUtils.getNodeValue(vector[0], "ip");
      String port = XmlUtils.getNodeValue(vector[0], "port");
      String instance = XmlUtils.getNodeValue(vector[0], "instance");
      String user = XmlUtils.getNodeValue(vector[0], "user");
      String pwd = XmlUtils.getNodeValue(vector[0], "pwd");
      if (driver == null || ip == null || port == null || instance == null || user == null || pwd == null) {
        log.error("reading config.xml error.");
      }

      String url = buildURL(driver, ip, port, instance);
      DBUtils.driver = driver;
      DBUtils.user = user;
      DBUtils.pwd = pwd;
      DBUtils.url = url;
    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * To sql string.
   *
   * @param value  the value
   * @param dbType the db type
   * @return the string
   */
  public static String toSqlString(Object value, String dbType) {
    String valStr = "''";
    if ((value instanceof Date)) {
      if ("mysql".equals(dbType)) {
        valStr = "STR_TO_DATE('" + dateToStr((Date) value) + "','"
                + "%Y-%m-%d %H:%i:%s" + "')";
      } else {
        valStr = "to_date('" + dateToStr((Date) value) + "','"
                + "yyyy-mm-dd hh24:mi:ss" + "')";
      }

    } else if ((value instanceof Long)) {
      valStr = String.valueOf(value);
    } else if ((value instanceof Integer)) {
      valStr = String.valueOf(value);
    } else if ((value instanceof Double)) {
      valStr = String.valueOf(value);
    } else if (((String) value).equalsIgnoreCase("sysdate")) {
      valStr = (String) value;
    } else {
      valStr = "'" + value + "'";
    }
    return valStr;
  }

  /**
   * Date to str.
   *
   * @param d the d
   * @return the string
   */
  private static String dateToStr(Date d) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return format.format(d);
  }


  /**
   * Builds the url.
   *
   * @param driver the driver
   * @param addr   the addr
   * @param port   the port
   * @param dbName the db name
   * @return the string
   */
  private static String buildURL(String driver, String addr, String port,
                                 String dbName) {
    String str = driver.toLowerCase();
    if (str.indexOf("oracle") >= 0) {
      return "jdbc:oracle:thin:@" + addr + ":" + port + ":" + dbName;
    }
    if (str.indexOf("sqlserver") >= 0) {
      return "jdbc:sqlserver://" + addr + ":" + port + ";DatabaseName=" + dbName;
    }
    if (str.indexOf("mysql") >= 0) {
      return "jdbc:mysql://" + addr + ":" + port + "/" + dbName;
    }
    return "";
  }

  /**
   * Title:  _buildConnect
   * Description:
   *
   * @param
   * @return Connection
   */
  public static Connection buildConnect() throws ClassNotFoundException, SQLException {
    if (connection != null && !connection.isClosed()) {
      return connection;
    }
    init();
    Class.forName(driver);
    connection = java.sql.DriverManager.getConnection(url, user, pwd);
    return connection;
  }


}