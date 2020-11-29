package ehu.isad.controllers.ui;

import ehu.isad.Main;
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
    MainController mainController = new MainController();
    @FXML
    Button btn_cache;

    @FXML
    private Button btn_tutorial;
    @FXML

    void onClick(ActionEvent event) throws IOException, SQLException {
        Button btn = (Button) event.getSource();
        if (btn.equals(btn_clear)) WhatWebDB.getInstance().clearDB();
        else if (btn.equals(btn_cache)) WhatWebDB.getInstance().deleteCache();

        else {
            TutorialDB tutorialDB = TutorialDB.getInstance();
            tutorialDB.unsetTutorial();
            mainController.showPopUp("prueba");
        }
    }

}
