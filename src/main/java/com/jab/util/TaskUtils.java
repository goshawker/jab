package com.jab.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:00
* @Version: 1.0
*/
public class TaskUtils {

  /**
   * The __log.
   */
  protected Logger log = LogManager.getLogger(TaskUtils.class);

  /**
   * Run task.
   *
   * @param request  the request
   * @param response the response
   */
  public static void runTask(HttpServletRequest request, HttpServletResponse response) {

  }

  /**
   * @description: TODO
   * @author mike
   * @date 2022/9/7 15:55
   * @version 1.0
   */

  public static boolean endTask(String... args) {
    return true;
  }

}
