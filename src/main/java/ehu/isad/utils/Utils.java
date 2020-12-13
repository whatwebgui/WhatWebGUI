package ehu.isad.utils;

import ehu.isad.controllers.db.WhatWebDB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class Utils {

    private static String home = System.getProperty("user.home");
    private static Properties p = Utils.getProperties();
    private static String pathToFolder = home+"/"+p.getProperty("pathToFolder");

    public static Properties getProperties()  {
        Properties properties = null;

        try (InputStream in = Utils.class.getResourceAsStream("/setup.properties")) {
            properties = new Properties();
            properties.load(in);

        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static void createDB(){
        File database = new File(home+"/"+p.getProperty("pathToDB"));
        if(!database.exists()) WhatWebDB.getInstance().createDB();
    }

    public static void createDirectories(){
        File directory = new File(pathToFolder);
        if(!directory.exists()) directory.mkdir();
        File directoryCache = new File(pathToFolder+"/cache");
        directoryCache.mkdir();
//        File directoryScreenshots = new File(p.getProperty("pathToFolder")+"/screenshots");
//        if(!directoryScreenshots.exists()) directoryScreenshots.mkdir();
    }

    public static void getTextFromURL() {
        File txtdir = new File(pathToFolder+"/txt");
        String fileurl = "https://raw.githubusercontent.com/whatwebgui/ww/main/txt/";
        if(!txtdir.exists()) {
            txtdir.mkdir();
            Process pr = null;
            String[] txt = {"cmslist.txt","serverlist.txt","list.txt","db.txt","extensions.txt"};
            if(System.getProperty("os.name").toLowerCase().contains("win")){
                for(int i = 0; i < txt.length; i++){
                    try {
                        ProcessBuilder processBuilder = new ProcessBuilder();
                        processBuilder.directory(txtdir);
                        processBuilder.command("cmd.exe", "/C", "curl "+fileurl+txt[i]+" -O "+txt[i]);
                        pr = processBuilder.start();
                    } catch (IOException ioException) { ioException.printStackTrace();  }
                }
            }else{
                for(int i = 0; i < txt.length; i++){
                    try {
                        pr = Runtime.getRuntime().exec("wget -O " + pathToFolder + "/txt/" + txt[i] + " " + fileurl + txt[i]);
                    } catch (IOException ioException) { ioException.printStackTrace();  }
                }
            }
            while(pr.isAlive()){}
        }

    }
}