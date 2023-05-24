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


/**
 * @Description :
 * @Author : goshawker@yeah.net
 * @Date : 2023-02-14 10:35
*/
public class JCodeBuilder {
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

  /**
   * 生产功能入口页面html、新建页面html、更新页面html、vo对象、action对象java代码
   */
  public void generate() throws Exception {
        //building main.html
        FileUtils.generateMainHtml();
        //building new.html
        FileUtils.generateNewHtml();
        //building update.html
        FileUtils.generateUpdateHtml();
        //building action.java
        FileUtils.generateAction();
        //building common.css
        FileUtils.generateCss();
    }
}
