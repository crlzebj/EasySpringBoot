package com.zjx.EasySpringBoot.constant;

import com.zjx.EasySpringBoot.util.PropertiesReader;

public class PathConstant {
    // 项目根目录
    public static final String PROJECT_ROOT;

    public static final String MAIN_JAVA;
    // pojo生成路径
    public static final String POJO;
    // mapper接口生成路径
    public static final String MAPPER_INTERFACE;
    // service类生成路径
    public static final String SERVICE;
    // controller类生成路径
    public static final String CONTROLLER;

    public static final String MAIN_RESOURCE;
    // mapper xml文件生成路径
    public static final String MAPPER_XML;

    static {
        PROJECT_ROOT = PropertiesReader.getSetting("project.path") +
                PropertiesReader.getSetting("project.name") + "/";
        MAIN_JAVA = PROJECT_ROOT + "src/main/java/";
        POJO = MAIN_JAVA + PropertiesReader.getSetting("package.prefix").replace(".", "/") +
                "/" + PropertiesReader.getSetting("project.name") + "/pojo/";
        MAPPER_INTERFACE = MAIN_JAVA + PropertiesReader.getSetting("package.prefix").replace(".", "/") +
                "/" + PropertiesReader.getSetting("project.name") + "/mapper/";
        SERVICE = MAIN_JAVA + PropertiesReader.getSetting("package.prefix").replace(".", "/") +
                "/" + PropertiesReader.getSetting("project.name") + "/service/";
        CONTROLLER = MAIN_JAVA + PropertiesReader.getSetting("package.prefix").replace(".", "/") +
                "/" + PropertiesReader.getSetting("project.name") + "/controller/";

        MAIN_RESOURCE = PROJECT_ROOT + "src/main/resources/";
        MAPPER_XML = MAIN_RESOURCE + "mapper/";
    }
}
