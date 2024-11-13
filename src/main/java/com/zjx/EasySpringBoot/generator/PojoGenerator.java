package com.zjx.EasySpringBoot.generator;

import com.zjx.EasySpringBoot.constant.PackageConstant;
import com.zjx.EasySpringBoot.constant.PathConstant;
import com.zjx.EasySpringBoot.pojo.Field;
import com.zjx.EasySpringBoot.pojo.Table;
import com.zjx.EasySpringBoot.util.FieldToPojoUtil;
import com.zjx.EasySpringBoot.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class PojoGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PojoGenerator.class);

    /**
     * 根据表生成对应实体类
     * @param table
     */
    public static void generateEntity(Table table) {
        String entityName = FieldToPojoUtil.tableNameToEntityName(table.getTableName());

        File file = new File(PathConstant.POJO + "/entity/", entityName + ".java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 包
            writer.write("package " + PackageConstant.PACKAGE + ".pojo.entity;");
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
            writer.write("public class " + entityName + " implements Serializable {");
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
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
