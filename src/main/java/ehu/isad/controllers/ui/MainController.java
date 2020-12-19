package ehu.isad.controllers.ui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ehu.isad.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.*;
import java.net.URL;
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
    private Parent pane6;
    SettingsController settingsController;
    private  Parent pane7;
    MultiController multiController;
    private Parent pane8;
    SecurityController securityController;
    private Parent pane9;
    ChartController chartController;
    int output=1;

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
    private Button btn8;
    @FXML
    private Button btn9;
    @FXML
    private Label lbl_title;

    @FXML
    private FontAwesomeIconView btn_x;

    @FXML
    private Button minimize;

    private static MainController instance = new MainController();

    private MainController(){
    }

    public static MainController getInstance() { return instance; }

    @FXML
    void handleClick(ActionEvent actionEvent) {

        pane.getChildren().clear();
        if (actionEvent.getSource() == btn1) {
            output = 1;
            cmsController.setItems();
            pane.getChildren().add(pane1);
            lbl_title.setText("CMS");
        }
        if (actionEvent.getSource() == btn2) {
            output = 2;
            serverController.setItems();
            pane.getChildren().add(pane2);
            lbl_title.setText("Server");
        }
        if (actionEvent.getSource() == btn3) {
            output = 3;
            pane.getChildren().add(pane3);
            lbl_title.setText("Formatter");
        }
        if (actionEvent.getSource() == btn4) {
            output = 4;
            historyController.setItems();
            pane.getChildren().add(pane4);
            lbl_title.setText("History");
        }
        if (actionEvent.getSource() == btn6) {
            output = 6;
            pane.getChildren().add(pane6);
            lbl_title.setText("Settings");
        }
        if (actionEvent.getSource() == btn7) {
            output = 7;
            pane.getChildren().add(pane7);
            lbl_title.setText("Multi-add");
        }
        if (actionEvent.getSource() == btn8) {
            pane.getChildren().add(pane8);
            lbl_title.setText("Security");
        }
        if (actionEvent.getSource() == btn9) {
            pane.getChildren().add(pane9);
            lbl_title.setText("Charts");
        }
    }


    @FXML
    void close() {
        File tab = new File(System.getProperty("user.home")+"/"+ Utils.getProperties().getProperty("pathToFolder")+".tab");
        if (tab.exists()){tab.delete();}
        try {
            FileOutputStream fos = new FileOutputStream(tab);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(String.valueOf(output));
            bw.newLine();
            bw.close();
        } catch (Exception e) {e.printStackTrace();}

        ((Stage)btn_x.getScene().getWindow()).close();
    }

    @FXML
    void hoverClose(){
        //TODO
    }

    public void showPopUp() throws IOException {
        File done = new File(System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToFolder")+".done");
        if(!done.exists()){
            done.createNewFile();
            FXMLLoader loaderTutorial = new FXMLLoader(getClass().getResource("/panes/tutorial.fxml"));
            loaderTutorial.setController(TutorialController.getInstance());
            Parent root = loaderTutorial.load();
            Stage stage = new Stage();
            stage.setTitle("WhatWebGUI tutorial");
            stage.getIcons().add(new Image(MainController.class.getResourceAsStream("/img/iconsmall.png")));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    private void openTab() {
        File tab = new File(System.getProperty("user.home")+"/"+ Utils.getProperties().getProperty("pathToFolder")+".tab");
        if (tab.exists()){
            FileReader fr;
            try {
                fr = new FileReader(tab);
                BufferedReader br=new BufferedReader(fr);
                String line;
                boolean gone=false;
                int lineint;
                while((line=br.readLine())!=null && !gone) {
                    try{
                    lineint = Integer.parseInt(line);}
                    catch (Exception e){lineint=1;}
                    switch(lineint){
                        case 2:
                            pane.getChildren().add(pane2);
                            lbl_title.setText("Server");
                            break;
                        case 3:
                            pane.getChildren().add(pane3);
                            lbl_title.setText("Formatter");
                            break;
                        case 4:
                            pane.getChildren().add(pane4);
                            lbl_title.setText("History");
                            break;
                        case 5:
                            pane.getChildren().add(pane8);
                            lbl_title.setText("Security");
                            break;
                        case 6:
                            pane.getChildren().add(pane6);
                            lbl_title.setText("Settings");
                            break;
                        case 7:
                            pane.getChildren().add(pane7);
                            lbl_title.setText("Multi-add");
                            break;
                        case 9:
                            pane.getChildren().add(pane9);
                            lbl_title.setText("Charts");
                        default:
                            pane.getChildren().add(pane1);
                            lbl_title.setText("CMS");
                            break;
                    }
                }
                fr.close();
            } catch(IOException e){ pane.getChildren().add(pane1);}
        } else {
            pane.getChildren().add(pane1);
        }


    }

    void getPanels() throws IOException {
        FXMLLoader loaderpane1 = new FXMLLoader(MainController.class.getResource("/panes/pane1.fxml"));
        cmsController = CMSController.getInstance();
        loaderpane1.setController(cmsController);
        pane1 = loaderpane1.load(); //cms

        FXMLLoader loaderpane2 = new FXMLLoader(MainController.class.getResource("/panes/pane2.fxml"));
        serverController = ServerController.getInstance();
        loaderpane2.setController(serverController);
        pane2 = loaderpane2.load(); //server

        FXMLLoader loaderpane3 = new FXMLLoader(MainController.class.getResource("/panes/pane3.fxml"));
        formatterController = FormatterController.getInstance();
        loaderpane3.setController(formatterController);
        pane3 = loaderpane3.load(); //formatter

        FXMLLoader loaderpane4 = new FXMLLoader(MainController.class.getResource("/panes/pane4.fxml"));
        historyController = HistoryController.getInstance();
        loaderpane4.setController(historyController);
        pane4 = loaderpane4.load(); //history

        FXMLLoader loaderpane6 = new FXMLLoader(MainController.class.getResource("/panes/pane6.fxml"));
        settingsController = SettingsController.getInstance();
        loaderpane6.setController(settingsController);
        pane6 = loaderpane6.load(); //settings

        FXMLLoader loaderpane7 = new FXMLLoader(MainController.class.getResource("/panes/pane7.fxml"));
        multiController = MultiController.getInstance();
        loaderpane7.setController(multiController);
        pane7 = loaderpane7.load(); //Multi-add option

        FXMLLoader loaderpane8 = new FXMLLoader(MainController.class.getResource("/panes/pane8.fxml"));
        securityController = SecurityController.getInstance();
        loaderpane8.setController(securityController);
        pane8 = loaderpane8.load(); //security
        FXMLLoader loaderpane9 = new FXMLLoader(MainController.class.getResource("/panes/pane9.fxml"));
        chartController = chartController.getInstance();
        loaderpane9.setController(chartController);
        pane9 = loaderpane9.load(); //security
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Properties p = Utils.getProperties();
        File directory = new File(p.getProperty("pathToFolder"));
        if(!directory.exists()) directory.mkdir();
        Utils.createDirectories();
        //Utils.getTextFromURL();
        Utils.createDB();
        try { getPanels(); } catch (IOException e) { e.printStackTrace();}
        pane.getChildren().clear();
        openTab();

    }
}