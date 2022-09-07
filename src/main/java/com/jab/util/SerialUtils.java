package com.jab.util;

import java.util.HashMap;
import java.util.Map;


// TODO: Auto-generated Javadoc
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 14:18
 * @version 1.0
 */
public class SerialUtils {

	/** The Constant chs. */
	private static final char[] __chs = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z' };

	/** The Constant max. */
	private static final int __max = __chs.length;

	/** The Constant serials. */
	private static final Map __serials = new HashMap();

	/** The Constant sequences. */
	private static final Map __sequences = new HashMap();

	/** The Constant sync_serials. */
	private static final Object __sync_serials = new Object();

	/** The Constant sync_sequences. */
	private static final Object __sync_sequences = new Object();

	/**
	 * Adds the serial.
	 * 
	 * @param name
	 *            the name
	 * @param initStr
	 *            the init str
	 */
	public static void addSerial(String name, String initStr) {
		if (__serials.get(name) != null) {
			return;
		}
		int len = initStr.length();
		int[] initValue = new int[len];
		for (int i = 0; i < len; i++) {
			initValue[i] = 0;
			char c = initStr.charAt(i);
			for (int val = 0; val < __max; val++) {
				if (__chs[val] == c) {
					initValue[i] = val;
				}
			}
		}
		__serials.put(name, new Serial(initValue, __max));
	}

	/**
	 * Adds the sequence.
	 * 
	 * @param name
	 *            the name
	 * @param initValue
	 *            the init value
	 */
	public static void addSequence(String name, Long initValue) {
		if (__sequences.get(name) != null) {
			return;
		}
		__sequences.put(name, initValue);
	}

	/**
	 * Next serial.
	 * 
	 * @param name
	 *            the name
	 * @return the string
	 */
	public static String nextSerial(String name) {
		synchronized (__sync_serials) {
			Serial s = (Serial) __serials.get(name);
			int[] value = s.next();
			char[] ret = new char[value.length];
			for (int i = 0; i < value.length; i++) {
				ret[i] = __chs[value[i]];
			}
			return new String(ret);
		}
	}

	/**
	 * Next sequence.
	 * 
	 * @param name
	 *            the name
	 * @return the long
	 */
	public static Long nextSequence(String name) {
		synchronized (__sync_sequences) {
			Long cur = (Long) __sequences.get(name);
			long val = cur.longValue();
			if (val++ == 9223372036854775807L) {
				val = 0L;
			}
			Long next = new Long(val);
			__sequences.put(name, next);
			return next;
		}
	}

	/**
	 * The Class Serial.
	 * 
	 * @copyright www.jdframe.com
	 * @Package com.jdframe.sys.core.util
	 * @Description: TODO
	 * @author: support@jdframe.com
	 * @date: 2013-10-29 15:53:36
	 * @version v1.0
	 */
	private static class Serial {

		/** The value. */
		private int[] value;

		/** The max bit value. */
		private int maxBitValue;

		/**
		 * Instantiates a new serial.
		 * 
		 * @param initValue
		 *            the init value
		 * @param maxBitValue
		 *            the max bit value
		 */
		public Serial(int[] initValue, int maxBitValue) {
			this.maxBitValue = maxBitValue;
			this.value = new int[initValue.length];
			for (int i = 0; i < initValue.length; i++)
				this.value[i] = initValue[i];
		}

		/**
		 * Next.
		 * 
		 * @return the int[]
		 */
		public int[] next() {
			int last = this.value.length - 1;
			int i = last;
			this.value[i] += 1;
			while (this.value[i] == this.maxBitValue) {
				if (i == 0) {
					for (int j = 0; j <= last; j++) {
						this.value[j] = 0;
					}
					break;
				}

				this.value[i] = 0;
				i--;
				this.value[i] += 1;
			}
			return this.value;
		}

		/**
		 * Current.
		 * 
		 * @return the int[]
		 */
		public int[] current() {
			return this.value;
		}
	}
}