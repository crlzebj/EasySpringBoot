package com.zjx.easyspringboot.generator;

import com.zjx.easyspringboot.constant.PackageConstant;
import com.zjx.easyspringboot.constant.PathConstant;
import com.zjx.easyspringboot.pojo.Field;
import com.zjx.easyspringboot.pojo.Table;
import com.zjx.easyspringboot.util.FieldToPojoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapperGenerator {
    private static final Logger log = LoggerFactory.getLogger(MapperGenerator.class);

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
        String poName = FieldToPojoUtil.tableNameToPoName(table.getTableName());

        File file = new File(PathConstant.MAIN_RESOURCE + "/mapper", poName + "Mapper.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<Field> fields = table.getFields();
            StringBuilder builder = new StringBuilder();

            // insert
            builder.append("\t<insert id=\"insert\">\n")
                    .append("\t\tinsert into ").append(table.getTableName()).append(" values\n\t\t(");
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
            builder.append("\t<select id=\"select\" resultType=\"").append(poName).append("\">\n")
                    .append("\t\tselect * from ").append(table.getTableName()).append("\n")
                    .append("\t\t<where>\n");
            for (int fieldIdxCount = 0; fieldIdxCount < fields.size(); fieldIdxCount++) {
                String javaName = FieldToPojoUtil.fieldNameToJavaName(fields.get(fieldIdxCount).getFieldName());
                builder.append("\t\t\t<if test=\"").append(javaName).append("!=null\">\n\t\t\t\tand ")
                        .append(fields.get(fieldIdxCount).getFieldName()).append("=#{")
                        .append(javaName).append("}\n\t\t\t</if>\n");
            }
            builder.append("\t\t</where>;\n\t</select>\n\n");

            // count
            builder.append("\t<select id=\"count\" resultType=\"Integer\">\n")
                    .append("\t\tselect count(*) from ").append(table.getTableName()).append("\n")
                    .append("\t\t<where>\n");
            for (int fieldIdxCount = 0; fieldIdxCount < fields.size(); fieldIdxCount++) {
                String javaName = FieldToPojoUtil.fieldNameToJavaName(fields.get(fieldIdxCount).getFieldName());
                builder.append("\t\t\t<if test=\"").append(javaName).append("!=null\">\n\t\t\t\tand ")
                        .append(fields.get(fieldIdxCount).getFieldName()).append("=#{")
                        .append(javaName).append("}\n\t\t\t</if>\n");
            }
            builder.append("\t\t</where>;\n\t</select>\n\n");

            // 分页查询select
            builder.append("\t<select id=\"selectPage\" resultType=\"").append(poName).append("\">\n")
                    .append("\t\tselect * from ").append(table.getTableName()).append("\n")
                    .append("\t\t<where>\n");
            for (int fieldIdxCount = 0; fieldIdxCount < fields.size(); fieldIdxCount++) {
                String javaName = FieldToPojoUtil.fieldNameToJavaName(fields.get(fieldIdxCount).getFieldName());
                builder.append("\t\t\t<if test=\"").append(poName.substring(0, 1).toLowerCase())
                        .append(poName.substring(1)).append(".")
                        .append(javaName).append("!=null\">\n\t\t\t\tand ")
                        .append(fields.get(fieldIdxCount).getFieldName()).append("=#{")
                        .append(poName.substring(0, 1).toLowerCase())
                        .append(poName.substring(1)).append(".")
                        .append(javaName).append("}\n\t\t\t</if>\n");
            }
            builder.append("\t\t</where>\n\t\tlimit #{offset},#{count};\n\t</select>\n\n");

            // 索引对应查询
            Set<Map.Entry<String, List<Field>>> indexes = table.getIndexes().entrySet();
            for (Map.Entry<String, List<Field>> index : indexes) {
                List<Field> indexFields = index.getValue();

                // updateBy
                builder.append("\t<update id=\"updateBy");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    if (fieldIdxCount > 0) {
                        builder.append("And");
                    }
                    builder.append(javaName.substring(0, 1).toUpperCase()).append(javaName.substring(1));
                }
                builder.append("\">\n");
                builder.append("\t\tupdate ").append(table.getTableName()).append("\n\t\t<set>\n");
                for (int fieldIdxCount = 0; fieldIdxCount < fields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(fields.get(fieldIdxCount).getFieldName());
                    builder.append("\t\t\t<if test=\"").append(poName.substring(0, 1).toLowerCase())
                            .append(poName.substring(1)).append(".")
                            .append(javaName).append("!=null\">\n").append("\t\t\t\t")
                            .append(fields.get(fieldIdxCount).getFieldName()).append("=#{")
                            .append(poName.substring(0, 1).toLowerCase())
                            .append(poName.substring(1)).append(".")
                            .append(javaName).append("},\n\t\t\t</if>\n");
                }
                builder.append("\t\t</set>\n\t\twhere ");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    builder.append(indexFields.get(fieldIdxCount).getFieldName()).append("=#{")
                            .append(FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName()))
                            .append("}");
                    if (fieldIdxCount < indexFields.size() - 1) {
                        builder.append(" and ");
                    }
                }
                builder.append(";\n\t</update>\n\n");

                // deleteBy
                builder.append("\t<delete id=\"deleteBy");
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
                builder.append("\n\t\twhere ");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    builder.append(indexFields.get(fieldIdxCount).getFieldName()).append("=#{")
                            .append(FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName()))
                            .append("}");
                    if (fieldIdxCount < indexFields.size() - 1) {
                        builder.append(" and ");
                    }
                }
                builder.append(";\n\t</delete>\n\n");

                // selectBy
                builder.append("\t<select id=\"selectBy");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    if (fieldIdxCount > 0) {
                        builder.append("And");
                    }
                    builder.append(FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName())
                            .substring(0, 1).toUpperCase() +
                            FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName())
                                    .substring(1));
                }
                builder.append("\" resultType=\"").append(poName).append("\">\n");
                builder.append("\t\tselect * from ").append(table.getTableName());
                builder.append("\n\t\twhere ");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    builder.append(indexFields.get(fieldIdxCount).getFieldName()).append("=#{")
                            .append(FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName()))
                            .append("}");
                    if (fieldIdxCount < indexFields.size() - 1) {
                        builder.append(" and ");
                    }
                }
                builder.append(";\n\t</select>\n\n");
            }

            String xmlContent = String.format(XML_TEMPLATE,
                    PackageConstant.PACKAGE + ".mapper." + poName + "Mapper",
                    builder.toString());

            // 写入文件
            writer.write(xmlContent);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void generateInterface(Table table) {
        String poName = FieldToPojoUtil.tableNameToPoName(table.getTableName());

        File file = new File(PathConstant.APPLICATION_ROOT + "/mapper", poName + "Mapper.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 包
            writer.write("package " + PackageConstant.PACKAGE + ".mapper;");
            writer.newLine();
            writer.newLine();

            // import语句
            writer.write("import " + PackageConstant.PACKAGE + ".pojo.po." + poName + ";");
            writer.newLine();
            writer.write("import org.apache.ibatis.annotations.Mapper;");
            writer.newLine();
            writer.write("import org.apache.ibatis.annotations.Param;");
            writer.newLine();
            writer.newLine();
            writer.write("import java.util.List;");
            writer.newLine();
            writer.newLine();

            // 注解
            writer.write("@Mapper");
            writer.newLine();
            writer.write("public interface " + poName + "Mapper {");
            writer.newLine();

            // insert方法
            writer.write("\tvoid insert(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ");");
            writer.newLine();
            writer.newLine();

            // select方法
            writer.write("\tList<" + poName + "> select(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ");");
            writer.newLine();
            writer.newLine();

            // count方法
            writer.write("\tInteger count(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ");");
            writer.newLine();
            writer.newLine();

            // 分页查询select
            writer.write("\tList<" + poName + "> selectPage(@Param(\""
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + "\") " + poName
                    + " " + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + ", @Param(\"offset\") Integer offset, @Param(\"count\") Integer count);");
            writer.newLine();

            // 索引对应方法
            Set<Map.Entry<String, List<Field>>> indexes = table.getIndexes().entrySet();
            for (Map.Entry<String, List<Field>> index : indexes) {
                List<Field> indexFields = index.getValue();
                StringBuilder builder = new StringBuilder();

                // updateBy方法
                builder.append("\n\tvoid updateBy");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());

                    builder.append(javaName.substring(0, 1).toUpperCase()).append(javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        builder.append("And");
                    }
                }
                builder.append("(");
                for (Field field : indexFields) {
                    String javaType = FieldToPojoUtil.fieldTypeToJavaType(field.getFieldType());
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(field.getFieldName());
                    builder.append("@Param(\"").append(javaName).append("\") ").
                            append(javaType).append(" ").append(javaName).append(", ");
                }
                builder.append("@Param(\"").append(poName.substring(0, 1).toLowerCase())
                        .append(poName.substring(1)).append("\") ").append(poName).append(" ")
                        .append(poName.substring(0, 1).toLowerCase()).append(poName.substring(1))
                        .append(");\n");

                // deleteBy方法
                builder.append("\n\tvoid deleteBy");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());

                    builder.append(javaName.substring(0, 1).toUpperCase()).append(javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        builder.append("And");
                    }
                }
                builder.append("(");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    String javaType = FieldToPojoUtil.fieldTypeToJavaType(indexFields.get(fieldIdxCount).getFieldType());
                    builder.append("@Param(\"").append(javaName).append("\") ")
                            .append(javaType).append(" ").append(javaName);
                    if (fieldIdxCount < indexFields.size() - 1) {
                        builder.append(", ");
                    } else {
                        builder.append(");\n");
                    }
                }

                // selectBy方法
                builder.append("\n\t").append(poName).append(" selectBy");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());

                    builder.append(javaName.substring(0, 1).toUpperCase()).append(javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        builder.append("And");
                    }
                }
                builder.append("(");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    String javaType = FieldToPojoUtil.fieldTypeToJavaType(indexFields.get(fieldIdxCount).getFieldType());
                    builder.append("@Param(\"").append(javaName).append("\") ")
                            .append(javaType).append(" ").append(javaName);
                    if (fieldIdxCount < indexFields.size() - 1) {
                        builder.append(", ");
                    } else {
                        builder.append(");\n");
                    }
                }

                writer.write(builder.toString());
            }
            writer.write("}");
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
