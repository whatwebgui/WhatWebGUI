package ehu.isad.controllers.db;

import org.apache.commons.io.FileUtils;
import ehu.isad.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


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
        int CONNECT_TIMEOUT = 1000, READ_TIMEOUT = 1000;

        /*String dbpath = System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToFolder")+"/db/";
        File directory = new File(dbpath);
        directory.mkdir();*/

        try {
            FileUtils.copyURLToFile(
                    new URL(Utils.getProperties().getProperty("setupDB")),
                    new File(System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToDB")),
                    CONNECT_TIMEOUT,
                    READ_TIMEOUT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
