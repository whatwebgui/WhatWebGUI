package ehu.isad.controllers.db;

import ehu.isad.model.ServerCMS;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerCMSDB {

    private static final ServerCMSDB instance = new ServerCMSDB();
    private static final DBController dbcontroller = DBController.getController();

    private ServerCMSDB() {   }

    public static ServerCMSDB getInstance() {
        return instance;
    }

    public void insertQueryIntoDB(String query){
        dbcontroller.execSQL(query);
    }

    public void updateDate(){

    }

    public boolean domainInDB(String domain) throws SQLException {
        String query = "select target_id from targets where target = '" + domain + "'";
        System.out.println(query);
        ResultSet rs = dbcontroller.execSQL(query);
        if(rs.next()){
            return true;
        }else{
            return false;
        }
    }



    public List<ServerCMS> getCMSDB() throws SQLException {
        List<ServerCMS> results = new ArrayList<>();
        String query = "select * from scans";
        ResultSet rs = dbcontroller.execSQL(query);

        while(rs.next()){

        }

        return results;
    }

    private String getTarget(int targetid){
        String query = "select target from targets where target_id = " + targetid;
        ResultSet rs = dbcontroller.execSQL(query);
        try {
            if(rs.next()){
                return rs.getString("target");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private String getPlugin(int pluginid){
        String query = "select name from plugins where plugin_id = " + pluginid;
        ResultSet rs = dbcontroller.execSQL(query);
        try {
            if(rs.next()){
                return rs.getString("name");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
