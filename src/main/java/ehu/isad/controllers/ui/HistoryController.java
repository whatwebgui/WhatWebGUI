package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.model.HistoryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML
    private TextField textField;

    @FXML
    private TableView<HistoryModel> tableview;

    @FXML
    private TableColumn<HistoryModel, Hyperlink> col_domain;

    @FXML
    private TableColumn<HistoryModel, String> col_tab;

    @FXML
    private TableColumn<HistoryModel, Date> col_date;

    private static HistoryController instance = new HistoryController();

    private HistoryController(){ }

    public static HistoryController getInstance() { return instance; }

    @FXML
    private MenuItem openBrowser;
    @FXML
    private MenuItem favUnFav;
    @FXML
    private MenuItem targetTwitter;
    @FXML
    private MenuItem targetFacebook;
    @FXML
    private MenuItem targetReddit;
    @FXML
    private MenuItem targetTumblr;
    @FXML
    void onBrowserRow(ActionEvent event) throws IOException {
        HistoryModel history = tableview.getSelectionModel().getSelectedItem();
        this.openURL(history.getDomain());
    }

    void openURL(Hyperlink url) throws IOException {
        if(System.getProperty("os.name").toLowerCase().contains("linux")){
            Runtime.getRuntime().exec("sensible-browser " + url.getText());
        }else{
            desktop.browse(URI.create(url.getText()));
        }
    }

    @FXML
    void target(ActionEvent event) throws IOException {
        MenuItem menuitem = (MenuItem) event.getSource();
        HistoryModel item = tableview.getSelectionModel().getSelectedItem();
        if (item != null){
            String url = null;
            String targetEncoded = URLEncoder.encode(item.getDomain().getText(), StandardCharsets.UTF_8);
            //String domain = URLEncoder.encode(item.getUrl().replace("/","").split(":")[1], StandardCharsets.UTF_8);
            CMSController.socialChoose(targetTwitter, targetFacebook, targetReddit, targetTumblr, desktop, menuitem, url, targetEncoded);
        }
    }

//    private final Tooltip tp = new Tooltip();
    Desktop desktop = java.awt.Desktop.getDesktop();

    private ObservableList<HistoryModel> getUserList() {
        HistoryDB historyDB = HistoryDB.getInstance();
        return FXCollections.observableArrayList(historyDB.getFromHistoryDB());
    }

    public void setItems() {
        col_domain.setCellValueFactory(new PropertyValueFactory<>("domain"));
        col_tab.setCellValueFactory(new PropertyValueFactory<>("tab"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        filter();
    }

    private void hoverAndLinkClick(){
        tableview.setRowFactory( tr -> {
            final TableRow<HistoryModel> row = new TableRow<>();
//            row.hoverProperty().addListener((observable) -> {
//                final HistoryModel hm = row.getItem();
//                if (row.isHover() && hm != null) {
//                    getScreenshot(hm);
//                    row.setTooltip(tp);
//                }
//            });
            row.setOnMouseMoved(event -> {
                if (! row.isEmpty() ) {
                    Hyperlink hl = row.getItem().getDomain();
                    hl.setOnAction(e -> {
                        try {
                            openURL(hl);
                            hl.setVisited(false);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
            });
            return row ;
        });
    }

//    private void getScreenshot(HistoryModel hm){
//       /* String screen = hm.getPath().split("/")[0];
//        String pathToScreenshots = Utils.getProperties().getProperty("pathToFolder")+"screenshots/";
//
//        Image image = new Image("file:///"+pathToScreenshots+screen+".jpeg", 250, 250, true, false);
//        if (image.getHeight()>10) image = new Image("file:///"+pathToScreenshots+"imgna.jpg", 250, 250, true, false);
//        image = new Image("https://image.shutterstock.com/image-illustration/not-available-red-rubber-stamp-260nw-586791809.jpg", 250, 250, true, false);
//        ImageView imageView = new ImageView(image);
//        tp.setGraphic(imageView);*/
//    }

    private void filter(){
        FilteredList<HistoryModel> filteredData = new FilteredList<>(getUserList(), b -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(historymodel -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (historymodel.getDomain().getText().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (historymodel.getTab().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (historymodel.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else
                    return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<HistoryModel> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tableview.setItems(sortedData);
    }

    private void style(){
        col_domain.setReorderable(false);
        col_domain.setStyle("-fx-alignment: CENTER;");
        col_date.setReorderable(false);
        col_date.setStyle("-fx-alignment: CENTER;");
        col_tab.setReorderable(false);
        col_tab.setStyle("-fx-alignment: CENTER;");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setItems();
        hoverAndLinkClick();
        tableview.setItems(getUserList());
        filter();
        style();
    }
}
