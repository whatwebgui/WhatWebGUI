package ehu.isad.controllers.db;

import ehu.isad.model.Extension;
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

    private final DBController dbcontroller = DBController.getController();

    public boolean formatExists(String domain, Extension choice) throws SQLException {
        // does {domain}.{format} exist?
        String type = choice.getType();
        String extension = choice.getExtension();
        String q1 = "SELECT * FROM cache WHERE domain = '";
        String q2 = "' AND ";
        String q3 = " = TRUE;";
        try (ResultSet rs = dbcontroller.execSQL(q1 + domain + q2 + type + q3)) {
            String filename = Utils.getProperties().getProperty("pathToFolder")+"cache/" + domain+"/"+domain + extension;
            System.out.println(filename);
            File file = new File(filename);
            boolean exists = file.exists();
            //If is empty.
            if (file.length() < 10) if (file.delete()) System.out.println("file not deleted"); // is the file empty?
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

}
