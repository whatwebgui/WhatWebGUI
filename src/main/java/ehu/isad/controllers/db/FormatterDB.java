package ehu.isad.controllers.db;

import javax.print.DocFlavor;
import java.sql.SQLException;

public class FormatterDB {
    private static final FormatterDB controller = new FormatterDB();
    public static FormatterDB getController() {
        return controller;
    }
    private FormatterDB() {}
    private DBController dbcontroller = DBController.getController();

    public Boolean formatExists(String domain,String format) throws SQLException {
        // does {domain}.{format} exist?
        String q1 = "Select * from cache  where url = ";
        String q2 = " AND ";
        String q3 = " = TRUE;";
        StringBuilder query = new StringBuilder();
        query.append(q1).append(domain).append(q2).append(format).append(q3);
        System.out.println( query.append(q1).append(domain).append(q2).append(format).append(q3));
        return dbcontroller.execSQL(query.toString()).next();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    void addFormatToDB(String domain, String format) {
        //This method adds the "check" to the database.
        String q1 = "INSERT OR IGNORE (domain) VALUES ('"+domain+"')";
        String q2 = "UPDATE cache SET "+format+" = TRUE WHERE domain='"+domain+"';";
        dbcontroller.execSQL(q1);
        dbcontroller.execSQL(q2);
    }




}
