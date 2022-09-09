package com.jab.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


// TODO: Auto-generated Javadoc

/**
 * @author mike
 * @version 1.0
 * @description: TODO
 * @date 2022/9/7 14:18
 */
public class URLUtils {

  /**
   * From class.
   *
   * @param contextClass the context class
   * @return the url
   */
  public static URL fromClass(Class contextClass) {
    String resourceName = contextClass.getName();
    int dotIndex = resourceName.lastIndexOf('.');

    if (dotIndex != -1)
      resourceName = resourceName.substring(0, dotIndex);
    resourceName = resourceName + ".properties";

    return fromResource(contextClass, resourceName);
  }

  /**
   * From filename.
   *
   * @param filename the filename
   * @return the url
   */
  public static URL fromFilename(String filename) {
    if (filename == null)
      return null;
    File file = new File(filename);
    URL url = null;
    try {
      if (file.exists())
        url = file.toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
      url = null;
    }
    return url;
  }

  /**
   * From resource.
   *
   * @param resourceName the resource name
   * @param loader       the loader
   * @return the url
   */
  public static URL fromResource(String resourceName, ClassLoader loader) {
    URL url = null;
    if ((loader != null) && (url == null))
      url = loader.getResource(resourceName);
    if ((loader != null) && (url == null))
      url = loader.getResource(resourceName + ".properties");

    if ((loader == null) && (url == null)) {
      try {
        loader = Thread.currentThread().getContextClassLoader();
      } catch (SecurityException e) {
        URLUtils utilURL = new URLUtils();

        loader = utilURL.getClass().getClassLoader();
      }
    }

    if (url == null)
      url = loader.getResource(resourceName);
    if (url == null)
      url = loader.getResource(resourceName + ".properties");

    if (url == null)
      url = ClassLoader.getSystemResource(resourceName);
    if (url == null)
      url = ClassLoader.getSystemResource(resourceName + ".properties");

    if (url == null)
      url = fromFilename(resourceName);

    return url;
  }

  /**
   * From resource.
   *
   * @param contextClass the context class
   * @param resourceName the resource name
   * @return the url
   */
  public static URL fromResource(Class contextClass, String resourceName) {
    if (contextClass == null) {
      return fromResource(resourceName, null);
    }
    return fromResource(resourceName, contextClass.getClassLoader());
  }

  /**
   * From resource.
   *
   * @param resourceName the resource name
   * @return the url
   */
  public static URL fromResource(String resourceName) {
    return fromResource(resourceName, null);
  }
}