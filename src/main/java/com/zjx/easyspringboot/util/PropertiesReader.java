package com.zjx.easyspringboot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取工具
 */
public class PropertiesReader {
    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
    // 存储配置信息
    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            InputStream fis = PropertiesReader.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(fis);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 获取配置
     * @param key
     * @return
     */
    public static String getSetting(String key) {
        return properties.getProperty(key);
    }
}
