package ehu.isad.controllers.db;

import ehu.isad.model.ServerCMSModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Random;


public class ServerCMSDB {

    private static final ServerCMSDB instance = new ServerCMSDB();
    private static final DBController dbcontroller = DBController.getController();

    private ServerCMSDB() {}

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
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public ObservableList<ServerCMSModel> getFromDB(){
        ObservableList<ServerCMSModel> results = FXCollections.observableArrayList();
        //String query = "SELECT DISTINCT q.target,q.name,q.version,q.date FROM ( SELECT t.target, CASE WHEN p.name = 'Apache' THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name = 'Apache' THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name='Apache' UNION SELECT t.target, CASE WHEN p.name = 'Apache' THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name = 'Apache' THEN s.version WHEN s.version = '0' THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name!='Apache' GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
        String listc = openFile("cms");
        String lists = openFile("server");
        String list = openFile("all");
        String query = "SELECT DISTINCT q.target,q.cms,q.versionc,q.server,q.versions,q.date FROM ( SELECT t.target, CASE WHEN p.name in " + listc + " THEN p.name ELSE 'unknown' END AS cms, CASE WHEN p.name in " + listc + " AND s.version not in ('0','') THEN s.version WHEN s.version in ('0','') THEN 'unknown' ELSE 'unknown' END AS versionc, CASE WHEN p.name in " + lists + " THEN p.name ELSE 'unknown' END AS server, CASE WHEN p.name in " + lists + " AND s.version not in ('0','') THEN s.version WHEN s.version in ('0','') THEN 'unknown' ELSE 'unknown' END AS versions, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name in " + list + "UNION SELECT t.target, 'unknown' AS cms,'unknown' AS versionc, 'unknown' AS server,'unknown' AS versions, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
        ResultSet rs = dbcontroller.execSQL(query);
        String url;
        String cms;
        String versionc;
        String server;
        String versions;
        String lastUpdated;
        try {
            while (rs.next()) {
                url = rs.getString("target");
                cms = rs.getString("cms");
                versionc = rs.getString("versionc");
                server = rs.getString("server");
                versions = rs.getString("versions");
                lastUpdated = rs.getString("date");
                results.add(new ServerCMSModel(url,cms,versionc,server,versions,lastUpdated));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return  results;
    }

    private String openFile(String tab) {
        StringBuffer sb = new StringBuffer();
        FileReader fr;
        try {
            if (tab.equals("cms")) {
                fr = new FileReader(ServerCMSDB.class.getResource("/txt/cmslist.txt").getFile());
                sb.append("('WordPress', 'Joomla', 'Drupal', 'phpMyAdmin',");
            } else if (tab.equals("server")) {
                fr=new FileReader(ServerCMSDB.class.getResource("/txt/serverlist.txt").getFile());
                sb.append("('Apache', 'nginx', ");
            } else {
                fr=new FileReader(ServerCMSDB.class.getResource("/txt/list.txt").getFile());
                sb.append("(");
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

    public void addToFavorites(ServerCMSModel selectedItem) {
        String query = "update targets set favorite = True where target like '%"+selectedItem.getUrl()+ "%'";
        DBController.getController().execSQL(query);
    }


    public ObservableList<ServerCMSModel> getFavorites() {
        String list = openFile("server");
        String query = "SELECT DISTINCT q.target,q.name,q.version,q.date FROM ( SELECT t.target, CASE WHEN p.name in " + list + " THEN p.name ELSE 'unknown' END AS name, CASE WHEN p.name in " + list + " AND s.version not in ('0','') THEN s.version WHEN s.version in ('0','') THEN 'unknown' ELSE 'unknown' END AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 and p.name in " + list + "UNION SELECT t.target, 'unknown' AS name,'unknown' AS version, s.date FROM ((targets t natural join scans s) natural join plugins p) join servercmsDate s ON t.target_id=s.id WHERE t.status=200 GROUP BY t.target ORDER BY s.date DESC ) q GROUP BY q.target ORDER by q.date DESC";
        ResultSet rs = DBController.getController().execSQL(query);
        ObservableList<ServerCMSModel> oList = FXCollections.observableArrayList();
        try{
            while(rs.next()) {
                ServerCMSModel model = new ServerCMSModel("","","","","","");
                oList.add(model);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return oList;
    }
}
