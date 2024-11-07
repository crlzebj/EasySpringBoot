package com.zjx.EasySpringBoot.generator;

import com.zjx.EasySpringBoot.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ProjectGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    // 项目所在目录
    private static final String PROJECT_PATH;
    // 项目名
    private static final String PROJECT_NAME;
    private static final String MAIN_JAVA_PATH;
    private static final String MAIN_RESOURCES_PATH;
    private static final String TEST_JAVA_PATH;
    private static final String PACKAGE_PATH;

    static {
        PROJECT_PATH = PropertiesReader.getSetting("project.path");
        PROJECT_NAME = PropertiesReader.getSetting("project.name");
        MAIN_JAVA_PATH = PROJECT_PATH + PROJECT_NAME + "/src/main/java/";
        MAIN_RESOURCES_PATH = PROJECT_PATH + PROJECT_NAME + "/src/main/resource/";
        TEST_JAVA_PATH = PROJECT_PATH + PROJECT_NAME + "/src/test/java/";
        PACKAGE_PATH = MAIN_JAVA_PATH + PropertiesReader.getSetting("package.prefix").replace(".", "/") + "/" + PROJECT_NAME;
    }

    /**
     * 生成项目目录
     */
    public static void generateDirectory() {
        // 创建项目根目录
        File file = new File(PROJECT_PATH + PROJECT_NAME);
        if (file.exists() || file.mkdir()) {
            logger.info("项目根目录创建成功！");
        } else {
            logger.info("项目根目录创建失败");
            return;
        }

        // 创建 main/java 目录
        file = new File(MAIN_JAVA_PATH);
        if (file.exists() || file.mkdirs()) {
            logger.info("main/java 目录创建成功!");
        } else {
            logger.info("main/java 目录创建失败");
            return;
        }

        // 创建 main/resources 目录
        file = new File(MAIN_RESOURCES_PATH);
        if (file.exists() || file.mkdirs()) {
            logger.info("main/resources 目录创建成功!");
        } else {
            logger.info("main/resources 目录创建失败");
            return;
        }

        // 创建 test/java 目录
        file = new File(TEST_JAVA_PATH);
        if (file.exists() || file.mkdirs()) {
            logger.info("test/java 目录创建成功!");
        } else {
            logger.info("test/java 目录创建失败");
            return;
        }

        // 创建包
        file = new File(PACKAGE_PATH);
        if (file.exists() || file.mkdirs()) {
            logger.info("包创建成功!");
        } else {
            logger.info("包创建失败");
        }
    }

    /**
     * 生成 pom.xml 文件
     */
    public static void generatePom() {
        File file = new File(PROJECT_PATH + PROJECT_NAME + "/pom.xml");
        if(file.exists()){
            logger.info("pom.xml 文件已存在");
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.flush();
            logger.info("pom.xml 生成成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
