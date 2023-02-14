package com.jab.util;
import java.text.DecimalFormat;
/**
 * @Description :
 * @Author : goshawker@yeah.net
 * @Date : 2023-02-14 11:45
 */

public class StringUtils {


    /**
     * Checks if is empty or null.
     *
     * @param str the str
     * @return true, if is empty or null
     */
    public static boolean isNotEmptyAndNull(String str) {
        return (str != null) && (str.length() != 0);
    }

    /**
     * _format.
     *
     * @param number  the number
     * @param pattern the pattern
     * @return the string
     */
    public static String format(String number, String pattern) {
        double d = Double.parseDouble(number);
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


}