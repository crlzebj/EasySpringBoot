package com.zjx.EasySpringBoot.generator;

import com.zjx.EasySpringBoot.constant.PathConstant;
import com.zjx.EasySpringBoot.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ProjectGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    // pom.xml文件模板
    private static final String POM_TEMPLATE;

    static {
        POM_TEMPLATE = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                
                    <groupId>%s</groupId>
                    <artifactId>%s</artifactId>
                    <version>1.0-SNAPSHOT</version>
                
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                        <lombok.version>1.18.26</lombok.version>
                        <mysql.version>8.0.30</mysql.version>
                    </properties>
                
                    <dependencies>
                        <!-- lombok -->
                        <dependency>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </dependency>
                
                        <!-- mysql -->
                        <dependency>
                            <groupId>mysql</groupId>
                            <artifactId>mysql-connector-java</artifactId>
                            <version>${mysql.version}</version>
                        </dependency>
                    </dependencies>
                
                </project>
                """;
    }

    /**
     * 生成项目目录
     */
    public static void generateDirectory() {
        // 创建pojo目录
        File file = new File(PathConstant.POJO + "entity/");
        if (file.exists() || file.mkdirs()) {
            logger.info("entity 目录创建成功!");
        } else {
            logger.info("entity 目录创建失败");
            return;
        }
        file = new File(PathConstant.POJO + "dto/");
        if (file.exists() || file.mkdirs()) {
            logger.info("dto 目录创建成功!");
        } else {
            logger.info("dto 目录创建失败");
            return;
        }
        file = new File(PathConstant.POJO + "vo/");
        if (file.exists() || file.mkdirs()) {
            logger.info("vo 目录创建成功!");
        } else {
            logger.info("vo 目录创建失败");
            return;
        }

        // 创建mapper目录
        file = new File(PathConstant.MAPPER_INTERFACE);
        if (file.exists() || file.mkdirs()) {
            logger.info("mapper 目录创建成功!");
        } else {
            logger.info("mapper 目录创建失败");
            return;
        }

        // 创建service目录
        file = new File(PathConstant.SERVICE);
        if (file.exists() || file.mkdirs()) {
            logger.info("service 目录创建成功!");
        } else {
            logger.info("service 目录创建失败");
            return;
        }

        // 创建controller目录
        file = new File(PathConstant.CONTROLLER);
        if (file.exists() || file.mkdirs()) {
            logger.info("controller 目录创建成功!");
        } else {
            logger.info("controller 目录创建失败");
            return;
        }

        // 创建resources目录
        file = new File(PathConstant.MAIN_RESOURCE);
        if (file.exists() || file.mkdirs()) {
            logger.info("resources 目录创建成功!");
        } else {
            logger.info("resources 目录创建失败");
            return;
        }

        // 创建mapper xml目录
        file = new File(PathConstant.MAPPER_XML);
        if (file.exists() || file.mkdirs()) {
            logger.info("mapper xml 目录创建成功!");
        } else {
            logger.info("mapper xml 目录创建失败");
        }
    }

    /**
     * 生成pom.xml文件
     */
    public static void generatePom() {
        File file = new File(PathConstant.PROJECT_ROOT + "pom.xml");
        if(file.exists()){
            logger.info("pom.xml 文件已存在");
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String pomContent = POM_TEMPLATE.formatted(PropertiesReader.getSetting("package.prefix"), PropertiesReader.getSetting("project.name"));
            writer.write(pomContent);
            writer.flush();
            writer.close();
            logger.info("pom.xml 生成成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 生成基础SpringBoot项目
     */
    public static void generateProject() {
        generateDirectory();
        generatePom();
    }
}
