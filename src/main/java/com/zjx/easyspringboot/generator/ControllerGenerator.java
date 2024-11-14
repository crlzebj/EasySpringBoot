package com.zjx.easyspringboot.generator;

import com.zjx.easyspringboot.constant.PackageConstant;
import com.zjx.easyspringboot.constant.PathConstant;
import com.zjx.easyspringboot.pojo.Table;
import com.zjx.easyspringboot.util.FieldToPojoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
            writer.write("import org.springframework.beans.factory.annotation.Autowired;\n");
            writer.write("import org.springframework.web.bind.annotation.*;\n\n");

            // 注解
            writer.write("@RestController\n");
            writer.write("@RequestMapping(\"/" + entityName.substring(0, 1).toLowerCase()
                    + entityName.substring(1) + "s\")\n");

            // 实现类定义
            writer.write("public class " + entityName + "Controller {\n");

            // service接口
            writer.write("\t@Autowired\n\tprivate " + entityName + "Service "
                    + entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + "Service;\n");

            writer.write("}\n");
            writer.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
