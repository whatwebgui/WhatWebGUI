package ehu.isad.controllers.ui;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ehu.isad.controllers.db.ServerCMSDB;
import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Url;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Date;

public class CMSController {

    @FXML
    private Pane pane1;

    @FXML
    private TextField textField;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button scanBtn;

    @FXML
    private TableView<ServerCMSModel> cmsTable;

    @FXML
    private TableColumn<ServerCMSModel, FontAwesomeIconView> starColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> urlColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> cmsColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> versionColumn;

    @FXML
    private TableColumn<ServerCMSModel, Date> lastUpdatedColumn;

    private final ServerCMSController serverCMSController = ServerCMSController.getInstance();
    private final ServerCMSDB servercmsdb = ServerCMSDB.getInstance();
    private SecurityController securityController = SecurityController.getInstance();
    private ServerController server = ServerController.getInstance();
    private ChartController chart = ChartController.getInstance();
    Url urlUtils = new Url();
    Desktop desktop = java.awt.Desktop.getDesktop();

    private static CMSController instance = new CMSController();

    private CMSController(){}

    public static CMSController getInstance() { return instance; }

    @FXML
    private MenuItem openBrowser;
    @FXML
    private MenuItem favUnFav;
    @FXML
    private MenuItem scanTwitter;
    @FXML
    private MenuItem scanReddit;
    @FXML
    private MenuItem scanTumblr;
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
        ServerCMSModel model = cmsTable.getSelectionModel().getSelectedItem();
        this.openURL(model.getUrl().getText());
    }
    void openURL(String url) throws IOException {
        if(System.getProperty("os.name").toLowerCase().contains("linux")){
            Runtime.getRuntime().exec("sensible-browser " + url);
        }else{
            desktop.browse(URI.create(url));
        }
    }

    @FXML
    void onFavUnFavRow(ActionEvent event) {
        ServerCMSModel item = cmsTable.getSelectionModel().getSelectedItem();
        if(item != null) {
            if (servercmsdb.isFav(item.getUrl().getText())) {
                servercmsdb.removeFromFavorites(item);
                item.setStar(0);
                if(comboBox.getValue().equals("Favorites")){//both filters methods will be called to update the tables(cms and server tables)
                    filterFavorites();
                    server.filterFavorites();
                } else {
                    filterAll();
                    server.filterAll();
                }
            } else {
                item.setStar(1);
                servercmsdb.addToFavorites(item);
                filterAll();
                server.filterAll();
            }
        }
    }

    @FXML
    void scan(ActionEvent event) throws IOException {
        MenuItem menuitem = (MenuItem) event.getSource();
        ServerCMSModel item = cmsTable.getSelectionModel().getSelectedItem();
        if (item != null){
            String url = null;
            String[] encoded = encoded(item);
            String domain = URLEncoder.encode(item.getUrl().getText().replace("/","").split(":")[1], StandardCharsets.UTF_8);
            if (menuitem.equals(scanTwitter)){
                url = "https://twitter.com/intent/tweet?text=Target%3A%20"+encoded[0]+
                        "%0ACMS%3A%20"+encoded[1]+
                        "%0ACMS%20version%3A%20"+encoded[2]+
                        "%0A%0AScanned%20on%20"+encoded[3]+
                        "%20with%20%40WhatWebGUI&hashtags=whatweb";
            } else if (menuitem.equals(scanReddit)){
                url = "http://www.reddit.com/submit?text=Target%3A%20"+encoded[0]+
                        "%20%20%0ACMS%3A%20"+encoded[1]+
                        "%20%20%0ACMS%20version%3A%20"+encoded[2]+
                        "%20%20%0A%0AScanned%20on%20"+encoded[3]+
                        "%20with%20%5B%40WhatWebGUI%5D(https%3A%2F%2Fwhatwebgui.github.io%2F)"+
                        "&title="+domain+"%20%7C%20CMS%20scan%20%7C%20WhatWebGUI";
            } else if (menuitem.equals(scanTumblr)){
                url = "https://www.tumblr.com/widgets/share/tool?posttype=link&caption=Target%3A%20"+encoded[0]+
                        "%3Cbr%3ECMS%3A%20"+encoded[1]+
                        "%3Cbr%3ECMS%20version%3A%20"+encoded[2]+
                        "%3Cbr%3E%3Cbr%3EScanned%20on%20"+encoded[3]+
                        "%20with%20%3Ca%20href%3D%22https%3A%2F%2Fwhatwebgui.github.io%2F%22%3E%40WhatWebGUI%3C%2Fa%3E"+
                        "&tags=whatwebgui%2Cwhatweb%2C"+domain+"&canonicalUrl="+encoded[0];
            }
            if(System.getProperty("os.name").toLowerCase().contains("linux")){
                Runtime.getRuntime().exec("sensible-browser " + url);
            }else{
                java.awt.Desktop.getDesktop().browse(URI.create(url));
            }
        }
    }

    @FXML
    void target(ActionEvent event) throws IOException {
        getItem(event, cmsTable, targetTwitter, targetFacebook, targetReddit, targetTumblr, desktop);
    }

    static void getItem(ActionEvent event, TableView<ServerCMSModel> cmsTable, MenuItem targetTwitter, MenuItem targetFacebook, MenuItem targetReddit, MenuItem targetTumblr, Desktop desktop) throws IOException {
        MenuItem menuitem = (MenuItem) event.getSource();
        ServerCMSModel item = cmsTable.getSelectionModel().getSelectedItem();
        if (item != null){
            String url = null;
            String targetEncoded = URLEncoder.encode(item.getUrl().getText(), StandardCharsets.UTF_8);
            //String domain = URLEncoder.encode(item.getUrl().replace("/","").split(":")[1], StandardCharsets.UTF_8);
            itemEquals(targetTwitter, targetFacebook, targetReddit, targetTumblr, desktop, menuitem, url, targetEncoded);
        }
    }

    static void itemEquals(MenuItem targetTwitter, MenuItem targetFacebook, MenuItem targetReddit, MenuItem targetTumblr, Desktop desktop, MenuItem menuitem, String url, String targetEncoded) throws IOException {
        socialChoose(targetTwitter, targetFacebook, targetReddit, targetTumblr, desktop, menuitem, url, targetEncoded);
    }

    static void socialChoose(MenuItem targetTwitter, MenuItem targetFacebook, MenuItem targetReddit, MenuItem targetTumblr, Desktop desktop, MenuItem menuitem, String url, String targetEncoded) throws IOException {
        if (menuitem.equals(targetTwitter)){
            url = "https://twitter.com/intent/tweet?url=";
        } else if (menuitem.equals(targetFacebook)){
            url = "https://www.facebook.com/share.php?u=";
        } else if (menuitem.equals(targetReddit)){
            url = "https://www.reddit.com/submit?url=";
        }else if (menuitem.equals(targetTumblr)){
            url = "https://www.tumblr.com/widgets/share/tool?posttype=link&canonicalUrl=";
        }
        if(System.getProperty("os.name").toLowerCase().contains("linux")){
            Runtime.getRuntime().exec("sensible-browser " + url+targetEncoded);
        }else{
            java.awt.Desktop.getDesktop().browse(URI.create(url+targetEncoded));
        }
    }

    private String[] encoded(ServerCMSModel item) {
        return serverCMSController.encoded(item,'c');
    }

    @FXML
    void onClick(ActionEvent event) throws IOException, SQLException {
        scan();
    }

    @FXML
    void onKeyPressed(KeyEvent event) throws IOException, SQLException {
        if (event.getCode().toString().equals("ENTER")) {
            scan();
        }
    }

    private void scan() throws IOException, SQLException {
        if(!textField.getText().equals("")){//there is no url in the textfield
            String url = urlUtils.processUrl(textField.getText());
            if(url!=null){
                processUrl(url,false);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error in CMS");
            alert.setHeaderText("Error on reading the provided URL");
            alert.setContentText("Textfield is empty");
            alert.showAndWait();
        }
    }

    void processUrl(String url,boolean multi){
        Scene scene = scanBtn.getScene();
            Thread thread = new Thread( () -> {
            try {
                CMS(url,multi);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Platform.runLater( () -> {
                chart.uploadCharts();
            } );
        });
        thread.start();
    }

    void CMS(String url, boolean multiadd) throws IOException {
        textField.clear();
        String domain = url.replace("/", "").split(":")[1];
        serverCMSController.click(domain, url,multiadd);
        //again both filters will be called
        securityController.filterAll();
        server.filterAll();
        filterAll();
    }

    public void filterAll(){
        FilteredList<ServerCMSModel> filteredData = new FilteredList<>(serverCMSController.getServerCMSList(), b -> true);
        filter(filteredData);
    }

    public void filterFavorites(){
        FilteredList<ServerCMSModel> filteredData = new FilteredList<>(serverCMSController.getFav(), b -> true);
        filter(filteredData);
    }

    private void filter(FilteredList<ServerCMSModel>filteredData){
        // 2. Set the filter Predicate whenever the filter changes.
        textField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(cmsmodel -> {
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = newValue.toLowerCase();
            if (cmsmodel.getUrl().getText().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches first name.
            } else // Does not match.
                if (cmsmodel.getCms().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if(cmsmodel.getLastUpdated().toString().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }   else return cmsmodel.getVersionc().toLowerCase().contains(lowerCaseFilter);
        }));
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<ServerCMSModel> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(cmsTable.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        cmsTable.setItems(sortedData);
    }

    private void style(){
        Style(starColumn, urlColumn, cmsColumn, versionColumn, lastUpdatedColumn);
    }

    static void Style(TableColumn<ServerCMSModel, FontAwesomeIconView> starColumn, TableColumn<ServerCMSModel, String> urlColumn, TableColumn<ServerCMSModel, String> cmsColumn, TableColumn<ServerCMSModel, String> versionColumn, TableColumn<ServerCMSModel, Date> lastUpdatedColumn) {
        starColumn.setReorderable(false);
        starColumn.setStyle("-fx-alignment: CENTER;");
        urlColumn.setReorderable(false);
        cmsColumn.setReorderable(false);
        cmsColumn.setStyle("-fx-alignment: CENTER;");
        versionColumn.setReorderable(false);
        versionColumn.setStyle("-fx-alignment: CENTER;");
        lastUpdatedColumn.setReorderable(false);
        lastUpdatedColumn.setStyle("-fx-alignment: CENTER;");
    }

    public void setItems() {
        starColumn.setCellValueFactory(new PropertyValueFactory<>("star"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        cmsColumn.setCellValueFactory(new PropertyValueFactory<>("cms"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("versionc"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
    }

    @FXML
    void initialize(){
        setItems();
        filterAll();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("All");
        list.add("Favorites");
        comboBox.setValue("All");
        comboBox.setItems(list);
        comboBox.setOnAction(e -> {
            String value = comboBox.getValue();
            if(value.equals("Favorites")){
                filterFavorites();
            }else{
                filterAll();
            }
        });
        serverCMSController.linkClick(cmsTable);
        style();
    }
}