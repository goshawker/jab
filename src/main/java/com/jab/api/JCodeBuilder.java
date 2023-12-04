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

import com.jab.util.DBUtils;
import com.jab.util.FileUtils;
import com.jab.util.XmlUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;


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
        //FileUtils.generateMainHtml();
//        FileUtils.generateNewHtml();

//        FileUtils.generateUpdateHtml();

//        FileUtils.generateAction();


        try {
          //  FileUtils.generateActionServlet("core");
            ArrayList<Node> arrayList = XmlUtils.getNodesByTagName("items");
            for (int i = 0; i < arrayList.size(); i++) {
                Node node = arrayList.get(i);
                NamedNodeMap namedNodeMaps = node.getAttributes();
                String namespace = XmlUtils.getNodeValue(namedNodeMaps, "namespace").toLowerCase();
                String tableName = XmlUtils.getNodeValue(namedNodeMaps, "tableName").toLowerCase();
                String page = XmlUtils.getNodeValue(namedNodeMaps, "page").toLowerCase();
                System.out.printf("\n namespace:%s tableName:%s \n", namespace, tableName);

                ArrayList<Node> itemNodes = XmlUtils.getChildNodes(node.getChildNodes(), "item");
                //item node  attributes
                NamedNodeMap[] itemsAttributes = new NamedNodeMap[itemNodes.size()];
                for (int j = 0; j < itemNodes.size(); j++) {
                    Node child = itemNodes.get(j);
                    itemsAttributes[j] = child.getAttributes();
                }
                FileUtils.clearScripts();
                if ("".equals(page) || page.contains("main")) {
                    FileUtils.generateMainHtml(namespace, itemsAttributes);
                }
                if ("".equals(page) || page.contains("new")) {
                    FileUtils.generateNewHtml(namespace, itemsAttributes);
                }
                if ("".equals(page) || page.contains("update")) {
                    FileUtils.generateUpdateHtml(namespace, itemsAttributes);
                }
                FileUtils.generateAction(namespace, tableName, itemsAttributes);
                FileUtils.generateCss(namespace);
                FileUtils.generateJavascript(namespace);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
