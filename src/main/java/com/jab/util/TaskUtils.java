package com.jab.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc

/**
 * @author mike
 * @version 1.0
 * @description: TODO
 * @date 2022/9/7 14:18
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
