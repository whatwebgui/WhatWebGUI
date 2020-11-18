package ehu.isad.utils;

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
    public static void createDirectories(){
        Properties p = Utils.getProperties();
        File directoryCache = new File(p.getProperty("pathToFolder")+"/cache");
        if(!directoryCache.exists()) directoryCache.mkdir();
        File directoryScreenshoots = new File(p.getProperty("pathToFolder")+"/screenshoots");
        if(!directoryScreenshoots.exists()) directoryScreenshoots.mkdir();
    }
}