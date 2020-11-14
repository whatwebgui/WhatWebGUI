package ehu.isad.controllers.db;

import java.sql.*;

public class DBController {

    Connection conn=null;
    private static DBController controller = new DBController();

    private DBController() {
        this.conOpen();
    }

    public static DBController getController() {
        return controller;
    }

    private void conOpen() {

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:whatweb.db");
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
                System.out.println("Connection to SQLite has been terminated.");
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }


}
