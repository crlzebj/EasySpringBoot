package com.zjx.EasySpringBoot;

import com.zjx.EasySpringBoot.pojo.Table;
import com.zjx.EasySpringBoot.util.PropertiesReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class CodeGenerator {

    // SQL 语句
    private static final String SHOW_TABLES_SQL = "show table status;";
    private static final String SHOW_FIELDS_SQL = "show full fields from %s;";
    private static final String SHOW_INDEX_SQL = "show index from %s";

    private static final Set<Table> tableSet = new HashSet<Table>();

    private static void parseTable(String tableName) {
        String sql = String.format(SHOW_TABLES_SQL, tableName);
        try {
            //Statement stmt = connection.createStatement();
            //ResultSet resultSet = stmt.executeQuery(SHOW_FIELDS_SQL);
            //while (resultSet.next()) {

            //}
        } catch (Exception e) {
            e.printStackTrace();
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
                table.setTableName(resultSet.getString("table_name"));
                table.setTableComment(resultSet.getString("table_comment"));
                tableSet.add(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}