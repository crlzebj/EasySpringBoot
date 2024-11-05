package com.zjx.EasySpringBoot.Util;

import java.util.HashMap;
import java.util.Map;

public class PojoConvertUtil {
    private static final Map<String, String> map = new HashMap<String, String>();

    public static String convertTableName(String tableName) {
        return tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
    }

    public static String convertFieldName(String FieldName) {
        return FieldName.substring(0, 1).toUpperCase() + FieldName.substring(1);
    }

    public static String convertFieldType(String FiledType) {
        return FiledType.substring(0, 1).toUpperCase() + FiledType.substring(1);
    }

    static {
        map.put("int", "java.lang.Integer");
    }

}
