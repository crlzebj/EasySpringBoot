package com.zjx.EasySpringBoot.generator;

import com.zjx.EasySpringBoot.constant.PathConstant;
import com.zjx.EasySpringBoot.pojo.Table;
import com.zjx.EasySpringBoot.util.FieldToPojoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapperGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MapperGenerator.class);

    private static final String XML_TEMPLATE;

    static {
        XML_TEMPLATE = """
                <?xml version="1.0" encoding="UTF-8" ?>
                <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
                """;
    }

    public static void generateXml(Table table) {
        File file = new File(PathConstant.MAPPER_XML, FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + "Mapper.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(XML_TEMPLATE);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void generateInterface(Table table) {
        File file = new File(PathConstant.MAPPER_INTERFACE, FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + "Mapper.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("public interface " + FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + "Mapper {");
            writer.newLine();
            writer.write("}");
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
