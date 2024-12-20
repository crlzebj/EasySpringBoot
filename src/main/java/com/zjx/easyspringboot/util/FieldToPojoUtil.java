package com.zjx.easyspringboot.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串转换工具
 */
public class FieldToPojoUtil {
    // 存储 Mysql 数据类型和 Java 数据类型的对应关系
    private static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("tinyint", "Integer");
        map.put("int", "Integer");
        map.put("bigint", "Long");
        map.put("decimal", "BigDecimal");
        map.put("varchar", "String");
        map.put("datetime", "LocalDateTime");
    }

    /**
     * 将表名转换为 Entity 类名
     * @param tableName
     * @return
     */
    public static String tableNameToPoName(String tableName) {
        String[] splitResult = tableName.split("_");
        String poName = "";
        for (String s : splitResult) {
            poName += s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return poName;
    }

    /**
     * 将 MySQL 字段名转换为 Java 属性名
     * @param fieldName
     * @return
     */
    public static String fieldNameToJavaName(String fieldName) {
        String[] splitResult = fieldName.split("_");
        String javaName = splitResult[0];
        for (int i = 1; i < splitResult.length; i++) {
            javaName += splitResult[i].substring(0, 1).toUpperCase() + splitResult[i].substring(1);
        }
        return javaName;
    }

    /**
     * 将 MySQL 字段类型转换为 Java 属性类型
     * @param filedType
     * @return
     */
    public static String fieldTypeToJavaType(String filedType) {
        int idx = filedType.indexOf("(");
        if (idx != -1) {
            filedType = filedType.substring(0, idx);
        }
        return map.get(filedType);
    }
}
