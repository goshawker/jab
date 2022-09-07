package com.jab.util;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;


// TODO: Auto-generated Javadoc

/**
 * @author mike
 * @version 1.0
 * @description: TODO
 * @date 2022/9/7 14:29
 */
public class ParameterUtils {

  /**
   * Gets the attribute.
   *
   * @param request    the request
   * @param attribName the attrib name
   * @return the attribute
   */
  public static String getAttribute(HttpServletRequest request,
                                    String attribName) {
    String temp = (String) request.getAttribute(attribName);
    if ((temp != null) && (!temp.equals(""))) {
      return temp;
    }
    return null;
  }

  /**
   * Gets the boolean attribute.
   *
   * @param request    the request
   * @param attribName the attrib name
   * @return the boolean attribute
   */
  public static boolean getBooleanAttribute(HttpServletRequest request,
                                            String attribName) {
    String temp = (String) request.getAttribute(attribName);

    return (temp != null) && (temp.equals("true"));
  }

  /**
   * Gets the boolean parameter.
   *
   * @param request   the request
   * @param paramName the param name
   * @return the boolean parameter
   */
  public static boolean getBooleanParameter(HttpServletRequest request,
                                            String paramName) {
    String temp = request.getParameter(paramName);
    return (temp != null) && (temp.equals("true"));
  }

  /**
   * Gets the checkbox parameter.
   *
   * @param request   the request
   * @param paramName the param name
   * @return the checkbox parameter
   */
  public static boolean getCheckboxParameter(HttpServletRequest request,
                                             String paramName) {
    String temp = request.getParameter(paramName);
    return (temp != null) && (temp.equals("on"));
  }

  /**
   * Gets the date parameter.
   *
   * @param request   the request
   * @param paramName the param name
   * @return the date parameter
   */
  public static Date getDateParameter(HttpServletRequest request,
                                      String paramName) {
    String temp = request.getParameter(paramName);
    if ((temp != null) && (!temp.equals(""))) {
      Date date = null;
      try {
        date = Date.valueOf(temp);
      } catch (Exception ignored) {
      }
      return date;
    }
    return null;
  }

  /**
   * Gets the double parameter.
   *
   * @param request    the request
   * @param paramName  the param name
   * @param defaultNum the default num
   * @return the double parameter
   */
  public static double getDoubleParameter(HttpServletRequest request,
                                          String paramName, double defaultNum) {
    String temp = request.getParameter(paramName);
    if ((temp != null) && (!temp.equals(""))) {
      double num = defaultNum;
      try {
        num = Double.parseDouble(temp);
      } catch (Exception ignored) {
      }
      return num;
    }
    return defaultNum;
  }

  /**
   * Gets the int attribute.
   *
   * @param request    the request
   * @param attribName the attrib name
   * @param defaultNum the default num
   * @return the int attribute
   */
  public static int getIntAttribute(HttpServletRequest request,
                                    String attribName, int defaultNum) {
    String temp = (String) request.getAttribute(attribName);
    if ((temp != null) && (!temp.equals(""))) {
      int num = defaultNum;
      try {
        num = Integer.parseInt(temp);
      } catch (Exception ignored) {
      }
      return num;
    }
    return defaultNum;
  }

  /**
   * Gets the int parameter.
   *
   * @param request    the request
   * @param paramName  the param name
   * @param defaultNum the default num
   * @return the int parameter
   */
  public static int getIntParameter(HttpServletRequest request,
                                    String paramName, int defaultNum) {
    String temp = request.getParameter(paramName);
    if ((temp != null) && (!temp.equals(""))) {
      int num = defaultNum;
      try {
        num = Integer.parseInt(temp);
      } catch (Exception ignored) {
      }
      return num;
    }
    return defaultNum;
  }

  /**
   * Gets the parameter.
   *
   * @param request        the request
   * @param paramName      the param name
   * @param emptyStringsOK the empty strings ok
   * @return the parameter
   */
  public static String getParameter(HttpServletRequest request,
                                    String paramName, boolean emptyStringsOK) {
    String temp = request.getParameter(paramName);
    if (emptyStringsOK) {
      if (temp != null) {
        return temp;
      }
      return null;
    }

    return getParameter(request, paramName);
  }

  /**
   * Gets the parameter.
   *
   * @param request   the request
   * @param paramName the param name
   * @return the parameter
   */
  public static String getParameter(HttpServletRequest request,
                                    String paramName) {
    String temp = request.getParameter(paramName);
    if ((temp != null) && (!temp.equals(""))) {
      return temp;
    }
    return null;
  }

  /**
   * Gets the value by key.
   *
   * @param strParameter the str parameter
   * @param strKey       the str key
   * @return the value by key
   */
  public static String getValueByKey(String strParameter, String strKey) {
    String strValue = null;
    int nPos = StringUtils.pos(strParameter, "?");
    strParameter = StringUtils.mid(strParameter, nPos + 1);

    String[] strSplit = strParameter.split("@");

    for (int i = 0; i < strSplit.length; i++) {
      int n = StringUtils.pos(strSplit[i], "=");
      if (StringUtils.left(strSplit[i], n - 1).trim()
              .equals(strKey.trim())) {
        strValue = StringUtils.mid(strSplit[i], n + 1).trim();
        break;
      }

    }

    return strValue;
  }
}