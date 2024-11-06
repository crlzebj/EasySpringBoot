package com.zjx.EasySpringBoot.util;

import java.util.HashMap;
import java.util.Map;

public class FieldToPojoUtil {

    // 存储 Mysql 数据类型和 Java 数据类型的对应关系
    private static final Map<String, String> map = new HashMap<String, String>();

    public static String tableNameToEntityName(String tableName) {
        return null;
    }

    public static String tableNameToDTOName(String tableName) {
        return null;
    }

    public static String fieldNameToJavaName(String fieldName) {
        return null;
    }

    public static String fieldTypeToJavaType(String filedType) {
        return null;
    }

    static {
        map.put("int", "java.lang.Integer");
    }

}
