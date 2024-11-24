package com.zjx.easyspringboot.generator;

import com.zjx.easyspringboot.constant.PackageConstant;
import com.zjx.easyspringboot.constant.PathConstant;
import com.zjx.easyspringboot.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 基础SpringBoot项目生成类
 */
public class ProjectGenerator {
    private static final Logger log = LoggerFactory.getLogger(ProjectGenerator.class);

    // pom.xml文件模板
    private static final String POM_TEMPLATE;
    // springboot配置文件模板
    private static final String YML_TEMPLATE;
    // springboot启动类模板
    private static final String SPRINGBOOT_APPLICATION_TEMPLATE;

    static {
        POM_TEMPLATE = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                
                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>2.7.3</version>
                    </parent>
                
                    <groupId>%s</groupId>
                    <artifactId>%s</artifactId>
                    <version>1.0-SNAPSHOT</version>
                
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                        <lombok.version>1.18.26</lombok.version>
                        <druid.spring.version>1.2.1</druid.spring.version>
                        <mybatis.spring.version>2.2.0</mybatis.spring.version>
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
                        </dependency>
                
                        <!-- druid连接池 -->
                        <dependency>
                            <groupId>com.alibaba</groupId>
                            <artifactId>druid-spring-boot-starter</artifactId>
                            <version>${druid.spring.version}</version>
                        </dependency>
                
                        <!-- mybatis-spring -->
                        <dependency>
                            <groupId>org.mybatis.spring.boot</groupId>
                            <artifactId>mybatis-spring-boot-starter</artifactId>
                            <version>${mybatis.spring.version}</version>
                        </dependency>
                
                        <!-- redis -->
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-data-redis</artifactId>
                        </dependency>
                
                        <!-- 连接池依赖 -->
                        <dependency>
                            <groupId>org.apache.commons</groupId>
                            <artifactId>commons-pool2</artifactId>
                        </dependency>
                
                        <!-- springboot -->
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter</artifactId>
                        </dependency>
                
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-test</artifactId>
                            <scope>test</scope>
                        </dependency>
                
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                            <scope>compile</scope>
                        </dependency>
                    </dependencies>
                </project>
                """;

        YML_TEMPLATE = """
                server:
                  port: 8080
                
                spring:
                  # 数据源配置
                  datasource:
                    druid:
                      driver-class-name: %s
                      url: %s
                      username: %s
                      password: %s
                
                  # redis配置
                  redis:
                    host: %s
                    port: %s
                    password: %s
                    database: 0
                    lettuce:
                      pool:
                        max-active: 8
                        max-idle: 8
                        min-idle: 0
                        max-wait: 100
                
                # mybatis配置
                mybatis:
                  # mapper xml文件
                  mapper-locations: classpath:mapper/*.xml
                  # po包
                  type-aliases-package: %s
                  configuration:
                    # 开启驼峰命名
                    map-underscore-to-camel-case: true
                """;

        SPRINGBOOT_APPLICATION_TEMPLATE = """
                package %s;
                
                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;
                
                @SpringBootApplication
                public class %sApplication {
                    public static void main(String[] args) {
                        SpringApplication.run(%sApplication.class, args);
                    }
                }
                """;
    }

    /**
     * 生成项目目录
     */
    public static void generateDirectory() {
        // 创建pojo目录
        Path poPath = Paths.get(PathConstant.APPLICATION_ROOT, "pojo/po");
        Path voPath = Paths.get(PathConstant.APPLICATION_ROOT, "pojo/vo");
        if (!Files.exists(poPath)) {
            try {
                Files.createDirectories(poPath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        if (!Files.exists(voPath)) {
            try {
                Files.createDirectories(voPath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        // 创建mapper目录
        Path mapperPath = Paths.get(PathConstant.APPLICATION_ROOT, "mapper");
        if (!Files.exists(mapperPath)) {
            try {
                Files.createDirectories(mapperPath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        // 创建service目录
        Path servicePath = Paths.get(PathConstant.APPLICATION_ROOT, "service/impl");
        if (!Files.exists(servicePath)) {
            try {
                Files.createDirectories(servicePath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        // 创建controller目录
        Path controllerPath = Paths.get(PathConstant.APPLICATION_ROOT, "controller");
        if (!Files.exists(controllerPath)) {
            try {
                Files.createDirectories(controllerPath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        // 创建mapper xml目录
        Path mapperxmlPath = Paths.get(PathConstant.MAIN_RESOURCE, "mapper");
        if (!Files.exists(mapperxmlPath)) {
            try {
                Files.createDirectories(mapperxmlPath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 生成pom.xml文件
     */
    public static void generatePom() {
        File file = new File(PathConstant.PROJECT_ROOT + "pom.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String pomContent = POM_TEMPLATE.formatted(PropertiesReader.getSetting("package.prefix"),
                    PropertiesReader.getSetting("project.name"));
            writer.write(pomContent);
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 生成SpringBoot配置文件
     */
    public static void generateYml() {
        File file = new File(PathConstant.MAIN_RESOURCE, "application.yml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String ymlContent = YML_TEMPLATE.formatted(PropertiesReader.getSetting("db.driver-class-name"),
                    PropertiesReader.getSetting("db.url"),
                    PropertiesReader.getSetting("db.username"),
                    PropertiesReader.getSetting("db.password"),
                    PropertiesReader.getSetting("redis.host"),
                    PropertiesReader.getSetting("redis.port"),
                    PropertiesReader.getSetting("redis.password"),
                    PackageConstant.PACKAGE + ".pojo.po");
            writer.write(ymlContent);
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 生成SprintBoot启动类
     */
    public static void generateApplication() {
        File file = new File(PathConstant.APPLICATION_ROOT, PropertiesReader.getSetting("project.name") + "Application.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String springBootApplicationContent = SPRINGBOOT_APPLICATION_TEMPLATE.formatted(
                    PackageConstant.PACKAGE,
                    PropertiesReader.getSetting("project.name"),
                    PropertiesReader.getSetting("project.name")
            );
            writer.write(springBootApplicationContent);
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 生成基础SpringBoot项目
     */
    public static void generateProject() {
        generateDirectory();
        generatePom();
        generateYml();
        generateApplication();
    }
}
