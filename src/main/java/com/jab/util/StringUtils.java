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
}