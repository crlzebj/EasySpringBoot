package com.zjx.EasySpringBoot.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    private String tableName;
    private String tableComment;
    private List<Field> fields;

    public Table() {
        this.fields = new ArrayList<Field>();
    }
}
