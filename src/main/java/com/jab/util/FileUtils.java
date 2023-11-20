package com.jab.util;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;


/**
 * @Description :
 * @Author : goshawker@yeah.net
 * @Date : 2023-02-14 10:48
 */

public class FileUtils {

    /**
     * The _log.
     */
    static Logger log = LogManager.getLogger(FileUtils.class);
    static HashMap scripts = new HashMap<String, String>();

    public static void clearScripts() {
        scripts.clear();
    }

    /**
     * Gets the resource as stream.
     *
     * @param resource the resource
     * @return the resource as stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static InputStream getResourceAsStream(String resource) throws IOException {
        InputStream inputStream = FileUtils.class.getResourceAsStream(resource);
        if (inputStream == null && !resource.startsWith("/")) {
            resource = "/".concat(resource);
        }
        inputStream = FileUtils.class.getResourceAsStream(resource);
        if (inputStream == null) {
            log.error("Could not find resource " + resource);
            throw new FileNotFoundException(resource);
        }
        return inputStream;
    }

    public static void generateAction() throws Exception {
        String blank7 = "	    ";
        /*查询代码*/
        StringBuffer queryCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");
        /*新增代码*/
        StringBuffer insertCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");
        /*更新代码*/
        StringBuffer updateCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");
        /*删除代码*/
        StringBuffer deleteCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");

        NamedNodeMap[] items = XmlUtils.parseItems();
        String namespace = XmlUtils.getNodeValue(items[0], "namespace");
        String tableName = XmlUtils.getNodeValue(items[0], "tableName");

        StringBuilder QuerySQL = new StringBuilder("select ");
        StringBuilder UpdateSQL = new StringBuilder("update ").append(tableName).append(" set ");

        StringBuilder values_insert = new StringBuilder();

        NamedNodeMap[] args = XmlUtils.parseItem();
        Vector<String> keys_columns = new Vector<>();
        Vector<String> nokeys_columns = new Vector<>();
        Vector<String> all_columns = new Vector<>();
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id").toLowerCase();
            String primaryKey = XmlUtils.getNodeValue(map, "primarykey").toLowerCase();
            //InsertSQL.append(id).append(",");
            QuerySQL.append(id).append(",");
            all_columns.add(id);
            // whereCondition_query.append(" and ").append(id).append("=? ");
            values_insert.append("?,");
            if (StringUtils.isNotEmptyAndNull(primaryKey)) {
                /*主键部分*/
                keys_columns.add(id);
                deleteCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
                // whereCondition.append(" and ").append(id).append("=? ");
            } else {
                /*非主键部分*/
                nokeys_columns.add(id);
                UpdateSQL.append(id).append(" = ?,");
            }

            insertCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
            updateCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
            queryCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
        }
        //处理列拼接，多余部分
        if (UpdateSQL.toString().trim().endsWith(",")) {
            String tmp = UpdateSQL.toString().trim().substring(0, UpdateSQL.length() - 1);
            UpdateSQL.setLength(0);
            UpdateSQL.append(tmp);
        }
        //处理列拼接，多余部分
        if (values_insert.toString().trim().endsWith(",")) {
            String tmp = values_insert.substring(0, values_insert.length() - 1);
            values_insert.setLength(0);
            values_insert.append(tmp).append(")");
        }

        //处理列拼接，多余部分
        if (QuerySQL.toString().trim().endsWith(",")) {
            String tmp = QuerySQL.substring(0, QuerySQL.length() - 1);
            QuerySQL.setLength(0);
            QuerySQL.append(tmp);
        }
        QuerySQL.append(" from ").append(tableName);

        //插入SQL
        insertCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");
        //删除SQL
        deleteCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");
        //更新SQL
        updateCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");
        //查询SQL
        queryCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");

        /*处理动态新增SQL*/
        buildDynamicInsertSQL("insert into " + tableName, keys_columns, all_columns, insertCode);
        /*处理动态查询SQL*/
        buildDynamicWhereConditions(QuerySQL.toString(), all_columns, queryCode);
        /*处理动态更新SQL*/
        buildDynamicUpdateSQL(UpdateSQL.toString(), keys_columns, nokeys_columns, updateCode);
        /*处理动态删除SQL*/
        buildDynamicWhereConditions("delete from " + tableName, keys_columns, deleteCode);
        deleteCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        updateCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        insertCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        queryCode.append(blank7).append("java.sql.ResultSet rs = ps.executeQuery();").append("\r\n");
        queryCode.append(blank7).append("JSONString = toJSONArray(rs).toString();").append("\r\n");

        try {
            HashMap<String, String> replace = new HashMap<>();
            replace.put("#PACKAGE#", namespace.substring(1).replaceAll("/", "."));
            replace.put("#NAMESPACE#", namespace);
            replace.put("#INSERTCODE#", insertCode.toString());
            replace.put("#QUERYCODE#", queryCode.toString());
            replace.put("#DELETECODE#", deleteCode.toString());
            replace.put("#UPDATECODE#", updateCode.toString());
            DBUtils.init();
            replace.put("#DRIVER#", DBUtils.driver);
            replace.put("#USER#", DBUtils.user);
            replace.put("#PWD#", DBUtils.pwd);
            replace.put("#URL#", DBUtils.url);
            generateCode("template/active.java.template", replace, "JabActive.java", namespace);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateAction(String namespace, String tableName, NamedNodeMap[] items) throws Exception {
        String blank7 = "	    ";
        /*查询代码*/
        StringBuffer queryCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");
        /*新增代码*/
        StringBuffer insertCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");
        /*更新代码*/
        StringBuffer updateCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");
        /*删除代码*/
        StringBuffer deleteCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");
        /*初始化代码*/
        StringBuffer initDmCode = new StringBuffer("/** TODO Auto-generated  */").append("\r\n");

//        NamedNodeMap[] items = XmlUtils.parseItems();
//        String namespace = XmlUtils.getNodeValue(items[0], "namespace");
//        String tableName = XmlUtils.getNodeValue(items[0], "tableName");

        StringBuilder QuerySQL = new StringBuilder("select ");
        StringBuilder UpdateSQL = new StringBuilder("update ").append(tableName).append(" set ");

        StringBuilder values_insert = new StringBuilder();

        Vector<String> keys_columns = new Vector<>();
        Vector<String> nokeys_columns = new Vector<>();
        Vector<String> all_columns = new Vector<>();
        for (NamedNodeMap map : items) {
            String id = XmlUtils.getNodeValue(map, "id").toLowerCase();
            String primaryKey = XmlUtils.getNodeValue(map, "primarykey").toLowerCase();

            String options = XmlUtils.getNodeValue(map, "options");
            if (options.startsWith("#") && options.endsWith("#")) {
                initDmCode.append(blank7).append("dmMap.put(\"DM#" + id + "\",\"" + options.substring(1, options.length() - 1) + "\"); \r\n");
            }
            //InsertSQL.append(id).append(",");
            QuerySQL.append(id).append(",");
            all_columns.add(id);
            // whereCondition_query.append(" and ").append(id).append("=? ");
            values_insert.append("?,");
            if (StringUtils.isNotEmptyAndNull(primaryKey)) {
                /*主键部分*/
                keys_columns.add(id);
                deleteCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
                // whereCondition.append(" and ").append(id).append("=? ");
            } else {
                /*非主键部分*/
                nokeys_columns.add(id);
                UpdateSQL.append(id).append(" = ?,");
            }

            insertCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
            updateCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
            queryCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
        }
        //处理列拼接，多余部分
        if (UpdateSQL.toString().trim().endsWith(",")) {
            String tmp = UpdateSQL.toString().trim().substring(0, UpdateSQL.length() - 1);
            UpdateSQL.setLength(0);
            UpdateSQL.append(tmp);
        }
        //处理列拼接，多余部分
        if (values_insert.toString().trim().endsWith(",")) {
            String tmp = values_insert.substring(0, values_insert.length() - 1);
            values_insert.setLength(0);
            values_insert.append(tmp).append(")");
        }

        //处理列拼接，多余部分
        if (QuerySQL.toString().trim().endsWith(",")) {
            String tmp = QuerySQL.substring(0, QuerySQL.length() - 1);
            QuerySQL.setLength(0);
            QuerySQL.append(tmp);
        }
        QuerySQL.append(" from ").append(tableName);

        //插入SQL
        insertCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");
        //删除SQL
        deleteCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");
        //更新SQL
        updateCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");
        //查询SQL
        queryCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");

        /*处理动态新增SQL*/
        buildDynamicInsertSQL("insert into " + tableName, keys_columns, all_columns, insertCode);
        /*处理动态查询SQL*/
        buildDynamicWhereConditions(QuerySQL.toString(), all_columns, queryCode);
        /*处理动态更新SQL*/
        buildDynamicUpdateSQL(UpdateSQL.toString(), keys_columns, nokeys_columns, updateCode);
        /*处理动态删除SQL*/
        buildDynamicWhereConditions("delete from " + tableName, keys_columns, deleteCode);
        deleteCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        updateCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        insertCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        queryCode.append(blank7).append("java.sql.ResultSet rs = ps.executeQuery();").append("\r\n");
        queryCode.append(blank7).append("JSONString = toJSONArray(rs).toString();").append("\r\n");

        try {
            HashMap<String, String> replace = new HashMap<>();
            replace.put("#PACKAGE#", namespace.substring(1).replaceAll("/", "."));
            replace.put("#NAMESPACE#", namespace);
            replace.put("#INSERTCODE#", insertCode.toString());
            replace.put("#QUERYCODE#", queryCode.toString());
            replace.put("#DELETECODE#", deleteCode.toString());
            replace.put("#UPDATECODE#", updateCode.toString());
            replace.put("#INITDMCODE#", initDmCode.toString());
            DBUtils.init();
            replace.put("#DRIVER#", DBUtils.driver);
            replace.put("#USER#", DBUtils.user);
            replace.put("#PWD#", DBUtils.pwd);
            replace.put("#URL#", DBUtils.url);
            generateCode("template/active.java.template", replace, "JabActive.java", namespace);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateCss(String namespace) throws Exception {
        HashMap<String, String> replace = new HashMap<>();
        generateCode("template/common.css.template", replace, "common.css", namespace);
    }

    public static void generateJavascript(String namespace) throws Exception {
        generateCode("template/common.js.template", scripts, "common.js", namespace);
    }

    /**
     * 生成动态SQL,WHERE条件部分
     *
     * @param resultSQL  sql前缀，如：select * from xxx
     * @param parameters 需要校验是否为空的请求参数，如果为空则忽略掉
     * @param javaCode   返回的java代码
     */
    public static void buildDynamicWhereConditions(String resultSQL, Vector<String> parameters, StringBuffer javaCode) {
        // StringBuffer javaCode = new StringBuffer("");
        String blank7 = "	    ";
        String blank9 = "	      ";
        if (resultSQL.length() > 0) {
            javaCode.append(blank7).append("java.util.Vector<String> lst_value = new java.util.Vector<>();").append("\r\n");
            javaCode.append(blank7).append("java.util.Vector<String> lst_key = new java.util.Vector<>();").append("\r\n");
            //int i = 0;
            for (String id : parameters) {
                //i++;
                //处理查询条件
                javaCode.append(blank7).append("if(").append(id).append("!=null && ").append(id).append(".length() > 0){").append("\r\n");
                javaCode.append(blank9).append("lst_value.add(").append(id).append("); \r\n");
                javaCode.append(blank9).append("lst_key.add(\"").append(id).append("\"); \r\n");
                javaCode.append(blank7).append("}").append("\r\n");
            }
            javaCode.append(blank7).append("for(String s : lst_key){").append("\r\n");
            javaCode.append(blank9).append("todoSql += \" and \" + s +\" like ?\" ;").append("\r\n");
            javaCode.append(blank7).append("}").append("\r\n");

            javaCode.append(blank7).append("todoSql =\" ").append(resultSQL).append(" where 1=1 \" + todoSql; ").append("\r\n");
            javaCode.append(blank7).append("ps =  buildConnect().prepareStatement(todoSql);").append("\r\n");

            javaCode.append(blank7).append("for(int i=0;i<lst_value.size();i++){").append("\r\n");
            javaCode.append(blank9).append("ps.setString(i+1,\"%\"+lst_value.get(i)+\"%\");").append("\r\n");
            javaCode.append(blank7).append("}").append("\r\n");
        }
        // return javaCode;
    }

    /**
     * 生产insert语句
     *
     * @param prefixSQL      前缀SQL
     * @param keys_columns   主键列
     * @param nokeys_columns 非主键列
     * @param javaCode       生成的java代码
     */
    public static void buildDynamicUpdateSQL(String prefixSQL, Vector<String> keys_columns, Vector<String> nokeys_columns, StringBuffer javaCode) {
        // StringBuffer javaCode = new StringBuffer("");
        String blank7 = "	    ";
        String blank9 = "	      ";
        if (prefixSQL.length() > 0) {
            javaCode.append(blank7).append("java.util.Vector<String> lst_value = new java.util.Vector<>();").append("\r\n");
            //javaCode.append(blank7).append("java.util.Vector<String> lst_key = new java.util.Vector<>();").append("\r\n");
            // int i = 0;
            /*处理非key*/
            for (String id : nokeys_columns) {
                // i++;
                /*处理非主键字段，为null改为”“*/
                javaCode.append(blank9).append("/**处理非主键*/ \r\n");
                javaCode.append(blank7).append("if(").append(id).append("==null){").append("\r\n");
                javaCode.append(blank9).append(id).append("=\"\"; \r\n");
                javaCode.append(blank7).append("}").append("\r\n");
                javaCode.append(blank9).append("lst_value.add(").append(id).append("); \r\n");
                //javaCode.append(blank9).append("lst_key.add(\"").append(id).append("\"); \r\n");
            }
            /*处理key*/
            for (String id : keys_columns) {
                //i++;
                //处理查询条件
                javaCode.append(blank9).append("/**处理主键*/ \r\n");
                javaCode.append(blank9).append("lst_value.add(").append(id).append("); \r\n");
                //javaCode.append(blank9).append("lst_key.add(\"").append(id).append("\"); \r\n");
                javaCode.append(blank9).append("todoSql += \" and ").append(id).append("=?\" ;").append("\r\n");
            }

            javaCode.append(blank7).append("todoSql =\" ").append(prefixSQL).append(" where 1=1 \" + todoSql; ").append("\r\n");
            javaCode.append(blank7).append("ps =  buildConnect().prepareStatement(todoSql);").append("\r\n");

            javaCode.append(blank7).append("for(int i=0;i<lst_value.size();i++){").append("\r\n");
            // queryCode.append(blank9).append("todoSql += \" and \" + lst_key.get(i) +\"=?\" ;").append("\r\n");
            javaCode.append(blank9).append("ps.setString(i+1,lst_value.get(i));").append("\r\n");
            javaCode.append(blank7).append("}").append("\r\n");
        }
        //return javaCode;
    }

    /**
     * 生产insert语句
     *
     * @param prefixSQL    前缀SQL
     * @param keys_columns 主键列
     * @param all_columns  所有列
     * @param javaCode     生成的java代码
     */
    public static void buildDynamicInsertSQL(String prefixSQL, Vector<String> keys_columns, Vector<String> all_columns, StringBuffer javaCode) {
        // StringBuffer javaCode = new StringBuffer("");
        String blank7 = "	    ";
        String blank9 = "	      ";
        if (prefixSQL.length() > 0) {
            javaCode.append(blank7).append("java.util.Vector<String> lst_value = new java.util.Vector<>();").append("\r\n");
            javaCode.append(blank7).append("java.util.Vector<String> lst_key = new java.util.Vector<>();").append("\r\n");
            // int i = 0;
            for (String id : all_columns) {
                // i++;
                if (keys_columns.contains(id)) {
                    //如果主键为null或者为空，返回0
                    javaCode.append(blank7).append("if(").append(id).append("==null || ").append(id).append(".length() == 0){").append("\r\n");
                    javaCode.append(blank9).append("return \"0\"; \r\n");
                    javaCode.append(blank7).append("}else{").append("\r\n");
                } else {
                    javaCode.append(blank7).append("if(").append(id).append(".length() > 0){").append("\r\n");
                }
                javaCode.append(blank9).append("lst_value.add(").append(id).append("); \r\n");
                javaCode.append(blank9).append("lst_key.add(\"").append(id).append("\"); \r\n");
                javaCode.append(blank7).append("}").append("\r\n");
            }
            //拼接字段
            javaCode.append(blank7).append("for(int i=0;i<lst_key.size();i++){").append("\r\n");
            javaCode.append(blank9).append("todoSql +=  lst_key.get(i)  ;").append("\r\n");
            javaCode.append(blank9).append("if(i<lst_key.size()-1){ todoSql +=  \",\"; };").append("\r\n");
            javaCode.append(blank7).append("}").append("\r\n");

            //拼接?
            javaCode.append(blank7).append("todoSql = \"(\" +todoSql+ \") values(\" ;").append("\r\n");
            javaCode.append(blank7).append("for(int i=0;i<lst_key.size();i++){").append("\r\n");
            javaCode.append(blank9).append("todoSql +=  \"?\" ;").append("\r\n");
            javaCode.append(blank9).append("if(i<lst_key.size()-1){todoSql +=  \",\"; };").append("\r\n");
            javaCode.append(blank7).append("}").append("\r\n");

            javaCode.append(blank7).append("todoSql =\" ").append(prefixSQL).append(" \" +todoSql+\")\"; ").append("\r\n");
            javaCode.append(blank7).append("ps =  buildConnect().prepareStatement(todoSql);").append("\r\n");

            javaCode.append(blank7).append("for(int i=0;i<lst_value.size();i++){").append("\r\n");
            javaCode.append(blank9).append("ps.setString(i+1,lst_value.get(i));").append("\r\n");
            javaCode.append(blank7).append("}").append("\r\n");
        }
        //return javaCode;
    }


    public static void generateUpdateHtml(String namespace, NamedNodeMap[] args) throws Exception {
        StringBuilder formField = new StringBuilder("<!-- // TODO Auto-generated  -->").append("\r\n");
        String blank7 = "						";
        String blank2 = "	";
        String blank9 = blank7 + blank2;
//        NamedNodeMap[] args = XmlUtils.parseItem();
        StringBuilder script = new StringBuilder("\r\n");
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id").toLowerCase();
            String lable = XmlUtils.getNodeValue(map, "lable").toLowerCase();
            String type = XmlUtils.getNodeValue(map, "type").toLowerCase();
            String length = XmlUtils.getNodeValue(map, "length").toLowerCase();
            String default_ = XmlUtils.getNodeValue(map, "default");
            String primarykey = XmlUtils.getNodeValue(map, "primarykey").toLowerCase();
            String options = XmlUtils.getNodeValue(map, "options");
            String readOnly = "";

            if (StringUtils.isNotEmptyAndNull(primarykey) && primarykey.equalsIgnoreCase("true")) {
                readOnly = "readonly";
                lable = "<font style=\"color:red\">" + lable + "</font>";
            }
            script.append(blank2).append("document.getElementById('").append(id).append("').value= getParameter('").append(id).append("');\r\n");
            String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\"   " + readOnly + ">";
            if (type.equalsIgnoreCase("date")) {
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\"   " + readOnly + ">";
            } else if (type.equalsIgnoreCase("select")) {
                StringBuilder html = new StringBuilder();
                html.append("<select  id=\"").append(id).append("\"    name=\"").append(id).append("\" id=\"").append(id).append("\" value=\"").append(default_).append("\"  style=\"width:").append(Integer.parseInt(length)).append("px\"   ").append(readOnly).append("> \r\t");
                assert options != null;
                String[] optons_ = options.split("\\|");
                html.append("<option value=\"\"></option> \r\t");
                for (String s : optons_) {
                    html.append("<option value=\"").append(s).append("\">").append(s).append("</option> \r\t");
                }
                html.append("</select>");
                fieldStr = html.toString();
            }
            formField.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
            formField.append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n");
            formField.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
            formField.append(blank9).append(blank2).append(fieldStr).append("\r\n");
            formField.append(blank9).append("</td>").append("\r\n");
            formField.append(blank7).append("</tr>").append("\r\n");
        }
//        NamedNodeMap[] items = XmlUtils.parseItems();
//        String namespace = XmlUtils.getNodeValue(items[0], "namespace");
        try {
            HashMap<String, String> replace = new HashMap<>();
            replace.put("#NAMESPACE#", namespace);
            replace.put("#FORMFIELD#", formField.toString());
            // replace.put("#INITDATA#", script.toString());
            scripts.put("#INITDATA#", script.toString());
            generateCode("template/update.html.template", replace, "update.html", namespace);
            //return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return false;
    }

    /**
     * @throws Exception
     * @deprecated
     */
    public static void generateUpdateHtml() throws Exception {
        StringBuilder formField = new StringBuilder("<!-- // TODO Auto-generated  -->").append("\r\n");
        String blank7 = "						";
        String blank2 = "	";
        String blank9 = blank7 + blank2;
        NamedNodeMap[] args = XmlUtils.parseItem();
        StringBuilder script = new StringBuilder("\r\n");
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id").toLowerCase();
            String lable = XmlUtils.getNodeValue(map, "lable").toLowerCase();
            String type = XmlUtils.getNodeValue(map, "type").toLowerCase();
            String length = XmlUtils.getNodeValue(map, "length").toLowerCase();
            String default_ = XmlUtils.getNodeValue(map, "default");
            String primarykey = XmlUtils.getNodeValue(map, "primarykey").toLowerCase();
            String options = XmlUtils.getNodeValue(map, "options");
            String readOnly = "";

            if (StringUtils.isNotEmptyAndNull(primarykey) && primarykey.equalsIgnoreCase("true")) {
                readOnly = "readonly";
                lable = "<font style=\"color:red\">" + lable + "</font>";
            }
            script.append(blank2).append("document.getElementById('").append(id).append("').value= getParameter('").append(id).append("');\r\n");
            String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\"   " + readOnly + ">";
            if (type.equalsIgnoreCase("date")) {
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\"   " + readOnly + ">";
            } else if (type.equalsIgnoreCase("select")) {
                StringBuilder html = new StringBuilder();
                html.append("<select  id=\"").append(id).append("\"    name=\"").append(id).append("\" id=\"").append(id).append("\" value=\"").append(default_).append("\"  style=\"width:").append(Integer.parseInt(length)).append("px\"   ").append(readOnly).append("> \r\t");
                assert options != null;
                String[] optons_ = options.split("\\|");
                html.append("<option value=\"\"></option> \r\t");
                for (String s : optons_) {
                    html.append("<option value=\"").append(s).append("\">").append(s).append("</option> \r\t");
                }
                html.append("</select>");
                fieldStr = html.toString();
            }
            formField.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
            formField.append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n");
            formField.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
            formField.append(blank9).append(blank2).append(fieldStr).append("\r\n");
            formField.append(blank9).append("</td>").append("\r\n");
            formField.append(blank7).append("</tr>").append("\r\n");
        }
        NamedNodeMap[] items = XmlUtils.parseItems();
        String namespace = XmlUtils.getNodeValue(items[0], "namespace");
        try {
            HashMap<String, String> replace = new HashMap<>();
            replace.put("#NAMESPACE#", namespace);
            replace.put("#FORMFIELD#", formField.toString());
            // replace.put("#INITDATA#", script.toString());
            scripts.put("#INITDATA#", script.toString());
            generateCode("template/update.html.template", replace, "update.html", namespace);
            //return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return false;
    }

    public static void generateNewHtml(String namespace, NamedNodeMap[] args) throws Exception {
        StringBuilder formField = new StringBuilder("<!-- // TODO Auto-generated  -->").append("\r\n");

        String blank7 = "						";
        String blank2 = "	";
        String blank9 = blank7 + blank2;
//        NamedNodeMap[] args = XmlUtils.parseItem();
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id").toLowerCase();
            String lable = XmlUtils.getNodeValue(map, "lable").toLowerCase();
            String type = XmlUtils.getNodeValue(map, "type").toLowerCase();
            String length = XmlUtils.getNodeValue(map, "length").toLowerCase();
            String default_ = XmlUtils.getNodeValue(map, "default");
            //String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
            String options = XmlUtils.getNodeValue(map, "options");

            String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\">";
            if (type.equalsIgnoreCase("date")) {
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
            } else if (type.equalsIgnoreCase("select")) {
                fieldStr = buildSelectHtml(id, length, default_, options);
            }
            formField.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
            formField.append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n");
            formField.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
            formField.append(blank9).append(blank2).append(fieldStr).append("\r\n");
            formField.append(blank9).append("</td>").append("\r\n");
            formField.append(blank7).append("</tr>").append("\r\n");
        }
//        NamedNodeMap[] items = XmlUtils.parseItems();
//        String namespace = XmlUtils.getNodeValue(items[0], "namespace");
        try {
            HashMap<String, String> replace = new HashMap<>();
            replace.put("#NAMESPACE#", namespace);
            replace.put("#FORMFIELD#", formField.toString());
            scripts.put("#NAMESPACE#", namespace);
            generateCode("template/new.html.template", replace, "new.html", namespace);
            // return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return false;
    }

    /**
     * @throws Exception
     * @deprecated
     */
    public static void generateNewHtml() throws Exception {
        StringBuilder formField = new StringBuilder("<!-- // TODO Auto-generated  -->").append("\r\n");

        String blank7 = "						";
        String blank2 = "	";
        String blank9 = blank7 + blank2;
        NamedNodeMap[] args = XmlUtils.parseItem();
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id").toLowerCase();
            String lable = XmlUtils.getNodeValue(map, "lable").toLowerCase();
            String type = XmlUtils.getNodeValue(map, "type").toLowerCase();
            String length = XmlUtils.getNodeValue(map, "length").toLowerCase();
            String default_ = XmlUtils.getNodeValue(map, "default");
            //String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
            String options = XmlUtils.getNodeValue(map, "options");

            String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\">";
            if (type.equalsIgnoreCase("date")) {
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
            } else if (type.equalsIgnoreCase("select")) {
                fieldStr = buildSelectHtml(id, length, default_, options);
            }
            formField.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
            formField.append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n");
            formField.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
            formField.append(blank9).append(blank2).append(fieldStr).append("\r\n");
            formField.append(blank9).append("</td>").append("\r\n");
            formField.append(blank7).append("</tr>").append("\r\n");
        }
        NamedNodeMap[] items = XmlUtils.parseItems();
        String namespace = XmlUtils.getNodeValue(items[0], "namespace");
        try {
            HashMap<String, String> replace = new HashMap<>();
            replace.put("#NAMESPACE#", namespace);
            replace.put("#FORMFIELD#", formField.toString());
            scripts.put("#NAMESPACE#", namespace);
            generateCode("template/new.html.template", replace, "new.html", namespace);
            // return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return false;
    }

    /**
     * @throws Exception
     * @deprecated
     */
    public static void generateMainHtml() throws Exception {
        /*查询条件*/
        StringBuilder queryCondition = new StringBuilder("<!-- // TODO Auto-generated  -->").append("\r\n");

        StringBuilder queryResultTitle = new StringBuilder("<!-- // TODO Auto-generated  -->").append("\r\n");
        //查询结果数据
        StringBuilder queryResultData = new StringBuilder(" //TODO Auto-generated ").append("\r\n");
        String blank7 = "						";
        String blank2 = "	";
        String blank9 = blank7 + blank2;
        /* 读取config.xml配置*/
        NamedNodeMap[] args = XmlUtils.parseItem();
        /*更新记录用的，主键字符串*/
        String updateStr = "";
        /*生成查询功能html*/
        StringBuilder requstString = new StringBuilder();
        /*遍历config.xml中的item*/
        int k = 0;
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id").toLowerCase();
            String lable = XmlUtils.getNodeValue(map, "lable").toLowerCase();
            String type = XmlUtils.getNodeValue(map, "type").toLowerCase();
            String length = XmlUtils.getNodeValue(map, "length").toLowerCase();
            String default_ = XmlUtils.getNodeValue(map, "default");
            // String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
            String options = XmlUtils.getNodeValue(map, "options");

            /*记录更新关键字，用于修改、删除等操作*/
            updateStr += id + "='+data[i]." + id + "+" + "'&";
            /*生成javascript脚本*/
            requstString.append(id).append("='+document.getElementById('").append(id).append("').value+").append("'&");
            /*查询字段，默认为文本输入类型*/
            String fieldStr;

            if (type.equalsIgnoreCase("date")) {
                /*处理日期类型*/
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                /*处理数字类型*/
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
            } else if (type.equalsIgnoreCase("select")) {
                /*处理下拉列表*/
                fieldStr = buildSelectHtml(id, length, default_, options);
            } else {
                fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\">";
            }
            /*生成一个查询组件： <tr><td>组件</td></tr>*/
            queryCondition.append(blank7).append("<tr class=\"repCnd\">").append("\r\n")
                    .append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n")
                    .append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n")
                    .append(blank9).append(blank2).append(fieldStr).append("\r\n")
                    .append(blank9).append("</td>").append("\r\n")
                    .append(blank7).append("</tr>").append("\r\n");
            /*生成查询结果表的数据部分表头*/
            queryResultTitle.append(blank9).append(blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >")
                    .append("\r\n")
                    .append(blank9).append(blank7).append(blank2).append(lable)
                    .append("\r\n")
                    .append(blank9 + blank7).append("</td>").append("\r\n");
            /*生成查询结果数据，灌装数据*/

            queryResultData.append(blank7).append("trHtml +=\"<td class='editGrid'  align='left'>").append("\"")
                    .append("+data[i]." + id + "+")
                    .append("\"</td>\"").append(";\r\n");

            /*最后一个元素，添加操作代码*/
            if (args.length - 1 == k++) {
                /*去掉最后一个& */
                if (updateStr.endsWith("&")) {
                    updateStr = updateStr.substring(0, updateStr.length() - 1);
                }
                queryResultData.append(blank7).append("let jabUpdateStr='").append(updateStr).append("';\r\n")
                        .append(blank7).append("trHtml +=\"<td class='editGrid'  align='left'>")
                        .append("<a href=\\\\\"javascript:jabEdit('\"+jabUpdateStr+\"')\\\\\">修改</a> &nbsp;&nbsp;  <a href=\\\\\"javascript:jabDel('\"+jabUpdateStr+\"')\\\\\">删除</a> ")
                        .append("</td>\"").append(";\r\n");
            }

        }
        /*生成查询结果表的操作部分表头*/
        queryResultTitle.append(blank9 + blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >").append("\r\n")
                .append(blank9 + blank7).append(blank2).append("操作").append("\r\n")
                .append(blank9)
                .append(blank7)
                .append("</td>")
                .append("\r\n");

        NamedNodeMap[] items = XmlUtils.parseItems();
        String namespace = XmlUtils.getNodeValue(items[0], "namespace");
        try {
            HashMap<String, String> replace = new HashMap<>();
            replace.put("#NAMESPACE#", namespace.concat(""));
            //replace.put("#DONEW#", namespace.concat(""));
            replace.put("#QUERYCONDITION#", queryCondition.toString());
            replace.put("#GRIDHEAD#", queryResultTitle.toString());
            replace.put("#GRIDDATA#", queryResultData.toString());
            //replace.put("#REQUSTPARAMETERS#", requstString.toString().concat("requestMethod=query"));
            //replace.put("#DELETEPARAMETERS#", "requestMethod=delete");
            //replace.put("#UPDATESTR#", updateStr.endsWith("&") ? updateStr.substring(0, updateStr.length() - 1) : updateStr);
            scripts.put("#GRIDDATA#", queryResultData.toString());
            scripts.put("#NAMESPACE#", namespace.concat(""));
            scripts.put("#REQUSTPARAMETERS#", requstString.toString().concat("requestMethod=query"));
            scripts.put("#DELETEPARAMETERS#", "requestMethod=delete");
            scripts.put("#UPDATESTR#", updateStr.endsWith("&") ? updateStr.substring(0, updateStr.length() - 1) : updateStr);
            generateCode("template/main.html.template", replace, "main.html", namespace);
            // return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return false;
    }


    public static void generateMainHtml(String namespace, NamedNodeMap[] args) throws Exception {
        /*查询条件*/
        StringBuilder queryCondition = new StringBuilder("<!-- // TODO Auto-generated  -->").append("\r\n");

        StringBuilder queryResultTitle = new StringBuilder("<!-- // TODO Auto-generated  -->").append("\r\n");
        //查询结果数据
        StringBuilder queryResultData = new StringBuilder(" //TODO Auto-generated ").append("\r\n");

        StringBuilder initDm = new StringBuilder(" //TODO Auto-generated ").append("\r\n");
        String blank7 = "						";
        String blank2 = "	";
        String blank9 = blank7 + blank2;
        /*更新记录用的，主键字符串*/
        String updateStr = "";
        /*生成查询功能html*/
        StringBuilder requstString = new StringBuilder();
        /*遍历config.xml中的item*/
        int k = 0;
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id").toLowerCase();
            String lable = XmlUtils.getNodeValue(map, "lable").toLowerCase();
            String type = XmlUtils.getNodeValue(map, "type").toLowerCase();
            String length = XmlUtils.getNodeValue(map, "length").toLowerCase();
            String default_ = XmlUtils.getNodeValue(map, "default");
            // String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
            String options = XmlUtils.getNodeValue(map, "options");
            if (options != null && options.startsWith("#") && options.endsWith("#"))
                initDm.append("initDM(document.getElementById(\"" + id + "\"),\"DM#" + id + "\");\r\n");

            /*记录更新关键字，用于修改、删除等操作*/
            updateStr += id + "='+data[i]." + id + "+" + "'&";
            /*生成javascript脚本*/
            requstString.append(id).append("='+document.getElementById('").append(id).append("').value+").append("'&");
            /*查询字段，默认为文本输入类型*/
            String fieldStr;

            if (type.equalsIgnoreCase("date")) {
                /*处理日期类型*/
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                /*处理数字类型*/
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
            } else if (type.equalsIgnoreCase("select")) {
                /*处理下拉列表*/
                fieldStr = buildSelectHtml(id, length, default_, options);
            } else {
                fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\">";
            }
            /*生成一个查询组件： <tr><td>组件</td></tr>*/
            queryCondition.append(blank7).append("<tr class=\"repCnd\">").append("\r\n")
                    .append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n")
                    .append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n")
                    .append(blank9).append(blank2).append(fieldStr).append("\r\n")
                    .append(blank9).append("</td>").append("\r\n")
                    .append(blank7).append("</tr>").append("\r\n");
            /*生成查询结果表的数据部分表头*/
            queryResultTitle.append(blank9).append(blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >")
                    .append("\r\n")
                    .append(blank9).append(blank7).append(blank2).append(lable)
                    .append("\r\n")
                    .append(blank9 + blank7).append("</td>").append("\r\n");
            /*生成查询结果数据，灌装数据*/

            queryResultData.append(blank7).append("trHtml +=\"<td class='editGrid'  align='left'>").append("\"")
                    .append("+data[i]." + id + "+")
                    .append("\"</td>\"").append(";\r\n");

            /*最后一个元素，添加操作代码*/
            if (args.length - 1 == k++) {
                /*去掉最后一个& */
                if (updateStr.endsWith("&")) {
                    updateStr = updateStr.substring(0, updateStr.length() - 1);
                }
                queryResultData.append(blank7).append("let jabUpdateStr='").append(updateStr).append("';\r\n")
                        .append(blank7).append("trHtml +=\"<td class='editGrid'  align='left'>")
                        .append("<a href=\\\\\"javascript:jabEdit('\"+jabUpdateStr+\"')\\\\\">修改</a> &nbsp;&nbsp;  <a href=\\\\\"javascript:jabDel('\"+jabUpdateStr+\"')\\\\\">删除</a> ")
                        .append("</td>\"").append(";\r\n");
            }

        }
        /*生成查询结果表的操作部分表头*/
        queryResultTitle.append(blank9 + blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >").append("\r\n")
                .append(blank9 + blank7).append(blank2).append("操作").append("\r\n")
                .append(blank9)
                .append(blank7)
                .append("</td>")
                .append("\r\n");

        try {
            HashMap<String, String> replace = new HashMap<>();
            replace.put("#NAMESPACE#", namespace.concat(""));
            //replace.put("#DONEW#", namespace.concat(""));
            replace.put("#QUERYCONDITION#", queryCondition.toString());
            replace.put("#GRIDHEAD#", queryResultTitle.toString());
            replace.put("#GRIDDATA#", queryResultData.toString());
            //replace.put("#REQUSTPARAMETERS#", requstString.toString().concat("requestMethod=query"));
            //replace.put("#DELETEPARAMETERS#", "requestMethod=delete");
            //replace.put("#UPDATESTR#", updateStr.endsWith("&") ? updateStr.substring(0, updateStr.length() - 1) : updateStr);
            scripts.put("#INITDM#", initDm.toString());
            scripts.put("#GRIDDATA#", queryResultData.toString());
            scripts.put("#NAMESPACE#", namespace.concat(""));
            scripts.put("#REQUSTPARAMETERS#", requstString.toString().concat("requestMethod=query"));
            scripts.put("#DELETEPARAMETERS#", "requestMethod=delete");
            scripts.put("#UPDATESTR#", updateStr.endsWith("&") ? updateStr.substring(0, updateStr.length() - 1) : updateStr);
            generateCode("template/main.html.template", replace, "main.html", namespace);
            // return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return false;
    }

    private static String buildSelectHtml(String id, String length, String default_, String options) {
        StringBuilder html = new StringBuilder();

        if (options != null && options.startsWith("#") && options.endsWith("#")) {
            html.append("<select  id=\"").append(id).append("\"    name=\"").append(id).append("\"  value=\"").append(default_).append("\"  style=\"width:").append(Integer.parseInt(length)).append("px\" > \r\t");
        } else {
            html.append("<select  id=\"").append(id).append("\"    name=\"").append(id).append("\"  value=\"").append(default_).append("\"  style=\"width:").append(Integer.parseInt(length)).append("px\" > \r\t");
            assert options != null;
            String[] optons = options.split("\\|");
            html.append("<option value=\"\"></option> \r\t");
            for (String s : optons) {
                html.append("<option value=\"").append(s).append("\">").append(s).append("</option> \r\t");
            }

        }
        html.append("</select>");
        return html.toString();
    }

    private static void generateCode(String templateFile, HashMap<String, String> replace, String newName, String namespace) throws ParserConfigurationException, IOException, SAXException {
        InputStream is = getResourceAsStream(templateFile);
        String template = IOUtils.toString(is);
        for (String key : replace.keySet()) {
//            System.out.printf("key:%s  values:%s \r\n ", key,replace.get(key));
            template = template.replaceAll(key, replace.get(key));
        }
        NamedNodeMap[] args = XmlUtils.parseConfigXml("file");
        String saveDir = XmlUtils.getNodeValue(args[0], "saveDir");
        String overwrite = XmlUtils.getNodeValue(args[0], "overwrite");
//        NamedNodeMap[] items = XmlUtils.parseItems();
//        String namespace = XmlUtils.getNodeValue(items[0], "namespace");

        String fileDir = "";
        saveDir = saveDir.concat(namespace);
        if (File.separator.equals("\\")) {
            fileDir += saveDir.replaceAll("/", "\\\\");
        } else {
            fileDir += saveDir.replaceAll("\\\\", "/");
        }
        File dir = new File(fileDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                log.error("Creates the directory failed. " + dir.getAbsolutePath());
            }
        }
        String file = fileDir + File.separator + newName;
        File f = new File(file);
        if (overwrite.equalsIgnoreCase("true")) {
            if (!f.delete() && !f.createNewFile()) {
                log.error("Atomically creates the file failed. " + f.getAbsolutePath());
                return;
            }
        }
        writeFile(f, template, "UTF-8");
        log.info("generate " + f.getAbsolutePath() + " success.");
    }


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
    }
}