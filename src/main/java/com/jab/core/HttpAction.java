/*
 * Copyright 2002-2006,2009 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jab.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;



/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:06
* @Version: 1.0
*/
public class HttpAction extends HttpServlet implements Action {
  protected Logger log = LogManager.getLogger(HttpAction.class);
  // 返还前台的消息提示
  String systemMessage = "";
  String pageNo = "1"; // 当前页码
  String pageCount = "1"; // 总页数
  String pageSize = "10"; // 每页显示记录数
  String rowCount = "0"; // 记录总数

  protected HttpServletRequest httpRequest;
  protected HttpServletResponse httpResponse;

  private ArrayList pagenationList = new ArrayList();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doPost(req, resp);
  }
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    httpRequest = req;
    httpResponse = resp;
    try {
      execute();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public String execute() throws Exception {
    return null;
  }
  protected HttpServletRequest getRequest() {
    return this.httpRequest;
  }
  protected HttpServletResponse getResponse() {
    return this.httpResponse;
  }
  protected HttpSession getSession() {
    return this.httpRequest.getSession();
  }

  public ArrayList getPagenationList() {
    return pagenationList;
  }

  public void setPagenationList(ArrayList pagenationList) {
    this.pagenationList = pagenationList;
  }

  public String getPageNo() {
    return pageNo;
  }

  public void setPageNo(String pageNo) {
    if (pageNo == null || "".equals(pageNo)) {
      pageNo = "1";
    }
    this.pageNo = pageNo;
  }

  public String getPageCount() {
    return pageCount;
  }

  public void setPageCount(String pageCount) {
    this.pageCount = pageCount;
  }

  public String getPageSize() {
    return pageSize;
  }

  public void setPageSize(String pageSize) {
    this.pageSize = pageSize;
  }

  public String getRowCount() {
    return rowCount;
  }

  public void setRowCount(String rowCount) {
    this.rowCount = rowCount;
  }
}
