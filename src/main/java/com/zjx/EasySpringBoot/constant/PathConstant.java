package com.zjx.EasySpringBoot.constant;

import com.zjx.EasySpringBoot.util.PropertiesReader;

public class PathConstant {
    public static final String MAIN_JAVA;
    public static final String MAIN_RESOURCE;
    // mapper xml文件生成路径
    public static final String MAPPER_XML;
    // mapper接口文件生成路径
    public static final String MAPPER_INTERFACE;

    static {
        MAIN_JAVA = PropertiesReader.getSetting("project.path") + PropertiesReader.getSetting("project.name") +
                    "/src/main/java/";
        MAIN_RESOURCE = PropertiesReader.getSetting("project.path") + PropertiesReader.getSetting("project.name") +
                        "/src/main/resources/";
        MAPPER_XML = MAIN_RESOURCE + "mapper/";
        MAPPER_INTERFACE = MAIN_JAVA + PropertiesReader.getSetting("package.prefix").replace(".", "/") +
                           "/" + PropertiesReader.getSetting("project.name") + "/mapper/";
    }
}
