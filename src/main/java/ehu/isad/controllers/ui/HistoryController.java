package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.model.HistoryModel;
import ehu.isad.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML
    private TableView<HistoryModel> tableview;

    @FXML
    private TableColumn<HistoryModel, Hyperlink> col_domain;

    @FXML
    private TableColumn<HistoryModel, String> col_tab;

    @FXML
    private TableColumn<HistoryModel, Date> col_date;

    @FXML
    private TableColumn<HistoryModel, String> col_link;

    private final Tooltip tp = new Tooltip();
    Desktop desktop = java.awt.Desktop.getDesktop();

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

    private void hoverAndLinkClick(){
        tableview.setRowFactory( tr -> {
            final TableRow<HistoryModel> row = new TableRow<>();
            row.hoverProperty().addListener((observable) -> {
                final HistoryModel hm = row.getItem();
                if (row.isHover() && hm != null) {
                    getScreenshot(hm);
                    row.setTooltip(tp);
                }
            });
            row.setOnMouseMoved(event -> {
                if (! row.isEmpty() ) {
                    Hyperlink hl = row.getItem().getDomain();
                    hl.setOnAction(e -> {
                        try {
                            desktop.browse(URI.create(hl.getText()));
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

    private void getScreenshot(HistoryModel hm){
        String screen = hm.getPath().split("/")[0];
        String pathToScreenshots = Utils.getProperties().getProperty("pathToFolder")+"screenshots/";

        Image image = new Image("file:///"+pathToScreenshots+screen+".jpeg", 250, 250, true, false);
        if (image.getHeight()>10) image = new Image("file:///"+pathToScreenshots+"imgna.jpg", 250, 250, true, false);
        image = new Image("https://image.shutterstock.com/image-illustration/not-available-red-rubber-stamp-260nw-586791809.jpg", 250, 250, true, false);
        ImageView imageView = new ImageView(image);
        tp.setGraphic(imageView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDatabase();
        hoverAndLinkClick();
    }
}
