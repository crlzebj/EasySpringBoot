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

public class ServiceGenerator {
    public static Logger log = LoggerFactory.getLogger(ServiceGenerator.class);

    /**
     * 根据表生成对应service接口类
     * @param table
     */
    public static void generateInterface(Table table) {
        String entityName = FieldToPojoUtil.tableNameToEntityName(table.getTableName());

        File file = new File(PathConstant.SERVICE, entityName + "Service.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 包
            writer.write("package " + PackageConstant.PACKAGE + ".service;\n\n");

            // import语句
            writer.write("import " + PackageConstant.PACKAGE + ".pojo.entity."
                    + entityName + ";\n\n");
            writer.write("import java.util.List;\n\n");

            // 接口定义
            writer.write("public interface " + entityName + "Service {\n");
            writer.write("\tvoid insert" + entityName + "(" + entityName + " "
                    + entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + ");\n\n");
            writer.write("\tList<" + entityName + "> select" + entityName + "();\n");

            // 索引对应方法
            Set<Map.Entry<String, List<Field>>> indexes = table.getIndexes().entrySet();
            for (Map.Entry<String, List<Field>> index : indexes) {
                List<Field> indexFields = index.getValue();

                // updateBy方法
                writer.write("\n\tvoid update" + entityName + "By");
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
                writer.write(");\n");

                // deleteBy方法
                writer.write("\n\tvoid delete" + entityName + "By");
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
                        writer.write(");\n");
                    }
                }

                // selectBy方法
                writer.write("\n\t" + entityName + " select" + entityName + "By");
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
                        writer.write(");\n");
                    }
                }
            }
            writer.write("}\n");
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 根据表生成对应service实现类
     * @param table
     */
    public static void generateImpl(Table table) {
        String entityName = FieldToPojoUtil.tableNameToEntityName(table.getTableName());

        File file = new File(PathConstant.SERVICE + "impl/", entityName + "ServiceImpl.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 包
            writer.write("package " + PackageConstant.PACKAGE + ".service.impl;\n\n");

            // import语句
            writer.write("import " + PackageConstant.PACKAGE + ".mapper."
                    + entityName + "Mapper;\n");
            writer.write("import " + PackageConstant.PACKAGE + ".pojo.entity."
                    + entityName + ";\n");
            writer.write("import " + PackageConstant.PACKAGE + ".service." + entityName + "Service;\n");
            writer.write("import org.springframework.beans.factory.annotation.Autowired;\n");
            writer.write("import org.springframework.stereotype.Service;\n\n");
            writer.write("import java.util.List;\n\n");

            // 注解
            writer.write("@Service\n");

            // 实现类定义
            writer.write("public class " + entityName + "ServiceImpl implements "
                    + entityName + "Service {\n");

            // mapper接口
            writer.write("\t@Autowired\n\tprivate " + entityName + "Mapper "
                    + entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + "Mapper;\n\n");

            // insert方法
            writer.write("\t@Override\n");
            writer.write("\tpublic void insert" + entityName + "(" + entityName + " "
                    + entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + ") {\n");
            writer.write("\t\t" + entityName.substring(0, 1).toLowerCase() + entityName.substring(1)
                    + "Mapper.insert" + entityName + "(" + entityName.substring(0, 1).toLowerCase()
                    + entityName.substring(1) + ");\n");
            writer.write("\t}\n\n");

            // select方法
            writer.write("\t@Override\n");
            writer.write("\tpublic List<" + entityName + "> select" + entityName + "() {\n");
            writer.write("\t\treturn " + entityName.substring(0, 1).toLowerCase() + entityName.substring(1)
                    + "Mapper.select" + entityName + "();\n");
            writer.write("\t}\n");

            // 索引对应方法
            Set<Map.Entry<String, List<Field>>> indexes = table.getIndexes().entrySet();
            for (Map.Entry<String, List<Field>> index : indexes) {
                List<Field> indexFields = index.getValue();

                // updateBy方法
                writer.write("\n\t@Override\n\tpublic void update" + entityName + "By");
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
                        + "Mapper.update" + entityName + "By");
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
                writer.write("\n\t@Override\n\tpublic void delete" + entityName + "By");
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
                        + "Mapper.delete" + entityName + "By");
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
                writer.write("\n\t@Override\n\tpublic " + entityName + " select" + entityName + "By");
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
                        + "Mapper.select" + entityName + "By");
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
