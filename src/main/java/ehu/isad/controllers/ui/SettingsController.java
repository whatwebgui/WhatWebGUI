package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.WhatWebDB;
import javafx.fxml.FXML;

public class SettingsController {

    @FXML
    void onClick() {
        WhatWebDB.getInstance().clearDB();
    }

}
