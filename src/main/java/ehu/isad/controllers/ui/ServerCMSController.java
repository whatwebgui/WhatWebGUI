package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.DBController;
import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.controllers.db.ServerCMSDB;
import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Utils;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ServerCMSController {

    private static final ServerCMSController instance = new ServerCMSController();
    private static final ServerCMSDB serverCMSDB = ServerCMSDB.getInstance();
    private final String path= Utils.getProperties().getProperty("pathToFolder");

    private ServerCMSController() { }

    public static ServerCMSController getInstance() {
        return instance;
    }

    ObservableList<ServerCMSModel> getServerCMSList() {
        return serverCMSDB.getFromDB();
    }

    void createSQLFile(String domain, String target) throws IOException {
        Process p;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(path));
            processBuilder.command("cmd.exe", "/C", "wsl whatweb --color=never --log-sql=" + domain + " " + target);
            p = processBuilder.start();
        } else {
            p = Runtime.getRuntime().exec("whatweb --color=never --log-sql=" + path + domain + " " + target);
        }
        while(p.isAlive()){}
    }

    void insertIntoDB(String domain, String target) throws IOException {
        BufferedReader input;
        input = new BufferedReader(new FileReader(path + domain + ".sql"));
        String line;
        while ((line = input.readLine()) != null) {
            ServerCMSDB.getInstance().insertQueryIntoDB(line.replace("IGNORE","OR IGNORE"));
        }
        input.close();
        //now, we will insert date information
        serverCMSDB.addDate(target);
    }

    String[] encoded(ServerCMSModel item, char servercms) {
        String var=null;
        if (servercms=='s'){
            var = URLEncoder.encode(item.getServer(), StandardCharsets.UTF_8);
        } else if (servercms == 'c'){
            var = URLEncoder.encode(item.getCms(), StandardCharsets.UTF_8);
        }
        String target = URLEncoder.encode(item.getUrl(), StandardCharsets.UTF_8);
        String version = URLEncoder.encode(item.getVersions(), StandardCharsets.UTF_8);
        String date = URLEncoder.encode(item.getLastUpdated(), StandardCharsets.UTF_8);
        return new String[]{target, var, version, date};
    }

    public void click(String domain, String target) {
        Thread thread = new Thread( () -> {
            if(serverCMSDB.domainInDB(target)){//file is already in the table
                serverCMSDB.updateDate(target);
            }else{//file is not in the table, so we will have to create the sql file and insert it into the database
                try {
                    createSQLFile(domain+".sql",target);
                    insertIntoDB(domain,target);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                File file = new File(path + domain + ".sql");
                if(file.exists()){
                    file.delete();
                }
            }
            HistoryDB.getInstance().addToHistoryDB(target,"CMS/SERVER");
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}