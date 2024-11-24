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

public class ServiceGenerator {
    public static Logger log = LoggerFactory.getLogger(ServiceGenerator.class);

    /**
     * 根据表生成对应service接口类
     * @param table
     */
    public static void generateInterface(Table table) {
        String poName = FieldToPojoUtil.tableNameToPoName(table.getTableName());

        File file = new File(PathConstant.APPLICATION_ROOT + "/service", poName + "Service.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 包
            writer.write("package " + PackageConstant.PACKAGE + ".service;\n\n");

            // import语句
            writer.write("import " + PackageConstant.PACKAGE + ".pojo.po."
                    + poName + ";\n");
            writer.write("import " + PackageConstant.PACKAGE + ".pojo.vo.PageVO;\n\n");
            writer.write("import java.util.List;\n\n");

            // 接口定义
            writer.write("public interface " + poName + "Service {\n");
            // insert方法
            writer.write("\tvoid insert(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ");\n\n");

            // select方法
            writer.write("\tList<" + poName + "> select();\n");
            writer.write("\tList<" + poName + "> select(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + ");\n\n");

            // count方法
            writer.write("\tInteger count();\n");
            writer.write("\tInteger count(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + ");\n\n");

            // 分页查询select方法
            writer.write("\tPageVO<" + poName + "> selectPage(Integer pageSize, Integer pageNum);\n");
            writer.write("\tPageVO<" + poName + "> selectPage(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + ", Integer pageSize, Integer pageNum);\n");

            // 索引对应方法
            Set<Map.Entry<String, List<Field>>> indexes = table.getIndexes().entrySet();
            for (Map.Entry<String, List<Field>> index : indexes) {
                List<Field> indexFields = index.getValue();

                // updateBy方法
                writer.write("\n\tvoid updateBy");
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
                writer.write(poName + " " + poName.substring(0, 1).toLowerCase()
                        + poName.substring(1));
                writer.write(");\n");

                // deleteBy方法
                writer.write("\n\tvoid deleteBy");
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
                writer.write("\n\t" + poName + " selectBy");
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
        String poName = FieldToPojoUtil.tableNameToPoName(table.getTableName());

        File file = new File(PathConstant.APPLICATION_ROOT + "/service/impl", poName + "ServiceImpl.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 包
            writer.write("package " + PackageConstant.PACKAGE + ".service.impl;\n\n");

            // import语句
            writer.write("import " + PackageConstant.PACKAGE + ".mapper."
                    + poName + "Mapper;\n");
            writer.write("import " + PackageConstant.PACKAGE + ".pojo.po."
                    + poName + ";\n");
            writer.write("import " + PackageConstant.PACKAGE + ".pojo.vo.PageVO;\n\n");
            writer.write("import " + PackageConstant.PACKAGE + ".service." + poName + "Service;\n");
            writer.write("import org.springframework.beans.factory.annotation.Autowired;\n");
            writer.write("import org.springframework.stereotype.Service;\n\n");
            writer.write("import java.util.List;\n\n");

            // 注解
            writer.write("@Service\n");

            // 实现类定义
            writer.write("public class " + poName + "ServiceImpl implements "
                    + poName + "Service {\n");

            // mapper接口
            writer.write("\t@Autowired\n\tprivate " + poName + "Mapper "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + "Mapper;\n\n");

            // insert方法
            writer.write("\t@Override\n");
            writer.write("\tpublic void insert(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ") {\n");
            writer.write("\t\t" + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + "Mapper.insert(" + poName.substring(0, 1).toLowerCase()
                    + poName.substring(1) + ");\n");
            writer.write("\t}\n\n");

            // select方法
            writer.write("\t@Override\n");
            writer.write("\tpublic List<" + poName + "> select() {\n");
            writer.write("\t\treturn " + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + "Mapper.select(new " + poName + "());\n");
            writer.write("\t}\n\n");
            writer.write("\t@Override\n");
            writer.write("\tpublic List<" + poName + "> select(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ") {\n");
            writer.write("\t\treturn " + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + "Mapper.select(" + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ");\n");
            writer.write("\t}\n\n");

            // count方法
            writer.write("\t@Override\n");
            writer.write("\tpublic Integer count() {\n");
            writer.write("\t\treturn " + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + "Mapper.count(new " + poName + "());\n");
            writer.write("\t}\n\n");
            writer.write("\t@Override\n");
            writer.write("\tpublic Integer count(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ") {\n");
            writer.write("\t\treturn " + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + "Mapper.count(" + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ");\n");
            writer.write("\t}\n\n");

            // 分页查询select方法
            writer.write("\t@Override\n");
            writer.write("\tpublic PageVO<" + poName + "> selectPage(Integer pageSize, Integer pageNum) {\n");
            writer.write("\t\tPageVO<" + poName + "> pageVO = new PageVO<" + poName + ">();\n");
            writer.write("\t\tpageVO.setTotalSize(count());\n");
            writer.write("\t\tpageVO.setPageSize(pageSize);\n");
            writer.write("\t\tpageVO.setTotalPage((count() + pageSize - 1) / pageSize);\n");
            writer.write("\t\tpageVO.setPageNum(pageNum);\n");
            writer.write("\t\tpageVO.setList("
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + "Mapper.selectPage(new " + poName + "(), (pageNum - 1) * pageSize, pageSize));\n");
            writer.write("\t\treturn pageVO;\n");
            writer.write("\t}\n\n");
            writer.write("\t@Override\n");
            writer.write("\tpublic PageVO<" + poName + "> selectPage(" + poName + " "
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + ", Integer pageSize, Integer pageNum) {\n");
            writer.write("\t\tPageVO<" + poName + "> pageVO = new PageVO<" + poName + ">();\n");
            writer.write("\t\tpageVO.setTotalSize(count("
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + "));\n");
            writer.write("\t\tpageVO.setPageSize(pageSize);\n");
            writer.write("\t\tpageVO.setTotalPage((count("
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1) + ") + pageSize - 1) / pageSize);\n");
            writer.write("\t\tpageVO.setPageNum(pageNum);\n");
            writer.write("\t\tpageVO.setList("
                    + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + "Mapper.selectPage(" + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                    + ", (pageNum - 1) * pageSize, pageSize));\n");
            writer.write("\t\treturn pageVO;\n");
            writer.write("\t}\n\n");

            // 索引对应方法
            Set<Map.Entry<String, List<Field>>> indexes = table.getIndexes().entrySet();
            for (Map.Entry<String, List<Field>> index : indexes) {
                List<Field> indexFields = index.getValue();

                // updateBy方法
                writer.write("\n\t@Override\n\tpublic void updateBy");
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
                writer.write(poName + " " + poName.substring(0, 1).toLowerCase()
                        + poName.substring(1));
                writer.write(") {\n");
                writer.write("\t\t" + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                        + "Mapper.updateBy");
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
                writer.write(poName.substring(0, 1).toLowerCase()
                        + poName.substring(1) + ");\n\t}\n");

                // deleteBy方法
                writer.write("\n\t@Override\n\tpublic void deleteBy");
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
                writer.write("\t\t" + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                        + "Mapper.deleteBy");
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
                writer.write("\n\t@Override\n\tpublic " + poName + " selectBy");
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
                writer.write("\t\treturn " + poName.substring(0, 1).toLowerCase() + poName.substring(1)
                        + "Mapper.selectBy");
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
