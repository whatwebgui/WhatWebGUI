package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.ServerCMSDB;
import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Utils;
import javafx.collections.ObservableList;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ServerCMSController {

    private static final ServerCMSController instance = new ServerCMSController();
    private static final ServerCMSDB serverCMSDB = ServerCMSDB.getInstance();
    private final String path= System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToFolder");

    private ServerCMSController() { }

    public static ServerCMSController getInstance() { return instance; }

    ObservableList<ServerCMSModel> getServerCMSList() { return serverCMSDB.getFromDB(); }
    ObservableList<ServerCMSModel> getFav() { return serverCMSDB.favoritesList(); }

    void createSQLFile(String domain, String target) throws IOException {
        int lvl = SettingsController.getInstance().getAggressive();
        String whatwebPath = Utils.getProperties().getProperty("whatwebPath");
        Process p;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(path));
            processBuilder.command("cmd.exe", "/C", "wsl whatweb --color=never -a "+lvl+" --log-sql=" + domain + " " + target);
            p = processBuilder.start();
        } else {
            p = Runtime.getRuntime().exec(whatwebPath + "whatweb --color=never -a "+lvl+ " --log-sql=" + path + domain + " " + target);
        }
        while(p.isAlive()){}
    }

    void insertIntoDB(String domain, String target) throws IOException {
        BufferedReader input;
        input = new BufferedReader(new FileReader(path + domain + ".sql"));
        String line;
        while ((line = input.readLine()) != null) {
            line = line.replace("IGNORE","OR IGNORE");

            // see https://stackoverflow.com/a/603579/243532
            line = line.replace("\\'","''");

            ServerCMSDB.getInstance().insertQueryIntoDB(line);
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
        String target = URLEncoder.encode(item.getUrl().getText(), StandardCharsets.UTF_8);
        String version = URLEncoder.encode(item.getVersions(), StandardCharsets.UTF_8);
        String date = URLEncoder.encode(new SimpleDateFormat("E dd MMM yyyy HH:mm:ss z",new Locale("en", "US")).format(item.getLastUpdated()), StandardCharsets.UTF_8);
        return new String[]{target, var, version, date};
    }

    public void click(String domain, String target, boolean multiadd) {
        Thread thread = new Thread( () -> {
            if(serverCMSDB.domainInDB(target)){//file is already in the table
                serverCMSDB.deleteInfoFromScans(target);
                serverCMSDB.updateDate(target);
            }
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
            if (!multiadd) HistoryDB.getInstance().addToHistoryDB(target,"CMS/Server");
        });
        thread.start();
        try {
            thread.join();//we will use this join to deal with the multithreading
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void openURL(Hyperlink url) throws IOException {
        if(System.getProperty("os.name").toLowerCase().contains("linux")){
            Runtime.getRuntime().exec("sensible-browser " + url.getText());
        }else{
            Desktop desktop = java.awt.Desktop.getDesktop();
            desktop.browse(URI.create(url.getText()));
        }
    }

    void        linkClick(TableView<ServerCMSModel> table){
        table.setRowFactory( tr -> {
            final TableRow<ServerCMSModel> row = new TableRow<>();
            row.setOnMouseMoved(event -> {
                if (! row.isEmpty() ) {
                    Hyperlink hl = row.getItem().getUrl();
                    hl.setOnAction(e -> {
                        try {
                            openURL(hl);
                            hl.setVisited(false);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
            });
            return row ;
        });
    }
}
