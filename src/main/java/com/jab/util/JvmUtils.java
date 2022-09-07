package com.jab.util;

import java.lang.management.*;

// TODO: Auto-generated Javadoc
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 14:18
 * @version 1.0
 */
public class JvmUtils {

	/**
	 * Hc_memory.
	 * 
	 * @return the long[]
	 */
	public static long[] hc_memory() {
		long[] l = new long[3];
		try {
			MemoryUsage mu = ManagementFactory.getMemoryMXBean()
					.getHeapMemoryUsage();
			// long init = mu.getInit();
			long used = mu.getUsed();
			// long commit = mu.getCommitted();
			long max = mu.getMax();

			l[0] = Math.round(used / max * 100);
			l[1] = used;
			l[2] = max;
// Function Test . 
//			for (int i = 0; i < 150; i++) {
//				new Thread() {
//					public void run() {
//						synchronized (Demo.lock) {
//							Demo.lst.add(new Date[4000]);
//							// System.out.println("Mem Test ...");
//							try {
//								this.sleep(100);
//							} catch (Exception e) {
//								// TODO: handle exception
//							}
//						}
//
//					}
//				}.start();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;

	}

	/**
	 * Hc_thread.
	 * 
	 * @return the long[]
	 */
	public static long[] hc_thread() {
		long[] l = new long[4];
		try {
			ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
			int peak_count = tmb.getPeakThreadCount();
			int active_count = tmb.getThreadCount();
			int block_count = 0;
			long[] ids = tmb.getAllThreadIds();
			ThreadInfo[] tis = tmb.getThreadInfo(ids);
			for (int i = 0; i < tis.length; i++) {
				if (tis[i] != null && tis[i].getThreadState() == Thread.State.BLOCKED) {
					block_count++;
				}
			}
			l[0] = Math.round(block_count / active_count * 100);
			l[1] = active_count;
			l[2] = block_count;
			l[3] = peak_count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;

	}

	/**
	 * Hc_thread.
	 * 
	 * @return the long[]
	 * @deprecated
	 */
	public static double hc_cpu() {
		OperatingSystemMXBean omb = ManagementFactory
				.getOperatingSystemMXBean();

		System.out.println(omb.getArch());
		System.out.println(omb.getAvailableProcessors());
		System.out.println(omb.getName());
		System.out.println(omb.getSystemLoadAverage());
		System.out.println(omb.getVersion());
		return omb.getSystemLoadAverage();
	}

	public static void main(String[] args) {
		System.out.println(hc_cpu());
	}
}
