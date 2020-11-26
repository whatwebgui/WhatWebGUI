package ehu.isad.controllers.db;

import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        String list = openFile("cms");
        ObservableList<ServerCMSModel> results = FXCollections.observableArrayList();
        //String query = "SELECT DISTINCT q.target,q.name,q.version,q.date FROM ( SELECT t.target, CASE WHEN p.name in ('WordPress','Joomla','phpMyAdmin','Drupal') THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name in ('WordPress','Joomla','phpMyAdmin','Drupal') THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name in ('WordPress','Joomla','phpMyAdmin','Drupal') UNION SELECT t.target, CASE WHEN p.name = 'WordPress' THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name = 'Wordpress' THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name not in ('WordPress','Joomla','phpMyAdmin','Drupal') GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
        String query = "SELECT DISTINCT q.target,q.name,q.version,q.date FROM ( SELECT t.target, CASE WHEN p.name in " + list + " THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name in " + list + " AND s.version not in ('0','') THEN s.version WHEN s.version in ('0','') THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name in " + list + "UNION SELECT t.target, 'unknown' AS name,'unknown' AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
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
        //String query = "SELECT DISTINCT q.target,q.name,q.version,q.date FROM ( SELECT t.target, CASE WHEN p.name = 'Apache' THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name = 'Apache' THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name='Apache' UNION SELECT t.target, CASE WHEN p.name = 'Apache' THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name = 'Apache' THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name!='Apache' GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
        String list = openFile("server");
        String query = "SELECT DISTINCT q.target,q.name,q.version,q.date FROM ( SELECT t.target, CASE WHEN p.name in " + list + " THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name in " + list + " AND s.version not in ('0','') THEN s.version WHEN s.version in ('0','') THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name in " + list + "UNION SELECT t.target, 'unknown' AS name,'unknown' AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
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

    private String openFile(String tab) {
        StringBuffer sb=new StringBuffer();
        FileReader fr;
        try {
            if (tab.equals("cms")) {
                fr = new FileReader(new File(ServerCMSDB.class.getResource("/cmslist.txt").getFile()));
                sb.append("('WordPress', 'Joomla', 'Drupal', 'phpMyAdmin',");
            } else {
                fr=new FileReader(new File(ServerCMSDB.class.getResource("/serverlist.txt").getFile()));
                sb.append("('Apache', 'nginx', ");
            }
            BufferedReader br=new BufferedReader(fr);
            String line;
            while((line=br.readLine())!=null) {
                sb.append("'").append(line).append("', ");
            }
            sb.append("'EndOfPluginList')");
            fr.close();
        } catch(IOException e){ e.printStackTrace();}
        return sb.toString();
    }

    public void addDate(String targ){
        System.out.println(targ);
        String domain;
        try {
            domain = targ.replace("/", "").split(":")[1];
        } catch (Exception e) {
            domain = targ.replace("/", "");
        }
        String target = correctDomain(domain);
        String query = "insert into servercmsDate values((select target_id from targets where target = '" + target + "'),DATETIME())";
        System.out.println(query);
        dbcontroller.execSQL(query);
    }

    public void updateDate(String targ){
        String domain;
        try {
            domain = targ.replace("/", "").split(":")[1];
        } catch (Exception e) {
            domain = targ.replace("/", "");
        }
        String target = correctDomain(domain)+"/";
        String query = "update servercmsDate set date = DATETIME() where id = (select target_id from targets where target = '" + target + "')";
        dbcontroller.execSQL(query);

    }

    public String correctDomain(String domain){
        String result = domain;
        String query = "SELECT target from targets where target like '%" + domain + "%' and status = 200";
        ResultSet rs = dbcontroller.execSQL(query);
        try {
            while(rs.next()){
                result = rs.getString("target");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

}
