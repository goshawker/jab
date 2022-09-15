package com.jab.common;

/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:06
* @Version: 1.0
*/
public class Test {


  public static void main(String[] args){
    String str = "man|woman";
    String[] s = str.split("\\|");
    System.out.printf("============="+s.length);
  }
}
