package com.jab.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:04
* @Version: 1.0
*/
public class ClassUtils {
	
	/** The Constant DOT. */
	private final static char DOT = '.';
	
	/** The Constant SLASH. */
	private final static char SLASH = '/';
	
	/** The Constant CLASS_SUFFIX. */
	private final static String CLASS_SUFFIX = ".class";
	
	/** The Constant BAD_PACKAGE_ERROR. */
	private final static String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the given '%s' package exists?";

	/**
	 * Find.
	 *
	 * @param scannedPackage the scanned package
	 * @return the list
	 */
	public final static List<Class<?>> find(final String scannedPackage) {
		final ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		final String scannedPath = scannedPackage.replace(DOT, SLASH);
		final Enumeration<URL> resources;
		try {
			resources = classLoader.getResources(scannedPath);
		} catch (IOException e) {
			throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR,
					scannedPath, scannedPackage), e);
		}
		final List<Class<?>> classes = new LinkedList<Class<?>>();
		while (resources.hasMoreElements()) {
			URL url=resources.nextElement();
			final File file = new File(url.getFile());
			classes.addAll(find(file, scannedPackage));
		}
		return classes;
	}

	/**
	 * Find.
	 *
	 * @param file the file
	 * @param scannedPackage the scanned package
	 * @return the list
	 */
	private final static List<Class<?>> find(final File file,
			final String scannedPackage) {
		final List<Class<?>> classes = new LinkedList<Class<?>>();
		if (file.isDirectory()) {
			for (File nestedFile : file.listFiles()) {
				
				classes.addAll(find(nestedFile, scannedPackage));
			}
			// File names with the $1, $2 holds the anonymous inner classes, we
			// are not interested on them.
		} else if (file.getName().endsWith(CLASS_SUFFIX) && !file.getName().contains("$")) {

			final int beginIndex = 0;
			final int endIndex = file.getName().length() - CLASS_SUFFIX.length();
			final String className = file.getName().substring(beginIndex,endIndex);
			try {
				String resource = scannedPackage + DOT + className;
				try {
					String path = file.getCanonicalPath().replaceAll("\\\\",".");
					path = path.substring(0,path.length()-CLASS_SUFFIX.length());
					String classpath = path.substring(path.indexOf(scannedPackage));
					//System.out.println(classpath);
					resource  = classpath;
					
					System.out.println(resource);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				classes.add(Class.forName(resource));
			} catch (ClassNotFoundException ignore) {
			}
		}
		return classes;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			List<Class<?>> classes = ClassUtils.find("com");
			System.out.println(classes.size());
			for (Class<?> aClass : classes) {
				// Constructor constructor = aClass.getConstructor();
				System.out.println(aClass.getCanonicalName());

				// Create an object of the class type
				// constructor.newInstance();
				// ...
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}
}
