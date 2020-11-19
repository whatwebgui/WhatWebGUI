package ehu.isad.controllers.ui;

import java.net.URL;
import java.util.ResourceBundle;

import ehu.isad.model.ServerCMS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ServerController {

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
    private TableColumn<ServerCMS, String> serverColumn;

    @FXML
    private TableColumn<ServerCMS, String> versionColumn;

    @FXML
    private TableColumn<ServerCMS, String> lastUpdatedColumn;

    @FXML
    void onClick(ActionEvent event) {

    }

    @FXML
    void initialize() {
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        serverColumn.setCellValueFactory(new PropertyValueFactory<>("server"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
    }
}