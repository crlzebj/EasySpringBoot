package com.zjx.EasySpringBoot.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串转换工具
 */
public class FieldToPojoUtil {
    // 存储 Mysql 数据类型和 Java 数据类型的对应关系
    private static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("int", "Integer");
        map.put("bigint", "Long");
        map.put("varchar", "String");
        map.put("datetime", "Date");
    }

    /**
     * 将表名转换为 Entity 类名
     * @param tableName
     * @return
     */
    public static String tableNameToEntityName(String tableName) {
        String[] splitResult = tableName.split("_");
        String entityName = "";
        for (String s : splitResult) {
            entityName += s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return entityName;
    }

    /**
     * 将表名转换为 DTO 类名
     * @param tableName
     * @return
     */
    public static String tableNameToDTOName(String tableName) {
        String[] splitResult = tableName.split("_");
        String dtoName = "";
        for (String s : splitResult) {
            dtoName += s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        dtoName += "QueryDTO";
        return dtoName;
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
        return map.get(filedType);
    }
}
