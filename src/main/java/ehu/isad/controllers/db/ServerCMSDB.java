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
        String firstQuery = "select * from targets";
        ResultSet rst = dbcontroller.execSQL(firstQuery);
        try {
            if(rst.next()){
                String query = "select * from(select target as url,name=null as plugin,version=0 as version,date as lastUpdated from scans as s,targets as t,plugins as p,servercmsDate as d where s.plugin_id = p.plugin_id and t.target_id = s.target_id and d.id = t.target_id and status=200 AND name not in (\"WordPress\",\"Joomla\",\"phpMyAdmin\",\"Drupal\") group by target except select target as url,name=null,version=0,date as lastUpdated from scans as s,targets as t,plugins as p,servercmsDate as d where s.plugin_id = p.plugin_id and t.target_id = s.target_id and d.id = t.target_id and status=200 AND name in (\"WordPress\",\"Joomla\",\"phpMyAdmin\",\"Drupal\") group by target union select target as url,name as plugin,version,date as lastUpdated from scans as s,targets as t,plugins as p,servercmsDate as d where s.plugin_id = p.plugin_id and t.target_id = s.target_id and d.id = t.target_id and status=200 AND name in (\"WordPress\",\"Joomla\",\"phpMyAdmin\",\"Drupal\") group by target) order by lastUpdated desc";
                ResultSet rs = dbcontroller.execSQL(query);
                try {
                    String url;
                    String plugin;
                    String version;
                    String lastUpdated;
                    while(rs.next()){
                        url = rs.getString("url");
                        plugin = rs.getString("plugin");
                        version = rs.getString("version");
                        lastUpdated = rs.getString("lastUpdated");
                        if(plugin==null){
                            results.add(new ServerCMSModel(url,"unknown",null,"0",lastUpdated));
                        }else{
                            results.add(new ServerCMSModel(url,plugin,null,version,lastUpdated));
                        }
                    }
                }catch(SQLException e){ e.printStackTrace(); }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return results;
    }

    public ObservableList<ServerCMSModel> getServerDB() {
        ObservableList<ServerCMSModel> results = FXCollections.observableArrayList();
        String firstQuery = "select * from targets";
        ResultSet rst = dbcontroller.execSQL(firstQuery);
        try {
            if(rst.next()){
                String query = "select * from(select target as url,name=null as plugin,version=0 as version,date as lastUpdated from scans as s,targets as t,plugins as p,servercmsDate as d where s.plugin_id = p.plugin_id and t.target_id = s.target_id and d.id = t.target_id and status=200 AND name not in (\"Apache\") group by target except select target as url,name=null,version=0,date as lastUpdated from scans as s,targets as t,plugins as p,servercmsDate as d where s.plugin_id = p.plugin_id and t.target_id = s.target_id and d.id = t.target_id and status=200 AND name in (\"Apache\") group by target union select target as url,name as plugin,version,date as lastUpdated from scans as s,targets as t,plugins as p,servercmsDate as d where s.plugin_id = p.plugin_id and t.target_id = s.target_id and d.id = t.target_id and status=200 AND name in (\"Apache\") group by target) order by lastUpdated desc;";
                ResultSet rs = dbcontroller.execSQL(query);
                try {
                    String url;
                    String plugin;
                    String version;
                    String lastUpdated;
                    while(rs.next()){
                        url = rs.getString("url");
                        plugin = rs.getString("plugin");
                        version = rs.getString("version");
                        lastUpdated = rs.getString("lastUpdated");

                        if(plugin==null){
                            results.add(new ServerCMSModel(url,null,"unknown","0",lastUpdated));
                        }else{
                            results.add(new ServerCMSModel(url,null,plugin,version,lastUpdated));
                        }
                    }
                }catch(SQLException e){ e.printStackTrace(); }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return results;
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
