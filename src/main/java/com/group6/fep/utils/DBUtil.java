/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group6.fep.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author kanek
 */
public class DBUtil {

    private static Connection conn;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        if (conn == null) {
            String url = "jdbc:sqlserver://localhost;databaseName=FEP;"
                   + "user=fepgroup6;password=fepgroup6";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            conn = DriverManager.getConnection(url);
        }

        return conn;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
