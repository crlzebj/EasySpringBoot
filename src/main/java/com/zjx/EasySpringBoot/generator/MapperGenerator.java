package com.zjx.EasySpringBoot.generator;

import com.zjx.EasySpringBoot.constant.PackageConstant;
import com.zjx.EasySpringBoot.constant.PathConstant;
import com.zjx.EasySpringBoot.pojo.Field;
import com.zjx.EasySpringBoot.pojo.Table;
import com.zjx.EasySpringBoot.util.FieldToPojoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapperGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MapperGenerator.class);

    // xml文件模板
    private static final String XML_TEMPLATE;

    static {
        XML_TEMPLATE = """
                <?xml version="1.0" encoding="UTF-8" ?>
                <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
                <mapper namespace="%s">
                %s
                </mapper>
                """;
    }

    /**
     * 根据表生成对应mapper xml文件
     * @param table
     */
    public static void generateXml(Table table) {
        File file = new File(PathConstant.MAPPER_XML,
                FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + "Mapper.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<Field> fields = table.getFields();
            StringBuilder builder = new StringBuilder();

            // insert
            builder.append("\t<insert id=\"insert").append(FieldToPojoUtil.tableNameToEntityName(table.getTableName()))
                    .append("\">\n");
            builder.append("\t\tinsert into ").append(table.getTableName()).append(" values (");
            for (int fieldIdxCount = 0; fieldIdxCount < fields.size(); fieldIdxCount++) {
                builder.append("#{").append(FieldToPojoUtil.fieldNameToJavaName(fields.get(fieldIdxCount).getFieldName()))
                        .append("}");
                if (fieldIdxCount < fields.size() - 1) {
                    builder.append(",");
                } else {
                    builder.append(");\n\t</insert>\n\n");
                }
            }

            // select
            builder.append("\t<select id=\"select").append(FieldToPojoUtil.tableNameToEntityName(table.getTableName()))
                    .append("\" returnType=\"").append(PackageConstant.PACKAGE).append(".pojo.entity.")
                    .append(FieldToPojoUtil.tableNameToEntityName(table.getTableName())).append("\">\n");
            builder.append("\t\tselect * from ").append(table.getTableName()).append(";\n")
                    .append("\t</select>\n\n");

            // 索引对应查询
            Set<Map.Entry<String, List<Field>>> indexes = table.getIndexes().entrySet();
            for (Map.Entry<String, List<Field>> index : indexes) {
                List<Field> indexFields = index.getValue();

                // updateBy
                builder.append("\t<update id=\"update").append(FieldToPojoUtil.tableNameToEntityName(table.getTableName()))
                        .append("By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    if (fieldIdxCount > 0) {
                        builder.append("And");
                    }
                    builder.append(FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName())
                            .substring(0, 1).toUpperCase() +
                            FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName())
                                    .substring(1));
                }
                builder.append("\">\n");
                builder.append("\t\tupdate ").append(table.getTableName()).append(" set ");
                for (int fieldIdxCount = 0; fieldIdxCount < fields.size(); fieldIdxCount++) {
                    builder.append(fields.get(fieldIdxCount).getFieldName()).append("=#{")
                            .append(FieldToPojoUtil.fieldNameToJavaName(fields.get(fieldIdxCount).getFieldName()))
                            .append("}");
                    if (fieldIdxCount < fields.size() - 1) {
                       builder.append(",");
                    } else {
                        builder.append(";\n\t</update>\n\n");
                    }
                }

                // deleteBy
                builder.append("\t<delete id=\"delete").append(FieldToPojoUtil.tableNameToEntityName(table.getTableName()))
                        .append("By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    if (fieldIdxCount > 0) {
                        builder.append("And");
                    }
                    builder.append(FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName())
                            .substring(0, 1).toUpperCase() +
                            FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName())
                                    .substring(1));
                }
                builder.append("\">\n");
                builder.append("\t\tdelete from ").append(table.getTableName()).append(" ");
                builder.append(";\n\t</delete>\n\n");

                // selectBy
                builder.append("\t<select id=\"select").append(FieldToPojoUtil.tableNameToEntityName(table.getTableName()))
                        .append("By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    if (fieldIdxCount > 0) {
                        builder.append("And");
                    }
                    builder.append(FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName())
                            .substring(0, 1).toUpperCase() +
                            FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName())
                                    .substring(1));
                }
                builder.append("\" returnType=\"").append(PackageConstant.PACKAGE).append(".pojo.entity.")
                        .append(FieldToPojoUtil.tableNameToEntityName(table.getTableName())).append("\">\n");
                builder.append("\t\tselect * from ").append(table.getTableName());
                builder.append(";\n\t</select>\n\n");
            }

            String xmlContent = String.format(XML_TEMPLATE, PackageConstant.PACKAGE + ".mapper." +
                            FieldToPojoUtil.tableNameToEntityName(table.getTableName()) + "Mapper",
                    builder.toString());

            // 写入文件
            writer.write(xmlContent);
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
