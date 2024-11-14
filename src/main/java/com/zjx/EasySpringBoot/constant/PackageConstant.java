package com.zjx.EasySpringBoot.constant;

import com.zjx.EasySpringBoot.util.PropertiesReader;

public class PackageConstant {
    public static final String PACKAGE;

    static {
        PACKAGE = PropertiesReader.getSetting("package.prefix") + "."
                + PropertiesReader.getSetting("project.name").toLowerCase();
    }
}
