package ehu.isad.utils;

import ehu.isad.controllers.db.WhatWebDB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    }
}
