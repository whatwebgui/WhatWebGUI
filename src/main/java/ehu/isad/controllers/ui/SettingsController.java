package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.WhatWebDB;
import ehu.isad.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.File;
import java.io.IOException;
import java.net.URI;


public class SettingsController {

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

}
