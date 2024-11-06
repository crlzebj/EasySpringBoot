package com.zjx.EasySpringBoot;

import com.zjx.EasySpringBoot.util.FieldToPojoUtil;

public class Test {
    public static void main(String[] args) {
        System.out.println(FieldToPojoUtil.tableNameToEntityName("tb_employee"));
        System.out.println(FieldToPojoUtil.tableNameToDTOName("tb_employee"));
        System.out.println(FieldToPojoUtil.fieldNameToJavaName("tb_employee"));
        System.out.println(FieldToPojoUtil.fieldTypeToJavaType("bigint"));
    }
}
