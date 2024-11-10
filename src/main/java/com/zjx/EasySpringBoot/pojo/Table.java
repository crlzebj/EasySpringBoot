package com.zjx.EasySpringBoot.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Table {
    private String tableName;
    private String tableComment;
    private List<Field> fields;
    private Map<String, List<Field>> indexes;
    private boolean hasDecimalType;
    private boolean hasDateTimeType;

    public Table() {
        fields = new ArrayList<Field>();
        indexes = new HashMap<String, List<Field>>();
        hasDecimalType = false;
        hasDateTimeType = false;
    }
}
