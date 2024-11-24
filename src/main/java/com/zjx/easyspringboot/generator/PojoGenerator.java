package com.zjx.easyspringboot.generator;

import com.zjx.easyspringboot.constant.PackageConstant;
import com.zjx.easyspringboot.constant.PathConstant;
import com.zjx.easyspringboot.pojo.Field;
import com.zjx.easyspringboot.pojo.Table;
import com.zjx.easyspringboot.util.FieldToPojoUtil;
import com.zjx.easyspringboot.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class PojoGenerator {
    private static final Logger log = LoggerFactory.getLogger(PojoGenerator.class);

    // PageVO类模板
    private static final String PAGEVO_TEMPLATE;

    static {
        PAGEVO_TEMPLATE = """
                package %s.pojo.vo;
                
                import lombok.AllArgsConstructor;
                import lombok.Data;
                import lombok.NoArgsConstructor;
                
                import java.util.List;
                
                /**
                 * 通用分页数据封装类
                 */
                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public class PageVO<T> {
                    // 总记录数
                    private Integer totalSize;
                
                    // 每页记录数
                    private Integer pageSize;
                
                    // 总页数
                    private Integer totalPage;
                
                    // 当前页码
                    private Integer pageNum;
                
                    // 分页数据
                    private List<T> list;
                }
                """;
    }

    /**
     * 根据表生成对应po
     * @param table
     */
    public static void generatePo(Table table) {
        String entityName = FieldToPojoUtil.tableNameToPoName(table.getTableName());

        File file = new File(PathConstant.APPLICATION_ROOT + "/pojo/po", entityName + ".java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 包
            writer.write("package " + PackageConstant.PACKAGE + ".pojo.po;");
            writer.newLine();
            writer.newLine();

            // import语句
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
            log.error(e.getMessage());
        }
    }

    /**
     * 生成VO
     */
    public static void generateVo() {
        File file = new File(PathConstant.APPLICATION_ROOT + "/pojo/vo", "PageVO.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String pageVOStr = PAGEVO_TEMPLATE.formatted(PackageConstant.PACKAGE);
            writer.write(pageVOStr);
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
