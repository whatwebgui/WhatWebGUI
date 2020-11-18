package ehu.isad.controllers.db;

import ehu.isad.utils.Utils;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormatterDB {

    private static final FormatterDB controller = new FormatterDB();
    public static FormatterDB getController() {
        return controller;
    }
    private FormatterDB() {}
    private DBController dbcontroller = DBController.getController();

    public boolean formatExists(String domain, String format, String extension) throws SQLException {
        // does {domain}.{format} exist?
        String q1 = "SELECT * FROM cache WHERE domain = '";
        String q2 = "' AND ";
        String q3 = " = TRUE;";
        try (ResultSet rs = dbcontroller.execSQL(q1 + domain + q2 + format + q3)) {
            File file = new File(Utils.getProperties().getProperty("pathToFolder")+"cache/" + domain + extension);
            if (file.length() < 10) file.delete(); //If is empty.
            boolean exists = file.exists();
            return rs.next() && exists;
        }
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

    private void deleteCache(){
        dbcontroller.execSQL("DELETE FROM cache");
    }

}
