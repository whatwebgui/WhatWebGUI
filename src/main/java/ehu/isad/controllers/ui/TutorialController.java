package ehu.isad.controllers.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class TutorialController implements Initializable {

    @FXML
    private ImageView img;

    @FXML
    private Button closeBtn;


    @FXML
    int i = 1;
    @FXML
    void onClickImg(MouseEvent event) throws FileNotFoundException {
        System.out.println(i);
        String path = this.getClass().getResource("/tutorial/Image"+i+".png").getFile();
        Image file = new Image(path);
        //InputStream isImage = new FileInputStream(file);
        //Image a =new Image(isImage);
        img.setImage(file);

    }

    @FXML
    void onClick(ActionEvent event) {
        ((Stage) closeBtn.getScene().getWindow()).close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        img.setImage(new Image("/tutorial/Image1.png"));
    }
}



