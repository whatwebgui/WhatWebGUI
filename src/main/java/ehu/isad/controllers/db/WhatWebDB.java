package ehu.isad.controllers.db;

import ehu.isad.utils.Utils;

import java.io.File;
import java.util.Properties;

public class WhatWebDB {

        private static final WhatWebDB instance = new WhatWebDB();
        private static final DBController dbcontroller = DBController.getController();

    private WhatWebDB() {
    }

    public static WhatWebDB getInstance() {
        return instance;
    }

    public void clearDB(){
        String del1 = "DELETE FROM ";
        String del2 = "DELETE FROM sqlite_sequence WHERE name = '";
        String[] tables = {"scans","targets"};
        for (String table : tables) {
            dbcontroller.execSQL(del1+table);
            dbcontroller.execSQL(del2+table+"';");
        }
    }

    public void deleteCache(){
        Properties p = Utils.getProperties();
        File directory = new File(p.getProperty("pathToFolder")+"cache/");
        if(directory.exists()) directory.delete();
        directory.mkdir();
        String del1 = "DELETE FROM ";
        String del2 = "DELETE FROM sqlite_sequence WHERE name = '";
        String table = "cache";
        dbcontroller.execSQL(del1+table);
        dbcontroller.execSQL(del2+table+"'");
    }

}
