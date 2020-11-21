package ehu.isad.controllers.db;

import ehu.isad.model.ServerCMSModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public boolean domainInDB(String domain) {
        String query = "select target_id from targets where target = '" + domain + "'";
        ResultSet rs = dbcontroller.execSQL(query);
        try {
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public ObservableList<ServerCMSModel> getCMSDB(){
        ObservableList<ServerCMSModel> results = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT q.target,q.name,q.version,q.date FROM ( SELECT t.target, CASE WHEN p.name in ('WordPress','Joomla','phpMyAdmin','Drupal') THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name in ('WordPress','Joomla','phpMyAdmin','Drupal') THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name in ('WordPress','Joomla','phpMyAdmin','Drupal') UNION SELECT t.target, CASE WHEN p.name = 'WordPress' THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name = 'Wordpress' THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name not in ('WordPress','Joomla','phpMyAdmin','Drupal') GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
        ResultSet rs = dbcontroller.execSQL(query);
        String url;
        String plugin;
        String version;
        String lastUpdated;
        try {
            while (rs.next()) {
                url = rs.getString("target");
                plugin = rs.getString("name");
                version = rs.getString("version");
                lastUpdated = rs.getString("date");
                if (plugin == null) {
                    results.add(new ServerCMSModel(url, "unknown", null, "0", lastUpdated));
                } else {
                    results.add(new ServerCMSModel(url, plugin, null, version, lastUpdated));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return  results;
    }

    public ObservableList<ServerCMSModel> getServerDB(){
        ObservableList<ServerCMSModel> results = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT q.target,q.name,q.version,q.date FROM ( SELECT t.target, CASE WHEN p.name = 'Apache' THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name = 'Apache' THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name='Apache' UNION SELECT t.target, CASE WHEN p.name = 'Apache' THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name = 'Apache' THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name!='Apache' GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
        ResultSet rs = dbcontroller.execSQL(query);
        String url;
        String plugin;
        String version;
        String lastUpdated;
        try {
            while (rs.next()) {
                url = rs.getString("target");
                plugin = rs.getString("name");
                version = rs.getString("version");
                lastUpdated = rs.getString("date");
                results.add(new ServerCMSModel(url, null, plugin, version, lastUpdated));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return  results;
    }

    public void addDate(String targ){
        String query = "insert into servercmsDate values((select target_id from targets where target = '" + targ + "'),DATETIME())";
        dbcontroller.execSQL(query);
    }

    public void updateDate(String domain){
        String query = "update servercmsDate set date = DATETIME() where id = (select target_id from targets where target = '" + domain + "')";
        System.out.println(query);
        dbcontroller.execSQL(query);

    }

}
