/*
 * @ (#) ConnectDB.java   1.0 9/24/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/24/2025
 * version: 1.0
 */
public class ConnectDB {
    public static Connection con = null;
    private static ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {
        return instance;
    }
    public Connection connect() throws SQLException {
//        String url = "jdbc:sqlserver://34.171.249.19:1433;databasename=QuanLyNhaThuoc";
//        String user = "sqlserver";
//        String password = "0867424460";
        String url = "jdbc:sqlserver://localhost:1433;databasename=QuanLyNhaThuoc";
        String user = "sa";
        String password = "sa123";
        con = DriverManager.getConnection(url, user, password);
        return con;
    }
    public void disconnect() {
        if (con != null)
            try {
                con.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public static Connection getConnection() {
        return con;
    }
}
