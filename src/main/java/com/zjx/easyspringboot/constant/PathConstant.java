package com.zjx.easyspringboot.constant;

import com.zjx.easyspringboot.util.PropertiesReader;

public class PathConstant {
    // 项目根目录
    public static final String PROJECT_ROOT;
    // main java路径
    public static final String MAIN_JAVA;
    // 应用路径（SpringBoot启动类扫描路径）
    public static final String APPLICATION_ROOT;
    // main resources路径
    public static final String MAIN_RESOURCE;

    static {
        PROJECT_ROOT = PropertiesReader.getSetting("project.path") +
                PropertiesReader.getSetting("project.name") + "/";
        MAIN_JAVA = PROJECT_ROOT + "src/main/java/";
        APPLICATION_ROOT = MAIN_JAVA + PropertiesReader.getSetting("package.prefix").replace(".", "/") +
                "/" + PropertiesReader.getSetting("project.name").toLowerCase();
        MAIN_RESOURCE = PROJECT_ROOT + "src/main/resources";
    }
}
