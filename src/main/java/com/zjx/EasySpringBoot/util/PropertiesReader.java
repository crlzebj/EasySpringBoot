package com.zjx.EasySpringBoot.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取工具
 */
public class PropertiesReader {

    // 存储配置信息
    private static final Properties properties;

    /**
     * 获取配置
     * @param key
     * @return
     */
    public static String getSetting(String key) {
        return properties.getProperty(key);
    }

    static {
        properties = new Properties();
        try {
            InputStream fis = PropertiesReader.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}