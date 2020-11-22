package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.controllers.db.ServerCMSDB;
import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Utils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ServerController {

    @FXML
    private Pane pane1;

    @FXML
    private TextField textField;

    @FXML
    private ComboBox<?> comboBox;

    @FXML
    private Button scanBtn;

    @FXML
    private TableView<ServerCMSModel> serverTable;

    @FXML
    private TableColumn<ServerCMSModel, String> urlColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> serverColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> versionColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> lastUpdatedColumn;

    private final ServerCMSController serverCMSController = ServerCMSController.getInstance();
    MainController main = new MainController();
    ArrayList<String> extensions;
    {
        try {
            extensions = this.readExtensionLines();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setItems() {
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        serverColumn.setCellValueFactory(new PropertyValueFactory<>("server"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        serverTable.setItems(serverCMSController.getServerList());
    }

    @FXML
    void onClick(ActionEvent event) throws IOException {
        String url = textField.getText();
        String domain = url.replace("/", "").split(":")[1];
        String target = url;
        if(validateInput()){
            serverCMSController.click(domain,target);
            serverTable.setItems(serverCMSController.getServerList());
        }else {
            main.showPopUp(url);

        }
    }

    @FXML
    void initialize() {
        setItems();
    }


    private boolean validateInput(){
        //extension split.
        String[] split = textField.getText().split("\\.");
        String keyword = split[split.length - 1];
        System.out.println(keyword);
        //prefix split
        String [] split2 = textField.getText().split(":");
        String protocol = split2[0];
        System.out.println(split2[0]);
        return (extensions.contains(keyword) && (protocol.equals("http") || protocol.equals("https")));
    }



    public ArrayList<String> readExtensionLines() throws IOException {
        BufferedReader br  = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/extensions.txt" )));
        ArrayList<String>  sb = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            sb.add(line.toLowerCase());
            line = br.readLine();
        }
        return sb;
    }

}