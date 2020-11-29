package ehu.isad.controllers.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TutorialDB {
    private static final TutorialDB instance = new TutorialDB();
    private static final DBController dbcontroller = DBController.getController();

    private TutorialDB() {
    }

    public static TutorialDB getInstance() {
        return instance;
    }

    public boolean tutorialDone() throws SQLException {
        //If true do not exec tutorial
        //If false execute it.
        String query = "select used from tutorial where used=true";
        ResultSet rs = dbcontroller.execSQL(query);

        if (!rs.next()){
            query = "update tutorial set used = 1";
            dbcontroller.execSQL(query);
        }

        return rs.next();
    }
    public  void unsetTutorial(){
        String query = "update tutorial set used = 1";
        dbcontroller.execSQL(query);
    }
}
