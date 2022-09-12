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
import com.jab.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.generator.api.*;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.XmlFileMergerJaxp;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.mybatis.generator.internal.util.ClassloaderUtility.getCustomClassloader;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:06
* @Version: 1.0
*/
public class JCodeBuilder {
  private final Logger log = LogManager.getLogger(getClass());

  private final Configuration configuration;

  private final ShellCallback shellCallback;

  private final List<GeneratedJavaFile> generatedJavaFiles;

  private final List<GeneratedXmlFile> generatedXmlFiles;

  private final List<String> warnings;

  private final Set<String> projects;

  private final List<IntrospectedTable> introspectedTables = new ArrayList();

  /**
   * Constructs a MyBatisGenerator object.
   *
   * @param configuration The configuration for this invocation
   * @param shellCallback an instance of a ShellCallback interface. You may specify
   *                      <code>null</code> in which case the DefaultShellCallback will
   *                      be used.
   * @param warnings      Any warnings generated during execution will be added to this
   *                      list. Warnings do not affect the running of the tool, but they
   *                      may affect the results. A typical warning is an unsupported
   *                      data type. In that case, the column will be ignored and
   *                      generation will continue. You may specify <code>null</code> if
   *                      you do not want warnings returned.
   * @throws InvalidConfigurationException if the specified configuration is invalid
   */
  public JCodeBuilder(Configuration configuration, ShellCallback shellCallback,
                      List<String> warnings) throws InvalidConfigurationException {
    super();
    if (configuration == null) {
      throw new IllegalArgumentException(getString("RuntimeError.2")); //$NON-NLS-1$
    } else {
      this.configuration = configuration;
    }

    if (shellCallback == null) {
      this.shellCallback = new DefaultShellCallback(false);
    } else {
      this.shellCallback = shellCallback;
    }

    if (warnings == null) {
      this.warnings = new ArrayList<String>();
    } else {
      this.warnings = warnings;
    }
    generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
    generatedXmlFiles = new ArrayList<GeneratedXmlFile>();
    projects = new HashSet<String>();

    this.configuration.validate();
  }

  /**
   * This is the main method for generating code. This method is long running,
   * but progress can be provided and the method can be canceled through the
   * ProgressCallback interface. This version of the method runs all
   * configured contexts.
   *
   * @param callback an instance of the ProgressCallback interface, or
   *                 <code>null</code> if you do not require progress information
   * @throws SQLException
   * @throws IOException
   * @throws InterruptedException if the method is canceled through the ProgressCallback
   */
  public void generate(ProgressCallback callback) throws SQLException,
          IOException, InterruptedException {
    generate(callback, null, null);
  }

  /**
   * This is the main method for generating code. This method is long running,
   * but progress can be provided and the method can be canceled through the
   * ProgressCallback interface.
   *
   * @param callback   an instance of the ProgressCallback interface, or
   *                   <code>null</code> if you do not require progress information
   * @param contextIds a set of Strings containing context ids to run. Only the
   *                   contexts with an id specified in this list will be run. If the
   *                   list is null or empty, than all contexts are run.
   * @throws InvalidConfigurationException
   * @throws SQLException
   * @throws IOException
   * @throws InterruptedException          if the method is canceled through the ProgressCallback
   */
  public void generate(ProgressCallback callback, Set<String> contextIds)
          throws SQLException, IOException, InterruptedException {
    generate(callback, contextIds, null);
  }

