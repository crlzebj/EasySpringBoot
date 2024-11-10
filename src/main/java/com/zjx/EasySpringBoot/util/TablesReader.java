package com.zjx.EasySpringBoot.util;

import com.zjx.EasySpringBoot.pojo.Field;
import com.zjx.EasySpringBoot.pojo.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
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

    /**
     * 解析表索引
     * @param table
     * @param connection
     */
    private static void readIndexesOfTable(Table table, Connection connection) {
        String sql = String.format(SHOW_INDEX_SQL, table.getTableName());
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int isUnique = resultSet.getInt("Non_unique");
                if (isUnique == 1) {
                    continue;
                }
                String indexName = resultSet.getString("Key_name");
                String fieldName = resultSet.getString("Column_name");
                if (!table.getIndexes().containsKey(indexName)) {
                    table.getIndexes().put(indexName, new ArrayList<Field>());
                }
                for(Field field : table.getFields()) {
                    if(field.getFieldName().equals(fieldName)) {
                        table.getIndexes().get(indexName).add(field);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 解析表
     * @param table
     * @param connection
     */
    private static void readTable(Table table, Connection connection) {
        String sql = String.format(SHOW_FIELDS_SQL, table.getTableName());
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                Field field = new Field();
                field.setFieldName(resultSet.getString("Field"));
                field.setFieldType(resultSet.getString("Type"));
                field.setFieldComment(resultSet.getString("Comment"));
                table.getFields().add(field);
                if(field.getFieldType().contains("decimal")) {
                    table.setHasDecimalType(true);
                }
                if(field.getFieldType().equals("datetime")) {
                    table.setHasDateTimeType(true);
                }
            }
            readIndexesOfTable(table, connection);
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
                readTable(table, connection);
                tableSet.add(table);
            }
            resultSet.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 获取表集合
     * @return
     */
    public static Set<Table> getTableSet() {
        return tableSet;
    }
}
