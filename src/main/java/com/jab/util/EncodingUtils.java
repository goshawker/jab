package com.jab.util;

import java.io.UnsupportedEncodingException;


// TODO: Auto-generated Javadoc
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 14:18
 * @version 1.0
 */
public class EncodingUtils {

	/**
	 * To g b2312.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public String toGB2312(String src) {
		try {
			return new String(src.getBytes(), "GB2312");
		} catch (UnsupportedEncodingException ex) {
		}
		return null;
	}

	/**
	 * To is o88591.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public String toISO88591(String src) {
		try {
			return new String(src.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException ex) {
		}
		return null;
	}

	/**
	 * To gbk.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public String toGBK(String src) {
		try {
			return new String(src.getBytes(), "GBK");
		} catch (UnsupportedEncodingException ex) {
		}
		return null;
	}

	/**
	 * To ut f8.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public String toUTF8(String src) {
		try {
			return new String(src.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		return null;
	}
}