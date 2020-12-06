package ehu.isad.controllers.db;

import ehu.isad.utils.Utils;
import java.io.File;
import java.io.IOException;
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
        String[] tables = {"scans","targets","history","servercmsDate"};
        for (String table : tables) {
            dbcontroller.execSQL(del1+table);
            dbcontroller.execSQL(del2+table+"';");
        }
    }

    public void deleteCache(){
        Properties p = Utils.getProperties();
        File directory = new File(p.getProperty("pathToFolder")+"cache/");
        if(directory.exists()){
            try {
                deleteDirectory(new File(p.getProperty("pathToFolder")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        directory.mkdir();
        String del1 = "DELETE FROM ";
        String del2 = "DELETE FROM sqlite_sequence WHERE name = '";
        String table = "cache";
        dbcontroller.execSQL(del1+table);
        dbcontroller.execSQL(del2+table+"'");
    }

    private void deleteDirectory(File path) throws IOException {
        Process p;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(path);
            processBuilder.command("cmd.exe", "/C", "rmdir cache /s /q");
            p = processBuilder.start();
        } else {
            p = Runtime.getRuntime().exec("rm -rf cache");
        }
        while(p.isAlive()){}
    }

}
