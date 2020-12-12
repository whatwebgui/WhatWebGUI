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

    public static void curl() {
        File txtdir = new File(pathToFolder+"/txt");
        String fileurl = "https://raw.githubusercontent.com/whatwebgui/ww/main/txt/";
        if(!txtdir.exists()) {
            txtdir.mkdir();
            InputStream in;
            try {
                in= new URL(fileurl+"cmslist.txt").openStream();
                Files.copy(in, Paths.get(pathToFolder+"/txt/cmslist.txt"), StandardCopyOption.REPLACE_EXISTING);

                in = new URL(fileurl+"serverlist.txt").openStream();
                Files.copy(in, Paths.get(pathToFolder+"/txt/serverlist.txt"), StandardCopyOption.REPLACE_EXISTING);

                in = new URL(fileurl+"list.txt").openStream();
                Files.copy(in, Paths.get(pathToFolder+"/txt/list.txt"), StandardCopyOption.REPLACE_EXISTING);

                in = new URL(fileurl+"db.txt").openStream();
                Files.copy(in, Paths.get(pathToFolder+"/txt/db.txt"), StandardCopyOption.REPLACE_EXISTING);

                in = new URL(fileurl+"extensions.txt").openStream();
                Files.copy(in, Paths.get(pathToFolder+"/txt/extensions.txt"), StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception e){

            }

        }

    }
}