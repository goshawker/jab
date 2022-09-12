package com.jab.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:06
* @Version: 1.0
*/
public interface ValueObject extends Serializable, Cloneable {
    Logger log = LogManager.getLogger(ValueObject.class);
}