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

    public static boolean generateAction() throws Exception {
        StringBuffer valueObjectName = new StringBuffer();
        String blank7 = "	    ";
        String blank9 = "	      ";

        StringBuffer queryCode = new StringBuffer("/** // TODO Auto-generated  */").append("\r\n");
        StringBuffer insertCode = new StringBuffer("/** // TODO Auto-generated  */").append("\r\n");
        StringBuffer updateCode = new StringBuffer("/** // TODO Auto-generated  */").append("\r\n");
        StringBuffer deleteCode = new StringBuffer("/** // TODO Auto-generated  */").append("\r\n");

        NamedNodeMap[] items = XmlUtils.parseItems();
        String namespace = XmlUtils.getNodeValue(items[0], "namespace");
        String tableName = XmlUtils.getNodeValue(items[0], "tableName");

        StringBuffer QuerySQL = new StringBuffer("select ");
        StringBuffer DeleteSQL = new StringBuffer("delete from ").append(tableName);
        StringBuffer UpdateSQL = new StringBuffer("update ").append(tableName).append(" set ");
        StringBuffer InsertSQL = new StringBuffer("insert into ").append(tableName).append("(");

        StringBuffer whereCondition = new StringBuffer(" where 1=1 ");
        StringBuffer whereCondition_query = new StringBuffer(" where 1=1 ");
        StringBuffer values_insert = new StringBuffer();

        NamedNodeMap[] args = XmlUtils.parseItem();
        Vector<String> keys_columns = new Vector();
        Vector<String> nokeys_columns = new Vector();
        Vector<String> all_columns = new Vector();
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id", true).toLowerCase();
            String lable = XmlUtils.getNodeValue(map, "lable", true).toLowerCase();
            String type = XmlUtils.getNodeValue(map, "type", true).toLowerCase();
            String length = XmlUtils.getNodeValue(map, "length", true).toLowerCase();
            String default_ = XmlUtils.getNodeValue(map, "default", true);
            String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
            String options = XmlUtils.getNodeValue(map, "options", true);
            InsertSQL.append(id).append(",");
            QuerySQL.append(id).append(",");
            all_columns.add(id);
            // whereCondition_query.append(" and ").append(id).append("=? ");
            values_insert.append("?,");
            if (!StringUtils.isEmptyOrNull(primarykey)) {
                keys_columns.add(id);
                deleteCode.append(blank7).append("String ").append(id).append(" = request.getParameter(\"").append(id).append("\"); \r\n");
                whereCondition.append(" and ").append(id).append("=? ");
            } else {
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
            String tmp = values_insert.toString().substring(0, values_insert.length() - 1);
            values_insert.setLength(0);
            values_insert.append(tmp).append(")");
        }

        //处理列拼接，多余部分
        if (InsertSQL.toString().trim().endsWith(",")) {
            String tmp = InsertSQL.toString().substring(0, InsertSQL.length() - 1);
            InsertSQL.setLength(0);
            InsertSQL.append(tmp).append(") values(");
        }


        //处理列拼接，多余部分
        if (QuerySQL.toString().trim().endsWith(",")) {
            String tmp = QuerySQL.toString().substring(0, QuerySQL.length() - 1);
            QuerySQL.setLength(0);
            QuerySQL.append(tmp);
        }
        QuerySQL.append(" from ").append(tableName);

        //插入SQL
        insertCode.append(blank7).append("java.sql.PreparedStatement ps = buildConnect().prepareStatement(\"").append(InsertSQL).append(values_insert).append("\");\r\n");
        //删除SQL
        deleteCode.append(blank7).append("java.sql.PreparedStatement ps =  buildConnect().prepareStatement(\"").append(DeleteSQL).append(whereCondition).append("\");\r\n");
        //更新SQL
        updateCode.append(blank7).append("java.sql.PreparedStatement ps =  buildConnect().prepareStatement(\"").append(UpdateSQL).append(whereCondition).append(" \");\r\n");
        //查询SQL
        queryCode.append(blank7).append("java.sql.PreparedStatement ps =  null;\r\n");


        if (QuerySQL.length() > 0) {
            whereCondition_query.setLength(0);
            queryCode.append(blank7).append("java.util.Vector<String> lst_value = new java.util.Vector();").append("\r\n");
            queryCode.append(blank7).append("java.util.Vector<String> lst_key = new java.util.Vector();").append("\r\n");
            int i = 0;
            for (String id : all_columns) {
                i++;
                //queryCode.append(blank7).append("ps.setString(").append(i).append(",").append(id).append(");").append("\r\n");
                //处理查询条件

                queryCode.append(blank7).append("if(").append(id).append(".length() > 0){").append("\r\n");
                queryCode.append(blank9).append("lst_value.add(").append(id).append("); \r\n");
                queryCode.append(blank9).append("lst_key.add(\"").append(id).append("\"); \r\n");
                //queryCode.append(blank7).append("todoSql += \"").append(id).append("=?,\";").append("\r\n");
                //queryCode.append(blank7).append(blank7).append("ps.setString(").append(i).append(",").append(id).append(");").append("\r\n");
                queryCode.append(blank7).append("}").append("\r\n");
            }
            queryCode.append(blank7).append("for(int i=0;i<lst_key.size();i++){").append("\r\n");
            queryCode.append(blank9).append("todoSql += \" and \" + lst_key.get(i) +\"=?\" ;").append("\r\n");
            // queryCode.append(blank9).append("ps.setString(i+1,lst_value.get(i));").append("\r\n");
            queryCode.append(blank7).append("}").append("\r\n");

            queryCode.append(blank7).append("todoSql =\" ").append(QuerySQL).append(" where 1=1 \" + todoSql; ").append("\r\n");
            queryCode.append(blank7).append("ps =  buildConnect().prepareStatement(todoSql);").append("\r\n");

            queryCode.append(blank7).append("for(int i=0;i<lst_value.size();i++){").append("\r\n");
            // queryCode.append(blank9).append("todoSql += \" and \" + lst_key.get(i) +\"=?\" ;").append("\r\n");
            queryCode.append(blank9).append("ps.setString(i+1,lst_value.get(i));").append("\r\n");
            queryCode.append(blank7).append("}").append("\r\n");
        }


        if (!UpdateSQL.equals("")) {
            int i = 0;
            for (String id : nokeys_columns) {
                i++;
                updateCode.append(blank7).append("ps.setString(").append(i).append(",").append(id).append(");").append("\r\n");
            }
            for (String id : keys_columns) {
                i++;
                updateCode.append(blank7).append("ps.setString(").append(i).append(",").append(id).append(");").append("\r\n");
            }
        }

        if (!DeleteSQL.equals("")) {
            int i = 0;
            for (String id : keys_columns) {
                i++;
                deleteCode.append(blank7).append("ps.setString(").append(i).append(",").append(id).append(");").append("\r\n");
            }
        }

        if (!InsertSQL.equals("")) {
            int i = 0;
            for (String id : all_columns) {
                i++;
                insertCode.append(blank7).append("ps.setString(").append(i).append(",").append(id).append(");").append("\r\n");
            }

        }
        deleteCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        updateCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        insertCode.append(blank7).append("affected_rows = ps.executeUpdate();").append("\r\n");
        queryCode.append(blank7).append("java.sql.ResultSet rs = ps.executeQuery();").append("\r\n");
        queryCode.append(blank7).append("JSONString = toJSONArray(rs).toString();").append("\r\n");


        args = XmlUtils.parseItems();
//    String namespace = XmlUtils.getNodeValue(args[0], "namespace");
//    String tableName = XmlUtils.getNodeValue(args[0], "tableName");
        valueObjectName.append(tableName);
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
            generateCode("template/active.java.template", replace, "JABActive.java");
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    public static boolean generateValueObject() throws Exception {
        StringBuffer valueObjectName = new StringBuffer();
        StringBuffer valueObjectField = new StringBuffer("/** // TODO Auto-generated  */").append("\r\n");
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
            replace.put("#PACKAGE#", namespace.substring(1).replaceAll("/", "."));
            replace.put("#VALUEOBJECTNAME#", valueObjectName.toString());
            replace.put("#VALUEOBJECTFIELD#", valueObjectField.toString());
            generateCode("template/vo.java.template", replace, tableName + ".java");
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
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\"   \"+readOnly+\">";
            } else if (type.equalsIgnoreCase("select")) {
                String html = "";
                html += "<select  id=\"" + id + "\"    name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  style=\"width:" + Integer.parseInt(length) + "px\"   \"+readOnly+\"> \r\t";
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
            generateCode("template/update.html.template", replace, "update.html");
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
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
            } else if (type.equalsIgnoreCase("select")) {
                String html = "";
                html += "<select  id=\"" + id + "\"    name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  style=\"width:" + Integer.parseInt(length) + "px\" > \r\t";
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
            generateCode("template/new.html.template", replace, "new.html");
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static boolean generateMainHtml() throws Exception {
        /*查询条件*/
        StringBuffer queryCondition = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");

        StringBuffer queryResultTitle = new StringBuffer("<!-- // TODO Auto-generated  -->").append("\r\n");
        //查询结果数据
        StringBuffer queryResultData = new StringBuffer(" //TODO Auto-generated ").append("\r\n");
        String blank7 = "						";
        String blank2 = "	";
        String blank9 = blank7 + blank2;
        /*主键*/
        Vector<NamedNodeMap> primarykeys = new Vector<>();
        /* 读取config.xml配置*/
        NamedNodeMap[] args = XmlUtils.parseItem();
        /*更新记录用的，主键字符串*/
        String updatestr = "";
        /*生成查询功能html*/
        String requstparameters = "";
        /*遍历config.xml中的item*/
        int k = 0;
        for (NamedNodeMap map : args) {
            String id = XmlUtils.getNodeValue(map, "id", true).toLowerCase();
            String lable = XmlUtils.getNodeValue(map, "lable", true).toLowerCase();
            String type = XmlUtils.getNodeValue(map, "type", true).toLowerCase();
            String length = XmlUtils.getNodeValue(map, "length", true).toLowerCase();
            String default_ = XmlUtils.getNodeValue(map, "default", true);
            String primarykey = XmlUtils.getNodeValue(map, "primarykey", true).toLowerCase();
            String options = XmlUtils.getNodeValue(map, "options", true);
            /*记录主键*/
            if (!StringUtils.isEmptyOrNull(primarykey) && primarykey.equalsIgnoreCase("true")) {
                primarykeys.add(map);
                /*记录更新关键字，用于修改、删除等操作*/
                updatestr += id + "='+data[i]." + id + "+" + "'&";
            }
            /*生成javascript脚本*/
            requstparameters += id + "='+document.getElementById('" + id + "').value+" + "'&";
            /*查询字段，默认为文本输入类型*/
            String fieldStr = "<input type=\"text\" name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  maxlength=\"" + (Integer.parseInt(length) + 1) + "\"  style=\"width:" + Integer.parseInt(length) + "px\">";

            if (type.equalsIgnoreCase("date")) {
                /*处理日期类型*/
                fieldStr = "<input type=\"date\"  name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  placeholder=\"Only date\"  style=\"width:" + (Integer.parseInt(length) + 5) + "px\" >";
            } else if (type.equalsIgnoreCase("number")) {
                /*处理数字类型*/
                fieldStr = "<input type=\"number\"    id=\"" + id + "\"  name=\"" + id + "\" value=\"" + default_ + "\" placeholder=\"Only numbers\"  min=\"0\" max=\"120\" step=\"1\" style=\"width:" + Integer.parseInt(length) + "px\" >";
            } else if (type.equalsIgnoreCase("select")) {
                /*处理下拉列表*/
                String html = "";
                html += "<select  id=\"" + id + "\"    name=\"" + id + "\" id=\"" + id + "\" value=\"" + default_ + "\"  style=\"width:" + Integer.parseInt(length) + "px\" > \r\t";
                String[] optons_ = options.split("\\|");
                html += "<option value=\"\"></option> \r\t";
                for (int i = 0; i < optons_.length; i++) {
                    html += "<option value=\"" + optons_[i] + "\">" + optons_[i] + "</option> \r\t";
                }
                html += "</select>";
                fieldStr = html;
            }
            /*生成一个查询组件： <tr><td>组件</td></tr>*/
            queryCondition.append(blank7).append("<tr class=\"repCnd\">").append("\r\n");
            queryCondition.append(blank9).append("<td class=\"repCndLb\">").append(lable).append(":</td>").append("\r\n");
            queryCondition.append(blank9).append("<td class=\"repCndEditRight\">").append("\r\n");
            queryCondition.append(blank9).append(blank2).append(fieldStr).append("\r\n");
            queryCondition.append(blank9).append("</td>").append("\r\n");
            queryCondition.append(blank7).append("</tr>").append("\r\n");
            /*生成查询结果表的数据部分表头*/
            queryResultTitle.append(blank9 + blank7).append("<td class=\"editGridHd\" nowrap=\"nowrap\" >").append("\r\n");
            queryResultTitle.append(blank9 + blank7).append(blank2).append(id).append("\r\n");
            queryResultTitle.append(blank9 + blank7).append("</td>").append("\r\n");
            /*生成查询结果数据，灌装数据*/
            String format = "";
            if (type.equalsIgnoreCase("datetime")) {
                format = " format=\"yyyy-MM-dd HH:mm:ss\" ";
            }
            queryResultData.append(blank7).append("trHtml +=\"<td class='editGrid'  align='left'>").append("\"");
            queryResultData.append("+data[i]." + id + "+");
            queryResultData.append("\"</td>\"").append(";\r\n");

            /*最后一个元素，添加操作代码*/
            if(args.length-1 == k++){
                /*去掉最后一个& */
                if(updatestr.endsWith("&")){
                    updatestr = updatestr.substring(0,updatestr.length()-1);
                }
                queryResultData.append(blank7).append("let jabUpdateStr='").append(updatestr).append("';\r\n");;
                queryResultData.append(blank7).append("trHtml +=\"<td class='editGrid'  align='left'>").append("");
                queryResultData.append("<a href=\\\\\"javascript:jabEdit('\"+jabUpdateStr+\"')\\\\\">Edit</a> &nbsp;&nbsp;  <a href=\\\\\"javascript:jabDel('\"+jabUpdateStr+\"')\\\\\">Delete</a> " );
                //queryResultData.append("<a href=\"javascript:jabEdit();\">Edit</a> &nbsp;&nbsp; <a href=\"javascript:jabDel();\">Delete</a>");
                queryResultData.append("</td>\"").append(";\r\n");
            }

        }
        /*生成查询结果表的操作部分表头*/
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
            replace.put("#REQUSTPARAMETERS#", requstparameters.concat("requestMethod=query"));
            replace.put("#UPDATESTR#", updatestr.endsWith("&") ? updatestr.substring(0, updatestr.length() - 1) : updatestr);
            generateCode("template/main.html.template", replace, "main.html");
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private static void generateCode(String templateFile, HashMap<String, String> replace, String newName) throws ParserConfigurationException, IOException, SAXException {
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
            f.delete();
        } else {
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
        //System.out.println("file  " + file + "  created.");
    }
}