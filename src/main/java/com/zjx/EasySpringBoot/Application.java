package com.zjx.EasySpringBoot;

import com.zjx.EasySpringBoot.generator.*;
import com.zjx.EasySpringBoot.pojo.Table;
import com.zjx.EasySpringBoot.util.TablesReader;

import java.util.Set;

/**
 * 项目入口
 */
public class Application {
    public static void main(String[] args) {
        // 生成基本SpringBoot项目
        ProjectGenerator.generateProject();

        Set<Table> tableSet = TablesReader.getTableSet();

        for (Table table : tableSet) {
            PojoGenerator.generateEntity(table);
            MapperGenerator.generateXml(table);
            MapperGenerator.generateInterface(table);
            ServiceGenerator.generateInterface(table);
            ServiceGenerator.generateImpl(table);
            ControllerGenerator.generateController(table);
        }
    }
}
