package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.WhatWebDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingsController {

    @FXML
    Button btn_clear;

    @FXML
    Button btn_cache;

    @FXML
    void onClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.equals(btn_clear)) WhatWebDB.getInstance().clearDB();
        else WhatWebDB.getInstance().deleteCache();
    }

}
