package com.zjx.easyspringboot.constant;

import com.zjx.easyspringboot.util.PropertiesReader;

public class PackageConstant {
    public static final String PACKAGE;

    static {
        PACKAGE = PropertiesReader.getSetting("package.prefix") + "."
                + PropertiesReader.getSetting("project.name").toLowerCase();
    }
}
