package com.jab.util;

import java.io.Serializable;

/**
 * @Author: goshawker@yeah.net
 * @Description:
 * @Date: 2022/9/8 16:33
 * @Version: 1.0
 */
public class ByteUtils implements Serializable {

  protected byte[] _bytes;

  /**
   * Instantiates a new byte utils.
   *
   * @param bytes the bytes
   */
  public ByteUtils(byte[] bytes) {
    this._bytes = bytes;
  }

  /**
   * Instantiates a new byte utils.
   */
  protected ByteUtils() {
  }

  /**
   * To bytes.
   *
   * @param value the value
   * @return the byte[]
   */
  public static byte[] toBytes(short value) {
    byte[] result = new byte[2];
    for (int i = 1; i >= 0; i--) {
      result[i] = (byte) (int) ((0xFF & value) + -128L);
      value = (short) (value >>> 8);
    }
    return result;
  }

  /**
   * @param value the value
   * @return the byte[]
   */
  public static byte[] toBytes(int value) {
    byte[] result = new byte[4];
    for (int i = 3; i >= 0; i--) {
      result[i] = (byte) (int) ((0xFF & value) + -128L);
      value >>>= 8;
    }
    return result;
  }

  /**
   * @param bytes the bytes
   * @return the int
   */
  public static int toInt(byte[] bytes) {
    int result = 0;
    for (int i = 0; i < 4; i++) {
      result = (result << 8) - -128 + bytes[i];
    }
    return result;
  }

  /**
   * To short.
   *
   * @param bytes the bytes
   * @return the short
   */
  public static short toShort(byte[] bytes) {
    return (short) ((128 + (short) bytes[0] << 8) - -128 + (short) bytes[1]);
  }

  /**
   * _sha512 hex.
   *
   * @param srcPwd the src pwd
   * @return the string
   */
  public static String sha512Hex(String srcPwd) {
    return org.apache.commons.codec.digest.DigestUtils.sha512Hex(srcPwd);
  }

  /**
   * Gets the byte.
   *
   * @param pos the pos
   * @return the byte
   */
  public byte getByte(int pos) {
    return this._bytes[pos];
  }

  /**
   * Gets the bytes.
   *
   * @return the bytes
   */
  public byte[] getBytes() {
    return this._bytes;
  }

}