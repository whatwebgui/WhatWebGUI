package ehu.isad.controllers.db;

import ehu.isad.utils.Utils;

import java.sql.*;

public class DBController {

    Connection conn=null;
    private static final DBController controller = new DBController();
    private final String path = System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToDB");

    private DBController() {
        this.conOpen();
    }

    public static DBController getController() {
        return controller;
    }

    private void conOpen() {
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        try {
            //conn = DriverManager.getConnection("jdbc:sqlite:"+path);
            DriverManager.registerDriver(new org.sqlite.JDBC());
            conn = DriverManager.getConnection("jdbc:sqlite:"+path);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    private void conClose() {

        if (conn != null)
            try {
                conn.close();
                System.out.println("Connection to SQLite has been terminated.\n");
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    public ResultSet execSQL(String query) {
        conClose();
        conOpen();
        ResultSet rs = null;
        try {
            Statement s = conn.createStatement();
            if (query.toLowerCase().indexOf("select") == 0) {
                // select
                rs = s.executeQuery(query);
            } else {
                // update, delete, create, insert...
                int count = s.executeUpdate(query);
                System.out.println(count + " rows affected");
            }
        } catch (SQLException e) {
            System.err.println(query);
            e.printStackTrace();
        }
        return rs;
    }

}
