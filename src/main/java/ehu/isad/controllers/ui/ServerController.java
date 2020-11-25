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
import javafx.scene.input.KeyEvent;
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
        String target = textField.getText();
        if(target.charAt(target.length()-1)!='/') textField.setText(textField.getText()+"/");
        if(!target.contains(":")){
            textField.setText("http://"+textField.getText());
        }
        this.server(textField.getText());
    }
    void server(String url) throws IOException {
        String domain = url.replace("/", "").split(":")[1];
        if(validateInput(url)){
            serverCMSController.click(domain,url);
            serverTable.setItems(serverCMSController.getServerList());
        }else {
            main.showPopUp(url);

        }
    }

    @FXML
    void initialize() {
        setItems();
    }


    private boolean validateInput(String url){
        //extension split.
        String[] split = url.split("\\.");
        String keyword = split[split.length - 1];
        if(keyword.charAt(keyword.length() -1) == '/') {
            keyword = keyword.substring(0, keyword.length() - 1);
        }
        //prefix split
        String[] split2 = url.split(":");
        String protocol = split2[0];
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
    @FXML
    void onEnter(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            this.server(textField.getText());
        }
    }
}