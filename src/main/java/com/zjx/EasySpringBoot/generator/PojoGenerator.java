package com.zjx.EasySpringBoot.generator;

import com.zjx.EasySpringBoot.pojo.Field;
import com.zjx.EasySpringBoot.pojo.Table;
import com.zjx.EasySpringBoot.util.FieldToPojoUtil;
import com.zjx.EasySpringBoot.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class PojoGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PojoGenerator.class);
    private static final String ENTITY_PATH;
    private static final String DTO_PATH;

    static {
        ENTITY_PATH = PropertiesReader.getSetting("project.path") + PropertiesReader.getSetting("project.name") +
                "/src/main/java/" + PropertiesReader.getSetting("package.prefix").replace(".", "/") +
                "/" + PropertiesReader.getSetting("project.name") + "/pojo/entity";
        DTO_PATH = ENTITY_PATH.replace("/entity", "/dto");
    }

    /**
     * 根据表生成对应实体类
     * @param table
     */
    public static void generateEntity(Table table) {
        File file = new File(ENTITY_PATH + "/" + FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + ".java");
        if (file.exists()) {
            logger.info("{} 文件已存在", FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + ".java");
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            // 包
            writer.write("package " + PropertiesReader.getSetting("package.prefix") +
                         "." + PropertiesReader.getSetting("project.name") + ".pojo.entity;");
            writer.newLine();
            writer.newLine();

            // 头文件
            writer.write("import java.io.Serializable;");
            writer.newLine();
            if (table.isHasDecimalType()) {
                writer.write("import java.math.BigDecimal;");
                writer.newLine();
            }
            if(table.isHasDateTimeType()) {
                writer.write("import java.time.LocalDateTime;");
                writer.newLine();
            }
            writer.write("import lombok.Data;");
            writer.newLine();
            writer.newLine();

            // 类注释
            writer.write("/**");
            writer.newLine();
            writer.write(" * @author " + PropertiesReader.getSetting("project.author"));
            writer.newLine();
            writer.write(" * " + table.getTableComment());
            writer.newLine();
            writer.write(" */");
            writer.newLine();

            // 注解
            writer.write("@Data");
            writer.newLine();

            // 类定义
            writer.write("public class " + FieldToPojoUtil.tableNameToEntityName(table.getTableName()) +
                         " implements Serializable {");
            writer.newLine();
            for(int i = 0; i < table.getFields().size(); i++) {
                Field field = table.getFields().get(i);
                writer.write("    // " + field.getFieldComment());
                writer.newLine();
                writer.write("    private " + FieldToPojoUtil.fieldTypeToJavaType(field.getFieldType()) +
                             " " + FieldToPojoUtil.fieldNameToJavaName(field.getFieldName()) + ";");
                writer.newLine();
            }
            writer.write("}");
            writer.newLine();
            writer.flush();
            writer.close();
            logger.info("{} 文件生成成功", FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + ".java");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 根据表生成对应 DTO
     * @param table
     */
    public static void generateDTO(Table table) {
        File file = new File(DTO_PATH + "/" + FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + "QueryDTO.java");
        if (file.exists()) {
            logger.info("{} 文件已存在", FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + "QueryDTO.java");
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            // 包
            writer.write("package " + PropertiesReader.getSetting("package.prefix") +
                    "." + PropertiesReader.getSetting("project.name") + ".pojo.dto;");
            writer.newLine();
            writer.newLine();

            // 头文件
            writer.write("import java.io.Serializable;");
            writer.newLine();
            if (table.isHasDecimalType()) {
                writer.write("import java.math.BigDecimal;");
                writer.newLine();
            }
            if(table.isHasDateTimeType()) {
                writer.write("import java.time.LocalDateTime;");
                writer.newLine();
            }
            writer.write("import lombok.Data;");
            writer.newLine();
            writer.newLine();

            // 类注释
            writer.write("/**");
            writer.newLine();
            writer.write(" * @author " + PropertiesReader.getSetting("project.author"));
            writer.newLine();
            writer.write(" * " + table.getTableComment());
            writer.newLine();
            writer.write(" */");
            writer.newLine();

            // 注解
            writer.write("@Data");
            writer.newLine();

            // 类定义
            writer.write("public class " + FieldToPojoUtil.tableNameToEntityName(table.getTableName()) +
                    "QueryDTO implements Serializable {");
            writer.newLine();
            for(int i = 0; i < table.getFields().size(); i++) {
                Field field = table.getFields().get(i);
                writer.write("    // " + field.getFieldComment());
                writer.newLine();
                writer.write("    private " + FieldToPojoUtil.fieldTypeToJavaType(field.getFieldType()) +
                        " " + FieldToPojoUtil.fieldNameToJavaName(field.getFieldName()) + ";");
                writer.newLine();
            }
            writer.write("}");
            writer.newLine();
            writer.flush();
            writer.close();
            logger.info("{} 文件生成成功", FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + "QueryDTO.java");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
