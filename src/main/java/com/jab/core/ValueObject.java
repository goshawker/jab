package com.jab.core;

import org.apache.log4j.Logger;
import java.io.Serializable;

public interface ValueObject extends Serializable, Cloneable {
  Logger log = Logger.getLogger(ValueObject.class);
}