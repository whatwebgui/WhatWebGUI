package ehu.isad.controllers.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TutorialController implements Initializable {



    @FXML
    private ImageView img;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        img.setImage(new Image("/proba.jpeg"));
    }
}


