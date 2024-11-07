package com.zjx.EasySpringBoot.util;

import com.zjx.EasySpringBoot.pojo.Field;
import com.zjx.EasySpringBoot.pojo.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class TablesReader {
    private static final Logger logger = LoggerFactory.getLogger(TablesReader.class);
    // SQL 语句
    private static final String SHOW_TABLES_SQL = "show table status;";
    private static final String SHOW_FIELDS_SQL = "show full fields from %s;";
    private static final String SHOW_INDEX_SQL = "show index from %s;";

    // 存储所有的表信息
    private static final Set<Table> tableSet = new HashSet<Table>();

    private static void parseTable(Table table, Connection connection) {
        String sql = String.format(SHOW_FIELDS_SQL, table.getTableName());
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                Field field = new Field();
                field.setFieldName(resultSet.getString("Field"));
                logger.info(field.getFieldName());
                field.setFieldType(resultSet.getString("Type"));
                logger.info(field.getFieldType());
                field.setFieldComment(resultSet.getString("Comment"));
                logger.info(field.getFieldComment());
                table.getFields().add(field);
            }
            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    static {
        try{
            Class.forName(PropertiesReader.getSetting("db.driver.name"));
            Connection connection = DriverManager.getConnection(PropertiesReader.getSetting("db.url"),
                                                     PropertiesReader.getSetting("db.username"),
                                                     PropertiesReader.getSetting("db.password"));
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SHOW_TABLES_SQL);
            while (resultSet.next()) {
                Table table = new Table();
                table.setTableName(resultSet.getString("Name"));
                table.setTableComment(resultSet.getString("Comment"));
                parseTable(table, connection);
                tableSet.add(table);
            }
            resultSet.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

}
