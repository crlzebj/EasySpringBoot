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

public class ControllerGenerator {
    private static final Logger log = LoggerFactory.getLogger(ControllerGenerator.class);

    /**
     * 根据表生成对应controller类
     * @param table
     */
    public static void generateController(Table table) {
        String entityName = FieldToPojoUtil.tableNameToEntityName(table.getTableName());

        File file = new File(PathConstant.CONTROLLER, entityName + "Controller.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 包
            writer.write("package " + PackageConstant.PACKAGE + ".controller;\n\n");

            // import语句
            writer.write("import " + PackageConstant.PACKAGE + ".service." + entityName + "Service;\n");
            writer.write("import " + PackageConstant.PACKAGE + ".pojo.entity."
                    + entityName + ";\n");
            writer.write("import org.springframework.beans.factory.annotation.Autowired;\n");
            writer.write("import org.springframework.web.bind.annotation.*;\n\n");
            writer.write("import java.util.List;\n\n");

            // 注解
            writer.write("@RestController\n");
            writer.write("@RequestMapping(\"/" + entityName.substring(0, 1).toLowerCase()
                    + entityName.substring(1) + "\")\n");

            // 实现类定义
            writer.write("public class " + entityName + "Controller {\n");

            // mapper接口
            writer.write("\t@Autowired\n\tprivate " + entityName + "Service "
                    + entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + "Service;\n\n");

            // insert方法
            // writer.write("\t@RequestMapping()\n");
            writer.write("\tpublic void insert" + entityName + "(" + entityName + " "
                    + entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + ") {\n");
            writer.write("\t\t" + entityName.substring(0, 1).toLowerCase() + entityName.substring(1)
                    + "Service.insert" + entityName + "(" + entityName.substring(0, 1).toLowerCase()
                    + entityName.substring(1) + ");\n");
            writer.write("\t}\n\n");

            // select方法
            // writer.write("\t@RequestMapping()\n");
            writer.write("\tpublic List<" + entityName + "> select" + entityName + "() {\n");
            writer.write("\t\treturn " + entityName.substring(0, 1).toLowerCase() + entityName.substring(1)
                    + "Service.select" + entityName + "();\n");
            writer.write("\t}\n");

            // 索引对应方法
            Set<Map.Entry<String, List<Field>>> indexes = table.getIndexes().entrySet();
            for (Map.Entry<String, List<Field>> index : indexes) {
                List<Field> indexFields = index.getValue();

                // updateBy方法
                // writer.write("\n\t@RequestMapping()\n");
                writer.write("\tpublic void update" + entityName + "By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toUpperCase() + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write("And");
                    } else {
                        writer.write("(");
                    }
                }
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaType = FieldToPojoUtil.fieldTypeToJavaType(indexFields.get(fieldIdxCount).getFieldType());
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaType + " " + javaName.substring(0, 1).toLowerCase()
                            + javaName.substring(1));
                    writer.write(", ");
                }
                writer.write(entityName + " " + entityName.substring(0, 1).toLowerCase()
                        + entityName.substring(1));
                writer.write(") {\n");
                writer.write("\t\t" + entityName.substring(0, 1).toLowerCase() + entityName.substring(1)
                        + "Service.update" + entityName + "By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toUpperCase() + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write("And");
                    }
                }
                writer.write("(");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toLowerCase()
                            + javaName.substring(1));
                    writer.write(", ");
                }
                writer.write(entityName.substring(0, 1).toLowerCase()
                        + entityName.substring(1) + ");\n\t}\n");

                // deleteBy方法
                // writer.write("\n\t@RequestMapping()\n");
                writer.write("\tpublic void delete" + entityName + "By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toUpperCase() + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write("And");
                    } else {
                        writer.write("(");
                    }
                }
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaType = FieldToPojoUtil.fieldTypeToJavaType(indexFields.get(fieldIdxCount).getFieldType());
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaType + " " + javaName.substring(0, 1).toLowerCase()
                            + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write(", ");
                    } else {
                        writer.write(") {\n");
                    }
                }
                writer.write("\t\t" + entityName.substring(0, 1).toLowerCase() + entityName.substring(1)
                        + "Service.delete" + entityName + "By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toUpperCase() + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write("And");
                    }
                }
                writer.write("(");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toLowerCase()
                            + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write(", ");
                    } else {
                        writer.write(");\n\t}\n");
                    }
                }

                // selectBy方法
                // writer.write("\n\t@RequestMapping()\n");
                writer.write("\tpublic " + entityName + " select" + entityName + "By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toUpperCase() + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write("And");
                    } else {
                        writer.write("(");
                    }
                }
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaType = FieldToPojoUtil.fieldTypeToJavaType(indexFields.get(fieldIdxCount).getFieldType());
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaType + " " + javaName.substring(0, 1).toLowerCase()
                            + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write(", ");
                    } else {
                        writer.write(") {\n");
                    }
                }
                writer.write("\t\treturn " + entityName.substring(0, 1).toLowerCase() + entityName.substring(1)
                        + "Service.select" + entityName + "By");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toUpperCase() + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write("And");
                    }
                }
                writer.write("(");
                for (int fieldIdxCount = 0; fieldIdxCount < indexFields.size(); fieldIdxCount++) {
                    String javaName = FieldToPojoUtil.fieldNameToJavaName(indexFields.get(fieldIdxCount).getFieldName());
                    writer.write(javaName.substring(0, 1).toLowerCase()
                            + javaName.substring(1));
                    if (fieldIdxCount < indexFields.size() - 1) {
                        writer.write(", ");
                    } else {
                        writer.write(");\n\t}\n");
                    }
                }
            }
            writer.write("}\n");
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