  /**
   * This is the main method for generating code. This method is long running,
   * but progress can be provided and the method can be cancelled through the
   * ProgressCallback interface.
   *
   * @param callback                 an instance of the ProgressCallback interface, or
   *                                 <code>null</code> if you do not require progress information
   * @param contextIds               a set of Strings containing context ids to run. Only the
   *                                 contexts with an id specified in this list will be run. If the
   *                                 list is null or empty, than all contexts are run.
   * @param fullyQualifiedTableNames a set of table names to generate. The elements of the set must
   *                                 be Strings that exactly match what's specified in the
   *                                 configuration. For example, if table name = "foo" and schema =
   *                                 "bar", then the fully qualified table name is "foo.bar". If
   *                                 the Set is null or empty, then all tables in the configuration
   *                                 will be used for code generation.
   * @throws InvalidConfigurationException
   * @throws SQLException
   * @throws IOException
   * @throws InterruptedException          if the method is canceled through the ProgressCallback
   */
  public void generate(ProgressCallback callback, Set<String> contextIds,
                       Set<String> fullyQualifiedTableNames) throws SQLException,
          IOException, InterruptedException {

    if (callback == null) {
      callback = new VerboseProgressCallback();
    }

    generatedJavaFiles.clear();
    generatedXmlFiles.clear();
    //  log.debug("configuration document: "+ configuration.toDocument().getFormattedContent());
    // calculate the contexts to run
    List<Context> contextsToRun;
    if (contextIds == null || contextIds.size() == 0) {
      contextsToRun = configuration.getContexts();
    } else {
      contextsToRun = new ArrayList<Context>();
      for (Context context : configuration.getContexts()) {
        if (contextIds.contains(context.getId())) {
          contextsToRun.add(context);
        }
      }
    }

    // setup custom classloader if required
    if (configuration.getClassPathEntries().size() > 0) {
      ClassLoader classLoader = getCustomClassloader(configuration.getClassPathEntries());
      ObjectFactory.addExternalClassLoader(classLoader);
    }

    // now run the introspections...
    int totalSteps = 0;
    for (Context context : contextsToRun) {
      totalSteps += context.getIntrospectionSteps();
    }
    callback.introspectionStarted(totalSteps);

    for (Context context : contextsToRun) {
      context.introspectTables(callback, warnings,
              fullyQualifiedTableNames);
      //log.debug("size "+context.getIntrospectedTables().size());
      //add tables infomation for generator
      introspectedTables.addAll(context.getIntrospectedTables());

    }
    // now run the generates
    totalSteps = 0;
    for (Context context : contextsToRun) {
      totalSteps += context.getGenerationSteps();
    }
    callback.generationStarted(totalSteps);

    for (Context context : contextsToRun) {
      context.generateFiles(callback, generatedJavaFiles,
              generatedXmlFiles, null, null, warnings);
    }

    // now save the files
    callback.saveStarted(generatedXmlFiles.size()
            + generatedJavaFiles.size());

    for (GeneratedXmlFile gxf : generatedXmlFiles) {
      projects.add(gxf.getTargetProject());

      File targetFile;
      String source;
      try {
        File directory = shellCallback.getDirectory(gxf
                .getTargetProject(), gxf.getTargetPackage());
        targetFile = new File(directory, gxf.getFileName());
        if (targetFile.exists()) {
          if (gxf.isMergeable()) {
            source = XmlFileMergerJaxp.getMergedSource(gxf,
                    targetFile);
          } else if (shellCallback.isOverwriteEnabled()) {
            source = gxf.getFormattedContent();
            warnings.add(getString("Warning.11", //$NON-NLS-1$
                    targetFile.getAbsolutePath()));
          } else {
            source = gxf.getFormattedContent();
            targetFile = getUniqueFileName(directory, gxf
                    .getFileName());
            warnings.add(getString(
                    "Warning.2", targetFile.getAbsolutePath())); //$NON-NLS-1$
          }
        } else {
          source = gxf.getFormattedContent();
        }
      } catch (ShellException e) {
        warnings.add(e.getMessage());
        continue;
      }

      callback.checkCancel();
      callback.startTask(getString(
              "Progress.15", targetFile.getName())); //$NON-NLS-1$
      FileUtils.writeFile(targetFile, source, "UTF-8"); //$NON-NLS-1$
    }

    for (GeneratedJavaFile gjf : generatedJavaFiles) {
      projects.add(gjf.getTargetProject());

      File targetFile;
      String source;
      try {
        File directory = shellCallback.getDirectory(gjf
                .getTargetProject(), gjf.getTargetPackage());
        targetFile = new File(directory, gjf.getFileName());
        if (targetFile.exists()) {
          if (shellCallback.isMergeSupported()) {
            source = shellCallback.mergeJavaFile(gjf
                            .getFormattedContent(), new File(targetFile
                            .getAbsolutePath()),
                    MergeConstants.getOldElementTags(),
                    gjf.getFileEncoding());
          } else if (shellCallback.isOverwriteEnabled()) {
            source = gjf.getFormattedContent();
            warnings.add(getString("Warning.11", //$NON-NLS-1$
                    targetFile.getAbsolutePath()));
          } else {
            source = gjf.getFormattedContent();
            targetFile = getUniqueFileName(directory, gjf
                    .getFileName());
            warnings.add(getString(
                    "Warning.2", targetFile.getAbsolutePath())); //$NON-NLS-1$
          }
        } else {
          source = gjf.getFormattedContent();
        }

        callback.checkCancel();
        callback.startTask(getString(
                "Progress.15", targetFile.getName())); //$NON-NLS-1$
        FileUtils.writeFile(targetFile, source, gjf.getFileEncoding());
      } catch (ShellException e) {
        warnings.add(e.getMessage());
      }
    }


    for (String project : projects) {
      shellCallback.refreshProject(project);
    }


    for (IntrospectedTable table : introspectedTables) {
      Properties properties = table.getTableConfiguration().getProperties();

      boolean generateExample = Boolean.parseBoolean(StringUtils.toNonNull(properties.get("generateExample")));
      String namespace = StringUtils.toNonNull(properties.get("namespace"));
      String targetProject = StringUtils.toNonNull(properties.get("targetProject"));
      if (namespace.equals("")) {
        namespace = "tempDir/";
      }
      //auto-generate Jsp,action and configuration
      if (generateExample) {
        boolean mainjsp = FileUtils.generateMainJsp(table, namespace, targetProject);
        if (mainjsp) {
          log.debug("generate main.jsp success.");

        }
        boolean createjsp = FileUtils.generateNewJsp(table, namespace, targetProject);
        if (createjsp) {
          log.debug("generate new.jsp success.");
        }
        boolean modifyjsp = FileUtils.generateUpdateJsp(table, namespace, targetProject);
        if (modifyjsp) {
          log.debug("generate update.jsp success.");
        }

        boolean mainaction = FileUtils.generateAction(table, namespace, targetProject);
        if (mainaction) {
          log.debug("generate mainaction file success.");
        }
        boolean queryaction = FileUtils.generateQueryAction(table, namespace, targetProject);
        if (queryaction) {
          log.debug("generate queryaction file success.");
        }
        boolean createaction = FileUtils.generateNewAction(table, namespace, targetProject);
        if (createaction) {
          log.debug("generate createaction file success.");
        }
        boolean modifyaction = FileUtils.generateUpdateAction(table, namespace, targetProject);
        if (modifyaction) {
          log.debug("generate modifyaction file success.");
        }

        boolean deleteaction = FileUtils.generateDeleteAction(table, namespace, targetProject);
        if (deleteaction) {
          log.debug("generate modifyaction file success.");
        }

        boolean configaction = FileUtils.generateConfiguration(table, namespace, targetProject);
        if (configaction) {
          log.debug("generate configaction file success.");
        }

        List<GeneratedXmlFile> list = table.getGeneratedXmlFiles();
        for (GeneratedXmlFile generatedXmlFile : list) {

          String xmlpath = generatedXmlFile.getTargetPackage().replaceAll("\\.", "/") + "/" + generatedXmlFile.getFileName();
          System.out.println(" <!--Important :: Add follow auto-generated mapper To src/DBConfig.xml -->");
          String mapper = " <mapper resource=\"" + xmlpath + "\" />";
          System.out.println(mapper + "\r\n");
        }
      }
    }

    callback.done();
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
