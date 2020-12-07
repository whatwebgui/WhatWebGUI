package ehu.isad.utils;

import ehu.isad.controllers.db.WhatWebDB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

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
        Properties p = Utils.getProperties();
        String home = System.getProperty("user.home");
        File database = new File(home+"/"+p.getProperty("pathToDB"));
        if(!database.exists()) WhatWebDB.getInstance().createDB();
    }

    public static void createDirectories(){
        Properties p = Utils.getProperties();
        String home = System.getProperty("user.home");
        File directory = new File(home+"/"+p.getProperty("pathToFolder")+"");
        if(!directory.exists()) directory.mkdir();
        File directoryCache = new File(home+"/"+p.getProperty("pathToFolder")+"/cache");
        directoryCache.mkdir();
//        File directoryScreenshots = new File(p.getProperty("pathToFolder")+"/screenshots");
//        if(!directoryScreenshots.exists()) directoryScreenshots.mkdir();
    }
}