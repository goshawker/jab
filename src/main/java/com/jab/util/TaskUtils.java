package com.jab.util;


import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 14:18
 * @version 1.0
 */
public class TaskUtils {
	
	/** The __log. */
	static Logger __log = Logger.getLogger(TaskUtils.class);
	 
	/**
	 * Run task.
	 *
	 * @param request the request
	 * @param response the response
	 */
	public static void runTask(HttpServletRequest request,HttpServletResponse response) {

	}
	
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 15:55
 * @version 1.0
 */

	public static boolean endTask(String... args)  {
		return true;
	}

}
