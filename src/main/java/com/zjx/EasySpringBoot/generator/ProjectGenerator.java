package com.zjx.EasySpringBoot.generator;

import com.zjx.EasySpringBoot.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ProjectGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    // pom.xml 生成路径
    private static final String POM_PATH;
    // Entity 生成路径
    private static final String ENTITY_PATH;
    // DTO 生成路径
    private static final String DTO_PATH;
    private static final String MAIN_RESOURCES_PATH;
    private static final String TEST_JAVA_PATH;

    static {
        POM_PATH = PropertiesReader.getSetting("project.path") + PropertiesReader.getSetting("project.name");
        ENTITY_PATH = POM_PATH + "/src/main/java/" +
                      PropertiesReader.getSetting("package.prefix").replace(".", "/") + "/" +
                      PropertiesReader.getSetting("project.name") + "/pojo/entity";
        DTO_PATH = ENTITY_PATH.replace("/entity", "/dto");
        MAIN_RESOURCES_PATH = POM_PATH + "/src/main/resource";
        TEST_JAVA_PATH = POM_PATH + "/src/test/java";
    }

    /**
     * 生成项目目录
     */
    public static void generateDirectory() {
        // 创建 pojo 目录
        File file = new File(ENTITY_PATH);
        if (file.exists() || file.mkdirs()) {
            logger.info("entity 目录创建成功!");
        } else {
            logger.info("entity 目录创建失败");
            return;
        }
        file = new File(DTO_PATH);
        if (file.exists() || file.mkdirs()) {
            logger.info("dto 目录创建成功!");
        } else {
            logger.info("dto 目录创建失败");
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
        }
    }

    /**
     * 生成 pom.xml 文件
     */
    public static void generatePom() {
        File file = new File(POM_PATH + "/pom.xml");
        if(file.exists()){
            logger.info("pom.xml 文件已存在");
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.flush();
            writer.close();
            logger.info("pom.xml 生成成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 生成基础 SpringBoot 项目
     */
    public static void generateProject() {
        generateDirectory();
        generatePom();
    }
}
