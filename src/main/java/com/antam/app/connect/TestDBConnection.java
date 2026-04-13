package com.antam.app.connect;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            ConnectDB db = ConnectDB.getInstance();
            if (db.connect() != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed!");
            }
            db.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

