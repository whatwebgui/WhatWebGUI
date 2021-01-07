package ehu.isad.controllers.db;

import ehu.isad.model.HistoryModel;
import ehu.isad.model.SecurityModel;
import ehu.isad.model.ServerCMSModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SecurityDB {

    private static final SecurityDB instance = new SecurityDB();
    private static final DBController dbcontroller = DBController.getController();

    private SecurityDB() {   }

    public static SecurityDB getInstance() {
        return instance;
    }

    public ObservableList<SecurityModel> getFromSecurityDB() {
        String query = "select name,string,target from plugins NATURAL join scans natural join targets where status = 200 and (name = \"IP\" or name = \"Country\")";
        ObservableList<SecurityModel> list = FXCollections.observableArrayList();
        ResultSet rs = dbcontroller.execSQL(query);
        String os = System.getProperty("os.name").toLowerCase();
        String ip = "";
        String target = "";
        String country = "";
        try {
            while (rs.next()) {
               if (os.contains("mac")) {
                    ip = rs.getString("string");
                   rs.next();
                    target = rs.getString("target");
                    country = rs.getString("string");

                } else if (os.contains("linux")) {
                    target = rs.getString("target");
                    country = rs.getString("string");
                   rs.next();
                   ip = rs.getString("string");
                }
                list.add(new SecurityModel(target,ip,country,false));

            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public boolean passwordField(String url){
        String query = "select name from plugins natural join scans natural join targets where target = '" + url + "' and name = \"PasswordField\"";
        System.out.println(query);
        ResultSet rs = dbcontroller.execSQL(query);
        try{
            while(rs.next()) {
                String name = rs.getString("name");
                return name != null;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean cmsVulnerability(ServerCMSModel model){
        String query = "select version from Versions where name = '"+model.getCms()+"'";
        ResultSet rs = dbcontroller.execSQL(query);
        try{
            String v = null;
            while(rs.next()){
                v = rs.getString("version");
            }
            if(!model.getVersionc().equals(v)) return true;
            else return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean serverVulnerability(ServerCMSModel model){
        String query = "select version from Versions where name = '"+model.getServer()+"'";
        ResultSet rs = dbcontroller.execSQL(query);
        try{
            String v = null;
            while(rs.next()){
                v = rs.getString("version");
            }
            if(!model.getVersions().equals(v)) return true;
            else return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean isCMSVersionInDB(String name){
        String query = "select * from Versions where name = '"+name+"'";
        ResultSet rs = dbcontroller.execSQL(query);
        try {
            if (rs.next()) return true;
            else return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String getVersion(String plugin){
        String query = "select version from Versions where name = '" + plugin + "'";
        ResultSet rs = dbcontroller.execSQL(query);
        try{
            rs.next();
            return rs.getString("version");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
