package com.zjx.EasySpringBoot.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Table {
    private String tableName;
    private List<Field> fields;
    private String tableComment;
}
