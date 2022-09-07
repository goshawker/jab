package com.jab.core;

import org.apache.log4j.Logger;

import java.io.Serializable;

public interface VOProject extends Serializable,Cloneable {
	  /** The _log. */
  	Logger log = Logger.getLogger("VOProject");
}