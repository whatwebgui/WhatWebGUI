package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.model.HistoryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML
    private TableView<HistoryModel> tableview;

    @FXML
    private TableColumn<HistoryModel, String> col_domain;

    @FXML
    private TableColumn<HistoryModel, String> col_tab;

    @FXML
    private TableColumn<HistoryModel, Date> col_date;

    @FXML
    private TableColumn<HistoryModel, String> col_link;

    private ObservableList<HistoryModel> getUserList() {
        HistoryDB historyDB = HistoryDB.getInstance();
        return FXCollections.observableArrayList(historyDB.getFromHistoryDB());
    }

    public void initializeDatabase() {
        col_domain.setCellValueFactory(new PropertyValueFactory<>("domain"));
        col_tab.setCellValueFactory(new PropertyValueFactory<>("tab"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_link.setCellValueFactory(new PropertyValueFactory<>("path"));
        tableview.setItems(getUserList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDatabase();
    }
}
