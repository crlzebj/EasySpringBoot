package com.zjx.EasySpringBoot;

import com.zjx.EasySpringBoot.generator.PojoGenerator;
import com.zjx.EasySpringBoot.generator.ProjectGenerator;
import com.zjx.EasySpringBoot.pojo.Table;
import com.zjx.EasySpringBoot.util.TablesReader;

import java.util.Set;

public class Application {
    public static void main(String[] args) {
        ProjectGenerator.generateDirectory();
        ProjectGenerator.generatePom();
        /*
        Set<Table> tableSet = TablesReader.getTableSet();
        for (Table table : tableSet) {
            PojoGenerator.generateEntity(table);
            PojoGenerator.generateDTO(table);
        }
         */
    }
}
