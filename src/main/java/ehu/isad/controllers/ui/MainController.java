package ehu.isad.controllers.ui;

import ehu.isad.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private Parent pane1;
    private Parent pane2;
    private Parent pane3;
    private Parent pane4;
    private Parent pane5;
    private Parent pane6;

    @FXML
    private Pane pane;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btn4;

    @FXML
    private Button btn5;

    @FXML
    private Button btn6;

    @FXML
    private Label lbl_title;

    @FXML
    private ImageView btn_x;

    @FXML
    void handleClick(ActionEvent actionEvent) {
        pane.getChildren().clear();
        if (actionEvent.getSource() == btn1) {
            pane.getChildren().add(pane1);
            lbl_title.setText("CMS");
        }
        if (actionEvent.getSource() == btn2) {
            pane.getChildren().add(pane2);
            lbl_title.setText("Server");
        }
        if (actionEvent.getSource() == btn3) {
            pane.getChildren().add(pane3);
            lbl_title.setText("Formatter");
        }
        if (actionEvent.getSource() == btn4) {
            pane.getChildren().add(pane4);
            lbl_title.setText("History");
        }
        if (actionEvent.getSource() == btn5) {
            pane.getChildren().add(pane5);
            lbl_title.setText("Statistics");
        }
        if (actionEvent.getSource() == btn6) {
            pane.getChildren().add(pane6);
            lbl_title.setText("Settings");
        }
    }

    @FXML
    void close() {
        ((Stage)btn_x.getScene().getWindow()).close();
    }

    void getPanels() throws IOException {
        pane1 = FXMLLoader.load(getClass().getResource("/pane1.fxml")); //cms
        pane2 = FXMLLoader.load(getClass().getResource("/pane2.fxml")); //server
        pane3 = FXMLLoader.load(getClass().getResource("/pane3.fxml")); //whatweb
        pane4 = FXMLLoader.load(getClass().getResource("/pane4.fxml")); //history
        //pane5 = FXMLLoader.load(getClass().getResource("/pane5.fxml")); //stats
        //pane6 = FXMLLoader.load(getClass().getResource("/pane6.fxml")); //settings
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties p = Utils.getProperties();
        p.getProperty("path");
        File directory = new File(p.getProperty("path"));
        // If you require it to make the entire directory path including parents,
        // use directory.mkdirs(); here instead.
        if(!directory.exists()) directory.mkdir();
        try { getPanels(); } catch (IOException e) { e.printStackTrace();}
        pane.getChildren().clear();
        //pane.getChildren().add(pane1);
    }
}