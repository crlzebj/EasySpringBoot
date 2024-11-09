package com.zjx.EasySpringBoot;

import com.zjx.EasySpringBoot.generator.PojoGenerator;
import com.zjx.EasySpringBoot.generator.ProjectGenerator;
import com.zjx.EasySpringBoot.pojo.Table;
import com.zjx.EasySpringBoot.util.TablesReader;

import java.util.Set;

/**
 * 项目入口
 */
public class Application {
    public static void main(String[] args) {
        ProjectGenerator.generateProject();
        Set<Table> tableSet = TablesReader.getTableSet();
        for (Table table : tableSet) {
            PojoGenerator.generateEntity(table);
            PojoGenerator.generateDTO(table);
        }
    }
}
