package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.TutorialDB;
import ehu.isad.controllers.db.WhatWebDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.SQLException;

public class SettingsController {
    @FXML
    Button btn_clear;
    MainController mainController = MainController.getInstance();
    @FXML
    Button btn_cache;
    @FXML
    private Button btn_tutorial;

    private static SettingsController instance = new SettingsController();

    private SettingsController(){ }

    public static SettingsController getInstance() { return instance; }
    
    @FXML
    void onClick(ActionEvent event) throws IOException, SQLException {
        Button btn = (Button) event.getSource();
        if (btn.equals(btn_clear)) WhatWebDB.getInstance().clearDB();
        else if (btn.equals(btn_cache)) WhatWebDB.getInstance().deleteCache();

        else {
            TutorialDB tutorialDB = TutorialDB.getInstance();
            tutorialDB.unsetTutorial();
            mainController.showPopUp();
        }
    }

}
