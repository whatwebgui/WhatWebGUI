package ehu.isad.controllers.ui;

import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Url;
import ehu.isad.utils.Utils;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

    @FXML
    void onKeyPressed(KeyEvent event) throws IOException, SQLException {
        if (event.getCode().toString().equals("ENTER")) {
            urlUtils.processUrl(textField.getText());
        }
    }

    void CMS(String url) throws IOException {
        String domain = url.replace("/", "").split(":")[1];
        serverCMSController.click(domain, url);
        cmsTable.setItems(serverCMSController.getCMSList());
    }

    public void setItems() {
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        cmsColumn.setCellValueFactory(new PropertyValueFactory<>("cms"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

    }

    @FXML
    void initialize() throws SQLException {
        setItems();
        cmsTable.setItems(serverCMSController.getCMSList());
        FilteredList<ServerCMSModel> filteredData = new FilteredList<>(serverCMSController.getCMSList(), b -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (employee.getUrl().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                } else if (employee.getCms().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (employee.getVersion().toLowerCase().indexOf(lowerCaseFilter)!=-1)
                    return true;
                else
                    return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<ServerCMSModel> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(cmsTable.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        cmsTable.setItems(sortedData);
    }
}