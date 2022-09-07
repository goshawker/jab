package com.jab.util;

import com.jab.core.VOProject;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

// TODO: Auto-generated Javadoc

/**
 * @author mike
 * @version 1.0
 * @description: TODO
 * @date 2022/9/7 14:18
 */
public class StringUtils {

  /**
   * Adds the quotes.
   *
   * @param str the str
   * @return the string
   */
  public static String addQuotes(String str) {
    StringBuffer strQuotes = new StringBuffer("'");

    if ((str == null) || (str == "")) {
      strQuotes = new StringBuffer("'");
    } else {
      for (int i = 1; i <= str.length(); i++) {
        if (mid(str, i, 1).equals("'")) {
          strQuotes.append("''");
        } else {
          strQuotes.append(mid(str, i, 1));
        }
      }
    }

    return "'";
  }

  /**
   * Checks if is empty or null.
   *
   * @param str the str
   * @return true, if is empty or null
   */
  public static boolean isEmptyOrNull(String str) {
    if (str == null) {
      return true;
    }

    return str.length() == 0;
  }


  /**
   * _is empty or null.
   *
   * @param obj the obj
   * @return true, if successful
   */
  public static boolean isEmptyOrNull(Object obj) {
    if (obj == null) {
      return true;
    }
    Class __c1 = obj.getClass(); // 得到运行时对象
    if (__c1 == String.class)
      return ((String) obj).length() == 0; // 如果c1是String类型,直接转换
    if (__c1.isArray()) { // 如果是数组类型
      return Array.getLength(obj) == 0;
    } else if (obj instanceof Map) {
      Map __map = (Map) obj;
      return __map.isEmpty();

    }
    return false;
  }

  /**
   * Join.
   *
   * @param it        the it
   * @param delimiter the delimiter
   * @return the string
   */
  public static String join(Iterator it, String delimiter) {
    if (it == null) {
      return null;
    }

    if (delimiter == null) {
      delimiter = "";
    }
    StringBuffer buf = new StringBuffer();
    while (it.hasNext()) {
      buf.append(it.next().toString() + delimiter);
    }
    return buf.toString();
  }

