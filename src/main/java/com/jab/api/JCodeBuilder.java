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

    boolean mainjsp = FileUtils.generateMainHtml();
    if (mainjsp) {
      log.debug("generate main.jsp success.");

    }


//        boolean createjsp = FileUtils.generateNewJsp(table, namespace, targetProject);
//        if (createjsp) {
//          log.debug("generate new.jsp success.");
//        }
//        boolean modifyjsp = FileUtils.generateUpdateJsp(table, namespace, targetProject);
//        if (modifyjsp) {
//          log.debug("generate update.jsp success.");
//        }
//
//        boolean mainaction = FileUtils.generateAction(table, namespace, targetProject);
//        if (mainaction) {
//          log.debug("generate mainaction file success.");
//        }
//        boolean queryaction = FileUtils.generateQueryAction(table, namespace, targetProject);
//        if (queryaction) {
//          log.debug("generate queryaction file success.");
//        }
//        boolean createaction = FileUtils.generateNewAction(table, namespace, targetProject);
//        if (createaction) {
//          log.debug("generate createaction file success.");
//        }
//        boolean modifyaction = FileUtils.generateUpdateAction(table, namespace, targetProject);
//        if (modifyaction) {
//          log.debug("generate modifyaction file success.");
//        }
//
//        boolean deleteaction = FileUtils.generateDeleteAction(table, namespace, targetProject);
//        if (deleteaction) {
//          log.debug("generate modifyaction file success.");
//        }
//
//        boolean configaction = FileUtils.generateConfiguration(table, namespace, targetProject);
//        if (configaction) {
//          log.debug("generate configaction file success.");
//        }


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
