package com.zjx.EasySpringBoot.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    private String tableName;
    private String tableComment;
    private List<Field> fields;
    private boolean hasDecimalType;
    private boolean hasDateTimeType;

    public Table() {
        fields = new ArrayList<Field>();
        hasDecimalType = false;
        hasDateTimeType = false;
    }
}
