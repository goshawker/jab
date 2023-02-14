package com.jab.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * @Description :
 * @Author : goshawker@yeah.net
 * @Date : 2023-02-14 11:43
 */

public class DBUtils {
    public static String driver;
    public static String user;
    public static String pwd;
    public static String url;
    static Logger log = LogManager.getLogger(DBUtils.class);
    private static Connection connection = null;

    public static void init() {
        NamedNodeMap[] vector;
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
                return;
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
        if (str.contains("oracle")) {
            return "jdbc:oracle:thin:@" + addr + ":" + port + ":" + dbName;
        }
        if (str.contains("sqlserver")) {
            return "jdbc:sqlserver://" + addr + ":" + port + ";DatabaseName=" + dbName;
        }
        if (str.contains("mysql")) {
            return "jdbc:mysql://" + addr + ":" + port + "/" + dbName;
        }
        return "";
    }


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