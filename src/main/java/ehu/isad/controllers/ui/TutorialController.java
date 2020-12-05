package ehu.isad.controllers.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;


public class TutorialController implements Initializable {

    @FXML
    private ImageView img;

    int i = 1;

    private static TutorialController instance = new TutorialController();

    private TutorialController(){}

    public static TutorialController getInstance() {
        return instance;
    }

    @FXML
    void onClickImg(MouseEvent event){
        if (event.getButton() == MouseButton.PRIMARY) {
            if (i == 1) {
                // Image image = new Image("/tutorial/Image"+i+".png");
                Image image = new Image("/tutorial/Image2.png");
                img.setImage(image);
                i++;
            } else {
                i = 1;
                ((Stage) img.getScene().getWindow()).close();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("/tutorial/Image1.png");
        img.setImage(image);
    }
}



