package ehu.isad.controllers.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class StatusController {

    private static StatusController instance = new StatusController();

    private StatusController(){ }

    public static StatusController getInstance() { return instance; }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> urlCol;

    @FXML
    private TableColumn<?, ?> pathCol;

    @FXML
    private TableColumn<?, ?> estimatedCol;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private Label totalScans;

    @FXML
    void initialize() {

    }
}
