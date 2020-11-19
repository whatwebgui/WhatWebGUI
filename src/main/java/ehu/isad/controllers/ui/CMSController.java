package ehu.isad.controllers.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import ehu.isad.controllers.db.ServerCMSDB;
import ehu.isad.model.ServerCMS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class CMSController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane pane1;

    @FXML
    private TextField textField;

    @FXML
    private ComboBox<?> comboBox;

    @FXML
    private Button scanBtn;

    @FXML
    private TableView<ServerCMS> cmsTable;

    @FXML
    private TableColumn<ServerCMS, String> urlColumn;

    @FXML
    private TableColumn<ServerCMS, String> cmsColumn;

    @FXML
    private TableColumn<ServerCMS, String> versionColumn;

    @FXML
    private TableColumn<ServerCMS, String> lastUpdatedColumn;

    @FXML
    void onClick(ActionEvent event) throws IOException, SQLException {
        String domain = textField.getText().replace("/", "").split(":")[1];
        //mirar si el dominio ya esta en la tabla
        if(ServerCMSDB.getInstance().domainInDB(textField.getText())){//file is already in the table

        }else{//file is not in the table, so we will have to create the sql file and insert it into the database
            createSQLFile(domain+".sql");
            insertIntoDB(domain+".sql");
            //borrar el archivo sql


        }
    }

    private ObservableList<ServerCMS> getCMSList(){
        ServerCMSDB servercmsDB = ServerCMSDB.getInstance();
        return FXCollections.observableArrayList(servercmsDB.getCMSDB());
    }

    private void createSQLFile(String domain) throws IOException {
        Process p = null;
        /*if (System.getProperty("os.name").toLowerCase().contains("win")) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(directory);
            processBuilder.command("cmd.exe", "/C", "wsl " +command);
            currentProcess = processBuilder.start();
        } else {*/
            p = Runtime.getRuntime().exec("whatweb --color=never --log-sql=/home/adeiarias/"+domain+ " " + textField.getText());
            while(p.isAlive()){}
        //}
    }

    private void insertIntoDB(String domain) throws IOException {
        BufferedReader input = null;
        input = new BufferedReader(new FileReader("/home/adeiarias/"+domain));
        String line;
        while ((line = input.readLine()) != null) {
            ServerCMSDB.getInstance().insertQueryIntoDB(line.replace("IGNORE","OR IGNORE"));
        }
        input.close();
    }

    @FXML
    void initialize() {
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        cmsColumn.setCellValueFactory(new PropertyValueFactory<>("cms"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        cmsTable.setItems(getCMSList());

    }
}