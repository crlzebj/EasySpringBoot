package com.zjx.EasySpringBoot.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Field {
    private String fieldName;
    private String fieldType;
    private String fieldComment;
}
