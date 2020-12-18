package ehu.isad.controllers.ui;


import ehu.isad.controllers.db.SecurityDB;
import ehu.isad.model.SecurityModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;


public class SecurityController {

    @FXML
    private Pane pane8;

    @FXML
    private TableView<SecurityModel> tableview;

    @FXML
    private TableColumn<SecurityModel, String> targetColumn;

    @FXML
    private TableColumn<SecurityModel, String> IPColumn;

    @FXML
    private TableColumn<SecurityModel, String> countryColumn;

    @FXML
    private TextField texfield;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button vuln;

    @FXML
    private MenuItem menuVuln;

    private static SecurityController instance = new SecurityController();

    private SecurityController(){}

    public static SecurityController getInstance() { return instance; }

    public SecurityDB securityDB = SecurityDB.getInstance();

    @FXML
    void onClickVuln(ActionEvent event) {
        SecurityModel sm = tableview.getSelectionModel().getSelectedItem();
        String url = sm.getUrl().getText();
        String text;
        if(url.contains("http:") && securityDB.passwordField(url)){
            text = "This website is vulnerable to Man in The Middle attack and it may also have a form, so it could also be vulnerable to SQLi or XSS attack";
            showMessage("vuln",text);
        }else if(url.contains("http:") && !securityDB.passwordField(url)){
            text = "This website is vulnerable to Man in The Middle attack";
            showMessage("vuln",text);
        }else if(!url.contains("http:") && securityDB.passwordField(url)){
            text = "This website may have a form, so it could be vulnerable to SQLi or XSS attack";
            showMessage("vuln",text);
        }else{
            text = "This website seems to be secure";
            showMessage("notVuln",text);
        }
    }

    private void showMessage(String type, String text){
        Alert alert;
        if(type.equals("vuln")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Vulnerabilites found");
        }else{
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No vulnerabilities found");
        }
        alert.setTitle("Scan completed");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void filterAll(){
        FilteredList<SecurityModel> filteredData = new FilteredList<>(securityDB.getFromSecurityDB(), b -> true);
        filter(filteredData);
    }

    /*public void filterVulnerable(){
        FilteredList<ServerCMSModel> filteredData = new FilteredList<>(serverCMSController.getFav(), b -> true);
        filter(filteredData);
    }

    public void filterNotVulnerable(){
        FilteredList<ServerCMSModel> filteredData = new FilteredList<>(serverCMSController.getFav(), b -> true);
        filter(filteredData);
    }*/

    private void filter(FilteredList<SecurityModel>filteredData){
        // 2. Set the filter Predicate whenever the filter changes.
        texfield.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(cmsmodel -> {
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = newValue.toLowerCase();
            if (cmsmodel.getUrl().getText().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches first name.
            } else // Does not match.
                if (cmsmodel.getUrl().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if(cmsmodel.getIP().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }   else return cmsmodel.getCountry().toLowerCase().contains(lowerCaseFilter);
        }));
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<SecurityModel> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tableview.setItems(sortedData);
    }

    static void Style(TableColumn<SecurityModel, String> targetColumn, TableColumn<SecurityModel, String> IPColumn, TableColumn<SecurityModel, String> countryColumn) {
        targetColumn.setReorderable(false);
        targetColumn.setStyle("-fx-alignment: CENTER;");
        IPColumn.setReorderable(false);
        IPColumn.setReorderable(false);
        countryColumn.setStyle("-fx-alignment: CENTER;");
        countryColumn.setReorderable(false);
    }

    private void style(){ Style(targetColumn, IPColumn, countryColumn); }

    public void setItems() {
        targetColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        IPColumn.setCellValueFactory(new PropertyValueFactory<>("IP"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
    }

    @FXML
    void initialize(){
        setItems();
        filterAll();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("All");
        list.add("Vulnerable");
        list.add("Not vulnerable");
        comboBox.setValue("All");
        comboBox.setItems(list);
        style();
    }

}
