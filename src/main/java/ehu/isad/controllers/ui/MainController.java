package ehu.isad.controllers.ui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ehu.isad.controllers.db.TutorialDB;
import ehu.isad.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private Parent pane1;
    CMSController cmsController;
    private Parent pane2;
    ServerController serverController;
    private Parent pane3;
    FormatterController formatterController;
    private Parent pane4;
    HistoryController historyController;
    private Parent pane5;
    //StatisticsController statisticsController;
    private Parent pane6;
    SettingsController settingsController;
    private  Parent pane7;
    MultiController multiController;


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
    private Button btn7;

    @FXML
    private Label lbl_title;

    @FXML
    private FontAwesomeIconView btn_x;

    private static MainController instance = new MainController();

    private MainController(){ }

    public static MainController getInstance() { return instance; }

    @FXML
    void handleClick(ActionEvent actionEvent) {
        pane.getChildren().clear();
        if (actionEvent.getSource() == btn1) {
            cmsController.setItems();
            pane.getChildren().add(pane1);
            lbl_title.setText("CMS");
        }
        if (actionEvent.getSource() == btn2) {
            serverController.setItems();
            pane.getChildren().add(pane2);
            lbl_title.setText("Server");
        }
        if (actionEvent.getSource() == btn3) {
            pane.getChildren().add(pane3);
            lbl_title.setText("Formatter");
        }
        if (actionEvent.getSource() == btn4) {
            historyController.setItems();
            pane.getChildren().add(pane4);
            lbl_title.setText("History");
        }/*
        if (actionEvent.getSource() == btn5) {
            pane.getChildren().add(pane5);
            lbl_title.setText("Statistics");
        }*/
        if (actionEvent.getSource() == btn6) {
            pane.getChildren().add(pane6);
            lbl_title.setText("Settings");
        }
        if (actionEvent.getSource() == btn7) {
            pane.getChildren().add(pane7);
            lbl_title.setText("Multi-add");
        }
    }

    @FXML
    void close() {
        ((Stage)btn_x.getScene().getWindow()).close();
    }

    @FXML
    void hoverClose(){
        //TODO
    }

    public void showPopUp(String url) throws IOException, SQLException {
            TutorialDB tutorialDB = TutorialDB.getInstance();
            if(!tutorialDB.tutorialDone()) {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("panes/tutorial.fxml")));
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
    }
    void getPanels() throws IOException {
        FXMLLoader loaderpane1 = new FXMLLoader(getClass().getResource("/panes/pane1.fxml"));
        cmsController = CMSController.getInstance();
        loaderpane1.setController(cmsController);
        pane1 = loaderpane1.load(); //cms

        FXMLLoader loaderpane2 = new FXMLLoader(getClass().getResource("/panes/pane2.fxml"));
        serverController = ServerController.getInstance();
        loaderpane2.setController(serverController);
        pane2 = loaderpane2.load(); //server

        FXMLLoader loaderpane3 = new FXMLLoader(getClass().getResource("/panes/pane3.fxml"));
        formatterController = FormatterController.getInstance();
        loaderpane3.setController(formatterController);
        pane3 = loaderpane3.load(); //formatter

        FXMLLoader loaderpane4 = new FXMLLoader(getClass().getResource("/panes/pane4.fxml"));
        historyController = HistoryController.getInstance();
        loaderpane4.setController(historyController);
        pane4 = loaderpane4.load(); //history

        /*FXMLLoader loaderpane5 = new FXMLLoader(getClass().getResource("/pane5.fxml"));
        pane5 = loaderpane5.load(); //stats
        statisticsController = loaderpane4.getController();*/

        FXMLLoader loaderpane6 = new FXMLLoader(getClass().getResource("/panes/pane6.fxml"));
        settingsController = SettingsController.getInstance();
        loaderpane6.setController(settingsController);
        pane6 = loaderpane6.load(); //settings

        FXMLLoader loaderpane7 = new FXMLLoader(getClass().getResource("/panes/pane7.fxml"));
        multiController = MultiController.getInstance();
        loaderpane7.setController(multiController);
        pane7 = loaderpane7.load(); //Multi-add option
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Properties p = Utils.getProperties();
        File directory = new File(p.getProperty("pathToFolder"));
        if(!directory.exists()) directory.mkdir();
        Utils.createDirectories();
        try { getPanels(); } catch (IOException e) { e.printStackTrace();}
        pane.getChildren().clear();

    }
}