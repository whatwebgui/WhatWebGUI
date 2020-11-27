package ehu.isad.controllers.ui;

import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Url;
import ehu.isad.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CMSController {

    @FXML
    private Pane pane1;

    @FXML
    private TextField textField;

    @FXML
    private ComboBox<?> comboBox;

    @FXML
    private Button scanBtn;

    @FXML
    private TableView<ServerCMSModel> cmsTable;

    @FXML
    private TableColumn<ServerCMSModel, String> urlColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> cmsColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> versionColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> lastUpdatedColumn;

    private final ServerCMSController serverCMSController = ServerCMSController.getInstance();
    MainController mainController = new MainController();
    Url urlUtils = new Url();

    @FXML
    void onClick(ActionEvent event) {
        try {
            if(urlUtils.processUrl(textField.getText())!=null){
                CMS(urlUtils.processUrl(textField.getText()));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void CMS(String url) throws IOException {
        String domain = url.replace("/", "").split(":")[1];
        serverCMSController.click(domain, url);
        cmsTable.setItems(serverCMSController.getCMSList());
    }

    @FXML
    void onKeyPressed(KeyEvent event) throws IOException, SQLException {
        if (event.getCode().toString().equals("ENTER")) {
            urlUtils.processUrl(textField.getText());
        }
    }

    public void setItems() {
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        cmsColumn.setCellValueFactory(new PropertyValueFactory<>("cms"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        cmsTable.setItems(serverCMSController.getCMSList());
    }

    @FXML
    void initialize() throws SQLException {
        setItems();
    }
}