package com.jab.util;



// TODO: Auto-generated Javadoc
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 14:18
 * @version 1.0
 */
public class RegexPattern
{
  
  /** The Constant DATE. */
  public static final String __DATE = "^((19)|(20))[0-9]{2}-([0-9]|(0[0-9])|(1[0-2]))-([0-9]|([0-2][0-9])|([3][0-1]))$";
  
  /** The Constant EMAIL. */
  public static final String __EMAIL = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
  
  /** The Constant MOBILE_NUM. */
  public static final String __MOBILE_NUM = "^(13|013)\\d{9}$";
  
  /** The Constant ZIP_CODE. */
  public static final String __ZIP_CODE = "^[0-9]{6}$";
}