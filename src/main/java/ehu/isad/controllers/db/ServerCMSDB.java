package ehu.isad.controllers.db;

import ehu.isad.model.ServerCMS;
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

    public boolean domainInDB(String domain) throws SQLException {
        String query = "select target_id from targets where target = '" + domain + "'";
        ResultSet rs = dbcontroller.execSQL(query);
        if(rs.next()){
            return true;
        }else{
            return false;
        }
    }

    public ObservableList<ServerCMS> getCMSDB() throws SQLException {
        ObservableList<ServerCMS> results = FXCollections.observableArrayList();
        String firstQuery = "select * from targets";
        ResultSet rst = dbcontroller.execSQL(firstQuery);
        if(rst.next()){
            String query = "select target as url,name=null as plugin,version=0 as version from scans as s,targets as t,plugins as p where s.plugin_id = p.plugin_id and t.target_id = s.target_id and status=200 AND name not in (\"WordPress\",\"Joomla\",\"phpMyAdmin\",\"Drupal\") group by target except select target as url,name=null,version=0 from scans as s,targets as t,plugins as p where s.plugin_id = p.plugin_id and t.target_id = s.target_id and status=200 AND name in (\"WordPress\",\"Joomla\",\"phpMyAdmin\",\"Drupal\") group by target union select target as url,name as plugin,version from scans as s,targets as t,plugins as p where s.plugin_id = p.plugin_id and t.target_id = s.target_id and status=200 AND name in (\"WordPress\",\"Joomla\",\"phpMyAdmin\",\"Drupal\") group by target";
            ResultSet rs = dbcontroller.execSQL(query);
            try {
                String url;
                String plugin;
                String version;
                while(rs.next()){
                    url = rs.getString("url");
                    plugin = rs.getString("plugin");
                    version = rs.getString("version");
                    if(plugin==null){
                        results.add(new ServerCMS(url,"unknown",null,"0",null));
                    }else{
                        results.add(new ServerCMS(url,plugin,null,version,null));
                    }
                }
            }catch(SQLException e){ e.printStackTrace(); }
        }
        return results;
    }

    public ObservableList<ServerCMS> getServerDB() throws SQLException {
        ObservableList<ServerCMS> results = FXCollections.observableArrayList();
        String firstQuery = "select * from targets";
        ResultSet rst = dbcontroller.execSQL(firstQuery);
        if(rst.next()){
            String query = "select target as url,name=null as plugin,version=0 as version from scans as s,targets as t,plugins as p where s.plugin_id = p.plugin_id and t.target_id = s.target_id and status=200 AND name not in (\"Apache\") group by target except select target as url,name=null,version=0 from scans as s,targets as t,plugins as p where s.plugin_id = p.plugin_id and t.target_id = s.target_id and status=200 AND name in (\"Apache\") group by target union select target as url,name as plugin,version from scans as s,targets as t,plugins as p where s.plugin_id = p.plugin_id and t.target_id = s.target_id and status=200 AND name in (\"Apache\") group by target";
            ResultSet rs = dbcontroller.execSQL(query);
            try {
                String url;
                String plugin;
                String version;
                while(rs.next()){
                    url = rs.getString("url");
                    plugin = rs.getString("plugin");
                    version = rs.getString("version");
                    if(plugin==null){
                        results.add(new ServerCMS(url,null,"unknown","0",null));
                    }else{
                        results.add(new ServerCMS(url,null,plugin,version,null));
                    }
                }
            }catch(SQLException e){ e.printStackTrace(); }
        }
        return results;
    }

    public void addDate(String targ){
        String query = "insert into servercmsDate values((select target_id from targets where target = '" + targ + "'),'"+"kaixo"+"')";
        dbcontroller.execSQL(query);
    }

    public void updateDate(String domain){

        //String query = "update servercmsDate set date = '" + + "' where id = (select id from targets where target '" + domain + "')";

    }

}
