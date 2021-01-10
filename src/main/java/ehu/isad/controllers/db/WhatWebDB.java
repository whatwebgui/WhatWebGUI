package ehu.isad.controllers.db;

import ehu.isad.utils.Txt;
import ehu.isad.utils.Utils;

import java.io.File;
import java.io.IOException;


public class WhatWebDB {

        private static final WhatWebDB instance = new WhatWebDB();
        private static final DBController dbcontroller = DBController.getController();

    private WhatWebDB() {}

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
        File directory = new File(System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToFolder")+"/cache");
        if(directory.exists()){
            try {
                deleteDirectory(new File(System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToFolder")));
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
            p = Runtime.getRuntime().exec("rm -rf " + path + "/cache");
        }
        while(p.isAlive()){}
    }

    public void createDB() {
        File directory = new File(System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToFolder")+"/db/");
        directory.mkdir();
        String[] create = Txt.getTxt("db");
        for (String query : create)
            dbcontroller.execSQL(query);
    }
}
