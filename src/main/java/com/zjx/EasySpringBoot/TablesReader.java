package com.zjx.EasySpringBoot;

import com.zjx.EasySpringBoot.pojo.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class TablesReader {
    private static Connection connection;
    private static final String SHOW_TABLES_SQL = "show table status";
    private static final String SHOW_FIELDS_SQL = "show full fields from ";
    private static Set<Table> tableSet = new HashSet<Table>();

    private static void setTable(String tableName) {

    }

    static {
        try{
            Class.forName(PropertiesReader.getSetting("db.driver.name"));
            connection = DriverManager.getConnection(PropertiesReader.getSetting("db.url"),
                                                     PropertiesReader.getSetting("db.username"),
                                                     PropertiesReader.getSetting("db.password"));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SHOW_TABLES_SQL);
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
    public static void main(String[] args) {

    }
}