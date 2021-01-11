package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.WhatWebDB;
import ehu.isad.model.MongoUser;
import ehu.isad.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;


public class SettingsController implements Initializable {
    @FXML
    Button btn_clear;
    MainController mainController = MainController.getInstance();
    @FXML
    Button btn_cache;
    @FXML
    private Button btn_tutorial;
    @FXML
    private Button btn_code;
    @FXML
    private CheckBox cbAgg;


    /*mongo stuff*/
    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField passPass;

    @FXML
    private TextField txtPass;

    @FXML
    private TextField txtCollection;

    @FXML
    private CheckBox chbxView;

    @FXML
    private Button btnLogOut;

    @FXML
    private Pane paneLogIn;

    /*-----------*/

    /*mongo stuff*/
    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField passPass;

    @FXML
    private TextField txtPass;

    @FXML
    private TextField txtCollection;

    @FXML
    private CheckBox chbxView;

    @FXML
    private Button btnLogOut;

    @FXML
    private Pane paneLogIn;

    /*-----------*/

    private static SettingsController instance = new SettingsController();

    private SettingsController(){ }

    public static SettingsController getInstance() { return instance; }


    public  int getAggressive(){if (cbAgg.isSelected()) return 3; else return 1;}

    @FXML
    void onClick(ActionEvent event) throws IOException{
        Button btn = (Button) event.getSource();
        if (btn.equals(btn_clear)){
            WhatWebDB.getInstance().clearDB();
            CMSController.getInstance().filterAll();
            ServerController.getInstance().filterAll();
            ChartController.getInstance().clearCharts();
        }
        else if (btn.equals(btn_cache)) WhatWebDB.getInstance().deleteCache();
        else if (btn.equals(btn_tutorial)){
            File done = new File(System.getProperty("user.home")+"/"+ Utils.getProperties().getProperty("pathToFolder")+".done");
            done.delete();
            mainController.showPopUp();
        } else if (btn.equals(btn_code)){
            if(System.getProperty("os.name").toLowerCase().contains("linux")){
                Runtime.getRuntime().exec("sensible-browser https://github.com/whatwebgui/whatwebgui");
            }else{
                java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/whatwebgui/whatwebgui"));
            }
        }
    }


    @FXML
    void onClickLogOut(ActionEvent event) {
        MongoUser.getInstance().setUser("");
        MongoUser.getInstance().setPassword("");
        MongoUser.getInstance().setCollection("");
        btnLogOut.setVisible(false);
        paneLogIn.setVisible(false);

        txtCollection.setText("");
        txtPass.setText("");
        txtUser.setText("");
        passPass.setText("");
    }

    @FXML
    void onClickLogIn(ActionEvent event) {
        if(!txtCollection.getText().equals("\\s+")){
            MongoUser.getInstance().setCollection(txtCollection.getText());
            MongoUser.getInstance().setUser(txtUser.getText());
            if(chbxView.isSelected()){
                MongoUser.getInstance().setPassword(txtPass.getText());
            }
            else MongoUser.getInstance().setPassword(passPass.getText());
        }
        btnLogOut.setVisible(true);
        paneLogIn.setVisible(true);
    }


    @FXML
    void onClickCheckBox(ActionEvent event) {
        //set Password visible
        if (chbxView.isSelected()){
            txtPass.setText(passPass.getText());
            passPass.setVisible(false);
        }
        else {
            passPass.setText(txtPass.getText());
            passPass.setVisible(true);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogOut.setVisible(false);
        paneLogIn.setVisible(false);
    }
}
