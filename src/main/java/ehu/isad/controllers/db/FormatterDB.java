package ehu.isad.controllers.db;

import javax.print.DocFlavor;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormatterDB {

    private static final FormatterDB controller = new FormatterDB();
    public static FormatterDB getController() {
        return controller;
    }
    private FormatterDB() {}
    private DBController dbcontroller = DBController.getController();

    public boolean formatExists(String domain,String format) throws SQLException {
        // does {domain}.{format} exist?
        String q1 = "SELECT * FROM cache WHERE domain = '";
        String q2 = "' AND ";
        String q3 = " = TRUE;";
        StringBuilder query = new StringBuilder();
        query.append(q1).append(domain).append(q2).append(format).append(q3);
        ResultSet rs = dbcontroller.execSQL(query.toString());
        return rs.next();

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void addFormatToDB(String domain, String format) {
        //This method adds the "check" to the database.
        String q2 = "UPDATE cache SET "+format+" = TRUE WHERE domain='"+domain+"';";
        dbcontroller.execSQL(q2);
    }
    public  void addDomainToDB(String domain){
        String q1 = "INSERT OR IGNORE into cache(domain) values('"+domain+"')";
        dbcontroller.execSQL(q1);

    }


}
