package com.example.tool;

import javax.ws.rs.GET;
import java.sql.*;

public class DB {
    protected final String url = "jdbc:mysql://localhost:3306/";
    private final String name = "root";
    private final String password = "51130012Cyc";

    public Connection db;
    protected String table;
    protected Statement stmt;
    protected String sql;
    public ResultSet results;

    protected String[] struct;
    protected int structNum;
    protected int rows;

    public DB(String dbname, String table) {
        String newUrl = url + dbname + "?autoReconnect=true";
        this.table = table;
        try {
            //注册JDBC驱动
            Class.forName("com.mysql.jdbc.Driver");
            db = DriverManager.getConnection(newUrl, name, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setDBName(String dbname) {
        String newUrl = url + dbname;
        try {
            db = DriverManager.getConnection(newUrl, name, password);
            stmt = null;
            stmt = db.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStruct() {
        for (int i = 0; i < this.struct.length; i++) {
            System.out.println(struct[i]);
        }
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getLastSql() {
        return sql;
    }

    public int getRows() {
        return rows;
    }

    public void showDesc() {
        try {
            sql = "desc " + table + ";";
            results = stmt.executeQuery(sql);
            System.out.println("\n表结构:\n表名\t\t\t值类型");
            while (results.next()) {
                String field = results.getString("Field");
                String type = results.getString("Type");
                System.out.println(field + "\t\t\t" + type);
            }
            results.last();       //必须到最后一行，才能读到准确的行数
            rows = results.getRow();
            System.out.println("总共" + rows + "条数据");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet select(String sql) {
        try {
            this.sql = sql;
            stmt = db.createStatement();
            results = stmt.executeQuery(sql);
            results.last();
            rows =results.getRow();
            results.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public int execute(String sql){
        try {
            this.sql = sql;
            stmt = db.createStatement();
            rows = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

}
