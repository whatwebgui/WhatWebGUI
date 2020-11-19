package ehu.isad.controllers.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class PopUpSureController implements Initializable {
        private static PopUpSureController instantzia = new PopUpSureController();

        public static PopUpSureController getInstantzia() {
                return instantzia;
        }

        @FXML
        private Button yesBtn;
        @FXML
        private Button noBtn;
        @FXML
        private ImageView img;
        @FXML
        private Label lbl;


        @Override
        public void initialize(URL location, ResourceBundle resources) {
                lbl = new Label();
                lbl.setText("hola");
        }

}
