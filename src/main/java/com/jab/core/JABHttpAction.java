/*
 *
 */
package com.jab.core;

import com.jab.util.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:06
* @Version: 1.0
*/
public abstract class JABHttpAction extends HttpAction {

  /**
   * The Constant DOMAIN_PATH.
   */
  public static final String $PATH_DOMAIN = System.getProperty("user.dir") + File.separator;
  /**
   * The Constant ATTACH_PATH.
   */
  public static final String $PATH_ATTACH = $PATH_DOMAIN + "Attachs";
  /**
   * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
   */


  private static final long serialVersionUID = -8727634387657108848L;

  /**
   * Instantiates a new iframe action.
   */
  public JABHttpAction() {
    log.debug("Action init finshed!");
  }

  /**
   * Perform方法，已经初始化SqlSession,无异常自动提交，有异常执行回滚操作..
   *
   * @return the string
   */
  protected abstract String perform();

  /**
   * Validators方法，已经初始化SqlSession,无异常自动提交，有异常执行回滚操作..
   */
  protected abstract void validators();

  /**
   * Initial方法，已经初始化SqlSession,无异常自动提交，有异常执行回滚操作.
   *
   * @return the string
   */
  protected abstract String initial();

  /* (非 Javadoc)
   * <p>Title: execute，子类实现perform方法即可</p>
   * <p>Description: </p>
   * @return
   * @throws Exception
   * @see com.opensymphony.xwork2.ActionSupport#execute()
   */
  @Override
  public final String execute() throws Exception {
    // TODO Auto-generated method stub
    return $private();
  }

  /**
   * __private.
   *
   * @return the string
   */
  private String $private() {
    long start = System.currentTimeMillis();
    String returnStr = SUCCESS;
    sqlsession = buildDBSession();
    try {
      returnStr = perform();
      sqlsession.commit();
    } catch (Exception e) {
      sqlsession.rollback();
      e.printStackTrace();
    } finally {
      sqlsession.close();
    }
    log.info("<Call " + this.getClass().getName() + " perform Completed,Measured milliseconds :" + (System.currentTimeMillis() - start) + ">");
    return returnStr;
  }



  /**
   * 框架内置方法 init(), 子类实现initial()即可.
   *
   * @return the string
   */
  public final String init0() {
    // TODO Auto-generated method stub
    return O00000000000OOOOO();
  }

  /**
   * @description: TODO
   * @author mike
   * @date 2022/9/7 18:56
   * @version 1.0
   */
  private String O00000000000OOOOO() {
    long start = System.currentTimeMillis();
    String returnStr = SUCCESS;
    sqlsession = buildDBSession();
    try {
      returnStr = initial();
      sqlsession.commit();
    } finally {
      sqlsession.rollback();
      sqlsession.close();
    }
    log.debug("<Call initial Completed,Measured milliseconds :" + (System.currentTimeMillis() - start) + ">");
    return returnStr;
  }

  protected boolean deleteAttach(String attach_uri) {
    if (attach_uri != null && !attach_uri.equals("")) {
      try {
        File f = new File(attach_uri);
        FileUtils.forceDelete(f);
        f = null;
        return true;
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }

  /**
   * 重命名并保存附件到domain/Attachs目录下，成功返回新文件文件名，失败返回null.
   *
   * @param attach the attach
   * @return the string
   */
  protected String saveAttach(File attach) {
    if (!attach.exists()) {
      return null;
    }
    String newfilename = StringUtils.randomString();
    File savefile = new File($PATH_ATTACH, newfilename);
    if (!savefile.getParentFile().exists()) {
      savefile.getParentFile().mkdirs();
    }
    try {
      FileUtils.copyFile(attach, savefile);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    return newfilename;
  }
}
