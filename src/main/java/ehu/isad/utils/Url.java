package ehu.isad.utils;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

public class Url {

    public String processUrl(String url) throws SQLException {
        String target = url;
        if(target.charAt(target.length()-1)!='/') target = target+"/";
        if(!target.contains(":")){
            target = "http://"+target;
        }
        if(!netIsAvailable(target)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error on URL");
            alert.setHeaderText("Error on reading the provided URL");
            alert.setContentText("The URL "+ url + " doesn't seem to exist");
            alert.showAndWait();
            return null;
        }else{
            return target;
        }
    }

    public String processUrlInMulti(String url) throws SQLException {
        String target = url;
        try{
            if(target.charAt(target.length()-1)!='/') target = target+"/";
            if(!target.contains(":")){
                target = "http://"+target;
            }
        }catch(IndexOutOfBoundsException e){
            //nothing
        }
        if(!netIsAvailable(target)){
            return null;
        }else{
            return target;
        }
    }

    private boolean netIsAvailable(String ur) throws SQLException {
        try {
            final URL url = new URL(ur);
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            //throw new RuntimeException(e);
            return false;
        } catch (IOException e) {
            return false;
        }

    }
}