package ehu.isad.controllers.db;

import ehu.isad.model.HistoryModel;
import ehu.isad.model.SecurityModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        try {
            while (rs.next()) {
                String target = rs.getString("target");
                String country = rs.getString("string");
                rs.next();
                String ip = rs.getString("string");
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
                if(name!=null){
                    return true;
                }else{
                    return false;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
