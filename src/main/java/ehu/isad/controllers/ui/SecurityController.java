package ehu.isad.controllers.ui;

import ehu.isad.model.SecurityModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SecurityController {

    @FXML
    private TextField textField;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TableView<SecurityModel> tableView;

    @FXML
    private TableColumn<SecurityModel, String> targetColumn;

    @FXML
    private TableColumn<SecurityModel, String> IPColumn;

    @FXML
    private TableColumn<SecurityModel, String> CountryColumn;

    private static SecurityController instance = new SecurityController();

    private SecurityController(){}

    public static SecurityController getInstance() { return instance; }

    public void setItems() {
        targetColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        IPColumn.setCellValueFactory(new PropertyValueFactory<>("IP"));
        CountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
    }

    @FXML
    void initialize(){
        setItems();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("All");
        list.add("Vulnerable");
        list.add("Not vulnerable");
        comboBox.setValue("All");
        comboBox.setItems(list);
    }

}
