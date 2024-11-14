package com.zjx.EasySpringBoot.generator;

import com.zjx.EasySpringBoot.constant.PackageConstant;
import com.zjx.EasySpringBoot.constant.PathConstant;
import com.zjx.EasySpringBoot.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ProjectGenerator {
    private static final Logger log = LoggerFactory.getLogger(ProjectGenerator.class);

    // pom.xml文件模板
    private static final String POM_TEMPLATE;
    private static final String YML_TEMPLATE;
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
                  datasource:
                    druid:
                      driver-class-name: %s
                      url: %s
                      username: %s
                      password: %s
                
                mybatis:
                  # mapper配置文件
                  mapper-locations: classpath:mapper/*.xml
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
        File file = new File(PathConstant.POJO + "entity/");
        file.mkdirs();
        file = new File(PathConstant.POJO + "dto/");
        file.mkdirs();
        file = new File(PathConstant.POJO + "vo/");
        file.mkdirs();

        // 创建mapper目录
        file = new File(PathConstant.MAPPER_INTERFACE);
        file.mkdirs();

        // 创建service目录
        file = new File(PathConstant.SERVICE + "/impl/");
        file.mkdirs();

        // 创建controller目录
        file = new File(PathConstant.CONTROLLER);
        file.mkdirs();

        // 创建resources目录
        file = new File(PathConstant.MAIN_RESOURCE);
        file.mkdirs();

        // 创建mapper xml目录
        file = new File(PathConstant.MAPPER_XML);
        file.mkdirs();
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
        File file = new File(PathConstant.MAIN_RESOURCE + "application.yml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String ymlContent = YML_TEMPLATE.formatted(PropertiesReader.getSetting("db.driver.name"),
                    PropertiesReader.getSetting("db.url"),
                    PropertiesReader.getSetting("db.username"),
                    PropertiesReader.getSetting("db.password"),
                    PackageConstant.PACKAGE + ".pojo.entity");
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
