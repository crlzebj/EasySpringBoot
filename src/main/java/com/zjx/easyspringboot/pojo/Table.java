package com.zjx.easyspringboot.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Table {
    // MySQL 表名
    private String tableName;

    // MySQL 表注释
    private String tableComment;

    private List<Field> fields;
    private Map<String, List<Field>> indexes;

    // 表中是否含有Decimal数据类型
    private boolean hasDecimalType;

    // 表中是否含有DateTime数据类型
    private boolean hasDateTimeType;

    public Table() {
        fields = new ArrayList<Field>();
        indexes = new HashMap<String, List<Field>>();
        hasDecimalType = false;
        hasDateTimeType = false;
    }
}
