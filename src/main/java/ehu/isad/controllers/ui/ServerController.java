package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.controllers.db.ServerCMSDB;
import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Url;
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
    Url urlUtils = new Url();

    @FXML
    void onClick(ActionEvent event) {
        try {
            if(urlUtils.processUrl(textField.getText())!=null){
                Server(urlUtils.processUrl(textField.getText()));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void Server(String url) throws IOException {
        String domain = url.replace("/", "").split(":")[1];
        serverCMSController.click(domain, url);
        serverTable.setItems(serverCMSController.getServerList());
    }

    @FXML
    void onKeyPressed(KeyEvent event) throws IOException, SQLException {
        if (event.getCode().toString().equals("ENTER")) {
            urlUtils.processUrl(textField.getText());
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
    void initialize() {
        setItems();
    }
}