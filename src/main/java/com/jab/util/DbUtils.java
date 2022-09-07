package com.jab.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


// TODO: Auto-generated Javadoc

/**
 * @author mike
 * @version 1.0
 * @description: TODO
 * @date 2022/9/7 14:18
 */
public class DbUtils {

  /**
   * The Constant DB_TYPE_ORACLE.
   */
  public static final String DB_TYPE_ORACLE = "oracle";
  /**
   * The Constant DB_TYPE_MYSQL.
   */
  public static final String DB_TYPE_MYSQL = "mysql";
  /**
   * The Constant DB_TYPE_SQLSERVER.
   */
  public static final String DB_TYPE_SQLSERVER = "sqlserver";
  /**
   * The Constant DB_TYPE_DB2.
   */
  public static final String DB_TYPE_DB2 = "db2";
  /**
   * The Constant DB_TYPE_SYBASE.
   */
  public static final String DB_TYPE_SYBASE = "sybase";
  /**
   * The Constant DATE_FORMAT_JAVA.
   */
  private static final String DATE_FORMAT_JAVA = "yyyy-MM-dd HH:mm:ss";
  /**
   * The Constant ORACLE_DATE_FORMAT_DB.
   */
  private static final String ORACLE_DATE_FORMAT_DB = "yyyy-mm-dd hh24:mi:ss";
  /**
   * The Constant MYSQL_DATE_FORMAT_DB.
   */
  private static final String MYSQL_DATE_FORMAT_DB = "%Y-%m-%d %H:%i:%s";
  /**
   * The __log.
   */
  static Logger __log = Logger.getLogger(DbUtils.class);
  /**
   * The __sql session.
   */
  private static SqlSessionFactory __sqlSession = null;

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
   * _build sql session.
   *
   * @return the sql session
   */
  public static SqlSession buildSqlSession() {
    return DbUtils.builder().openSession();

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
  public static String buildURL(String driver, String addr, String port,
                                String dbName) {
    String str = driver.toLowerCase();
    if (str.indexOf("oracle") >= 0) {
      return "jdbc:oracle:thin:@" + addr + ":" + port + ":" + dbName;
    }
    if (str.indexOf("sqlserver") >= 0) {
      return "jdbc:sqlserver://" + addr + ":" + port + ";DatabaseName="
              + dbName;
    }

    if (str.indexOf("mysql") >= 0) {
      return "jdbc:mysql://" + addr + ":" + port + "/" + dbName;
    }

    return "";
  }

  /**
   * Title:  _buildConnect
   * Description: TODO(这里用一句话描述这个方法的作用)
   *
   * @param
   * @return Connection
   */
  public static Connection buildConnect(String driver, String url, String user, String password) throws ClassNotFoundException, SQLException {
    Class.forName(driver);
    return java.sql.DriverManager.getConnection(url, user, password);
  }


  public static BigDecimal getAutoIncrementLsb(String tablename, String columnName) {
    Connection conn = null;
    try {
      conn = buildSqlSession().getConnection();
      java.sql.Statement stat = conn.createStatement();
      java.sql.ResultSet rs = stat.executeQuery("select max(" + columnName + ")+1 from " + tablename);
      if (rs.next()) {
        return rs.getBigDecimal(1);
      }
      rs.close();
      rs = null;
      stat.close();
      stat = null;
      if (conn != null) conn.close();
      conn = null;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return new BigDecimal(0);
  }


  /**
   * _get random lsb(String.).
   *
   * @return the string
   */
  public static String getRandomStringLsb() {
    return StringUtils.randomString();
  }

  /**
   * 获取默认SQL会话工厂，使用默认配置文件$ClassPath/DBConfig.xml.
   *
   * @return the sql session factory
   */
  public static SqlSessionFactory builder() {
    if (__sqlSession == null) {
      Reader __reader;
      try {
        __reader = Resources.getResourceAsReader("DBConfig.xml");
        __sqlSession = new SqlSessionFactoryBuilder().build(__reader);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return __sqlSession;
  }

  /**
   * 获取SQL会话工厂，使用配置文件configPath.
   *
   * @param configPath the config path
   * @return the sql session factory
   */
  public static SqlSessionFactory builder(String configPath) {
    if (__sqlSession == null) {
      Reader reader;
      try {
        reader = Resources.getResourceAsReader(configPath);
        __sqlSession = new SqlSessionFactoryBuilder().build(reader);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return __sqlSession;
  }


}