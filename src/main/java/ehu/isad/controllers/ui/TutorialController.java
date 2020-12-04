package ehu.isad.controllers.ui;

import ehu.isad.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.text.View;

public class TutorialController implements Initializable {

    @FXML
    private ImageView img;

    int i = 1;

    private static TutorialController instance = new TutorialController();

    private TutorialController(){};

    public static TutorialController getInstance() {
        return instance;
    }

    @FXML
    void onClickImg(MouseEvent event) throws FileNotFoundException {
        if (event.getButton() == MouseButton.PRIMARY) {
            if (i == 1) {
                // Image image = new Image("/tutorial/Image"+i+".png");
                Image image = new Image("/tutorial/Image2.png");
                img.setImage(image);
            } else {
                ((Stage) img.getScene().getWindow()).close();
            }


        }
        i++;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClassLoader classLoader = getClass().getClassLoader();
        Image image = new Image("/tutorial/Image1.png");
        img.setImage(image);
    }
}



