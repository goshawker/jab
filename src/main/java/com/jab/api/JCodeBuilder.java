/*
 *  Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.jab.api;

import com.jab.util.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @Author: goshawker@yeah.net
 * @Description:
 * @Date: 2022/9/12 11:06
 * @Version: 1.0
 */
public class JCodeBuilder {
  private final Logger log = LogManager.getLogger(JCodeBuilder.class);

  public JCodeBuilder() {
  }

  public static void main(String[] args) {
    JCodeBuilder jCodeBuilder = new JCodeBuilder();
    jCodeBuilder.build();
  }

  public void build() {
    try {
      generate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void generate() throws Exception {
    //generate main.html
    boolean mainHtml = FileUtils.generateMainHtml();
    if (mainHtml) {
      log.debug("generate main.html success.");

    }
    //generate new.html
    boolean newHtml = FileUtils.generateNewHtml();
    if (newHtml) {
      log.debug("generate new.html success.");
    }
    //generate update.html
    boolean updateHtml = FileUtils.generateUpdateHtml();
    if (updateHtml) {
      log.debug("generate update.html success.");
    }
    //generate valueObject.java
    boolean valueObject = FileUtils.generateValueObject();
    if (valueObject) {
      log.debug("generate valueObject.java success.");

    }

    boolean action = FileUtils.generateAction();
    if (action) {
      log.debug("generate action file success.");
    }
  }

  private File getUniqueFileName(File directory, String fileName) {
    File answer = null;

    // try up to 1000 times to generate a unique file name
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i < 1000; i++) {
      sb.setLength(0);
      sb.append(fileName);
      sb.append('.');
      sb.append(i);

      File testFile = new File(directory, sb.toString());
      if (!testFile.exists()) {
        answer = testFile;
        break;
      }
    }

    if (answer == null) {
      throw new RuntimeException(getString(
              "RuntimeError.3", directory.getAbsolutePath())); //$NON-NLS-1$
    }
    return answer;
  }
}