  /**
   * Join.
   *
   * @param strs      the strs
   * @param delimiter the delimiter
   * @return the string
   */
  public static String join(String[] strs, String delimiter) {
    if (strs == null) {
      return null;
    }

    if (delimiter == null) {
      delimiter = "";
    }
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < strs.length; i++) {
      buf.append(strs[i] + delimiter);
    }
    return buf.toString();
  }

  /**
   * Left.
   *
   * @param str     the str
   * @param nLength the n length
   * @return the string
   */
  public static String left(String str, int nLength) {
    if (nLength <= 0)
      return "";
    if (nLength >= str.length()) {
      return str;
    }
    return str.substring(0, nLength);
  }

  /**
   * Mid.
   *
   * @param str     the str
   * @param nFrom   the n from
   * @param nLength the n length
   * @return the string
   */
  public static String mid(String str, int nFrom, int nLength) {
    if ((nFrom <= 0) || (nFrom > str.length()) || (nFrom + nLength <= 0)
            || (nFrom + nLength - 1 > str.length())) {
      return "";
    }
    return str.substring(nFrom - 1, nFrom + nLength - 1);
  }

  /**
   * Mid.
   *
   * @param str   the str
   * @param nFrom the n from
   * @return the string
   */
  public static String mid(String str, int nFrom) {
    if ((nFrom <= 0) || (nFrom > str.length())) {
      return "";
    }
    return str.substring(nFrom - 1);
  }

  /**
   * New line.
   *
   * @return the string
   */
  public static String _newLine() {
    return "\r\n";
  }

  /**
   * Pos.
   *
   * @param str    the str
   * @param strSub the str sub
   * @param nFrom  the n from
   * @return the int
   */
  public static int pos(String str, String strSub, int nFrom) {
    if (nFrom <= 0) {
      return 0;
    }
    return str.indexOf(strSub, nFrom - 1) + 1;
  }

  /**
   * Pos.
   *
   * @param str    the str
   * @param strSub the str sub
   * @return the int
   */
  public static int pos(String str, String strSub) {
    return str.indexOf(strSub) + 1;
  }

  /**
   * Repeat.
   *
   * @param token the token
   * @param count the count
   * @return the string
   */
  public static String repeat(String token, int count) {
    if ((token == null) || (count < 1)) {
      return token;
    }

    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < count; i++) {
      buf.append(token);
    }
    return buf.toString();
  }

  /**
   * Replace first.
   *
   * @param str  the str
   * @param str1 the str1
   * @param str2 the str2
   * @return the string
   */
  public static String replaceFirst(String str, String str1, String str2) {
    String strResult = str;
    StringBuffer strReturn = new StringBuffer();

    if ((str == null) || (str1 == null) || (str2 == null)) {
      strReturn = strReturn.append(str);
    } else if (pos(str, str1) > 0) {
      int i = pos(strResult, str1);

      while (i > 0) {
        strReturn = strReturn.append(left(strResult, i - 1)).append(
                str2);
        strResult = mid(strResult, i + str1.length());
        i = 0;
      }
      strReturn = strReturn.append(strResult);
    } else {
      strReturn = strReturn = strReturn.append(str);
    }

    return strReturn.toString();
  }

  /**
   * Replace sub string.
   *
   * @param str  the str
   * @param str1 the str1
   * @param str2 the str2
   * @return the string
   */
  public static String replaceSubString(String str, String str1, String str2) {
    String strResult = str;
    StringBuffer strReturn = new StringBuffer();

    if ((str == null) || (str1 == null) || (str2 == null)) {
      strReturn = strReturn.append(str);
    } else if (pos(str, str1) > 0) {
      int i = pos(strResult, str1);

      while (i > 0) {
        strReturn = strReturn.append(left(strResult, i - 1)).append(
                str2);
        strResult = mid(strResult, i + str1.length());
        i = pos(strResult, str1);
      }
      strReturn = strReturn.append(strResult);
    } else {
      strReturn = strReturn = strReturn.append(str);
    }

    return strReturn.toString();
  }

  /**
   * Right.
   *
   * @param str     the str
   * @param nLength the n length
   * @return the string
   */
  public static String right(String str, int nLength) {
    if (nLength <= 0)
      return "";
    if (nLength >= str.length()) {
      return str;
    }
    return str.substring(str.length() - nLength);
  }

  /**
   * To non null.
   *
   * @param src the src
   * @return the string
   */
  public static String toNonNull(String src) {
    return src == null ? "" : src;
  }

  /**
   * _to non null.
   *
   * @param src the src
   * @return the string
   */
  public static String toNonNull(Object src) {
    return src == null ? "" : src.toString();
  }

  /**
   * Trim string.
   *
   * @param strValue the str value
   * @return the string
   */
  public static String trimString(String strValue) {
    String str = "";

    if (strValue != null) {
      str = strValue.trim();
    }

    return str;
  }

  /**
   * _random string.
   *
   * @return the string
   */
  public static String randomString() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * _autocomplete.
   *
   * @param src    the src
   * @param length the length
   * @param arg    the arg
   * @return the string
   */
  public static String autocomplete(String src, int length, String arg) {
    int l = src.length();
    while (l < length) {
      src = arg.concat(src);
      l = src.length();
    }
    return src;
  }

  /**
   * _to string.
   *
   * @param obj the obj
   * @return the string
   */
  public static String toString(Object obj) {
    if (obj == null)
      return null; // 如果obj为空..返回null
    Class __c1 = obj.getClass(); // 得到运行时对象
    if (__c1 == String.class)
      return (String) obj; // 如果c1是String类型,直接转换
    if (__c1.isArray()) { // 如果是数组类型
      String r = __c1.getComponentType() + "Array{"; // 得到该数组组件类型的Class
      for (int i = 0; i < Array.getLength(obj); i++) {
        if (i > 0)
          r += ",";
        Object var = Array.get(obj, i); // 取得obj[i]元素的值
        if (__c1.getComponentType().isPrimitive()) // 如果为基本类型
          r += var;
        else
          r += toString(var); // 递归调用
      }
      return r + "}";
    } else if (obj instanceof Map) {
      Map __map = (Map) obj;
      Iterator iter = __map.keySet().iterator();
      StringBuffer r = new StringBuffer("HashMap{"); // 得到obj名称
      while (iter.hasNext()) {
        Object key = iter.next();
        Object value = __map.get(key);
        r.append(key.toString()).append(":")
                .append(value.toString()).append(";");

      }
      r.append("}");
      __map = null;
      return r.toString();

    }
    StringBuffer r = new StringBuffer(__c1.getName()); // 得到obj名称

    if (obj instanceof VOProject) {
      // 检查本类域和所有超类域
      do {
        r.append(obj + "[");
        Field[] fields = __c1.getDeclaredFields(); // 得到本类和超类所有域
        AccessibleObject.setAccessible(fields, true); // 将所有域设置为可访问的.
        for (Field field : fields) {
          if (!Modifier.isStatic(field.getModifiers())) {
            if (!r.toString().endsWith("["))
              r.append(",");
            r.append(field.getName()).append("=");
            try {
              Class type = field.getType(); // 得到字段类型
              Object var = field.get(obj); // 设置值
              if (type.isPrimitive()) // 判断是否为基本类型
                r.append(var);
              else
                r.append(toString(var));
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        r.append("]");
        __c1 = __c1.getSuperclass();
      } while (__c1 != null);
    }
    return r.toString();
  }

  /**
   * _format.
   *
   * @param number  the number
   * @param pattern the pattern
   * @return the string
   */
  public static String format(String number, String pattern) {
    double d = new Double(number).doubleValue();
    return format(d, pattern);
  }

  /**
   * _format.
   *
   * @param number  the number
   * @param pattern the pattern
   * @return the string
   */
  public static String format(double number, String pattern) {
    DecimalFormat df = new DecimalFormat(pattern);
    return df.format(number);
  }

  /**
   * _double to chinese.
   *
   * @param sumofcash the sumofcash
   * @return the string
   */
  public static String doubleToChinese(double sumofcash) {
    String[] arr = {"分", "角", "元", "拾", "百", "千", "万", "拾万", "百万", "千万", "亿", "拾亿", "百亿", "千亿"};

    String sTmp = "";
    String sMoney = "";
    String header = "";

    if (sumofcash < 0.0D)
      header = "负";
    sumofcash = Math.abs(sumofcash);
    double dTmp = sumofcash * 100.0D;
    sMoney = format(dTmp, "0,000.#");
    StringTokenizer st = new StringTokenizer(sMoney, ",");
    String tmp = "";
    while (st.hasMoreElements()) {
      tmp = tmp + st.nextToken();
    }
    sMoney = tmp;
    int iLen = sMoney.length();
    int count = 0;
    for (int i = 0; i < iLen; i++) {
      String sTemp = sMoney.substring(i, i + 1);
      if (sTemp.equals("0")) {
        count++;
        if (count == 1) {
          sTmp = sTmp + trans(sTemp);
        }
      } else {
        sTmp = sTmp + trans(sTemp);
        sTmp = sTmp + arr[(iLen - i - 1)];
        count = 0;
      }
    }

    sTmp = sTmp.trim();
    iLen = sTmp.length();
    if (sTmp.charAt(iLen - 1) == '零') {
      sTmp = sTmp.substring(0, iLen - 1);
    }
    sTmp = sTmp + "整";
    sTmp = header.concat(sTmp);
    return sTmp;
  }

  /**
   * _trans.
   *
   * @param args the args
   * @return the string
   */
  private static String trans(String args) {
    int iTemp = Integer.parseInt(args);
    String sRes = "";
    switch (iTemp) {
      case 1:
        sRes = "壹";
        break;
      case 2:
        sRes = "贰";
        break;
      case 3:
        sRes = "叁";
        break;
      case 4:
        sRes = "肆";
        break;
      case 5:
        sRes = "伍";
        break;
      case 6:
        sRes = "陆";
        break;
      case 7:
        sRes = "柒";
        break;
      case 8:
        sRes = "捌";
        break;
      case 9:
        sRes = "玖";
        break;
      case 0:
        sRes = "零";
    }
    return sRes;
  }

}