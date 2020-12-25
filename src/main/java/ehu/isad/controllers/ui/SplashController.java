package ehu.isad.controllers.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class SplashController{

    private static SplashController instance = new SplashController();

    private SplashController(){ }

    public static SplashController getInstance() { return instance; }

    @FXML
    private AnchorPane splash;

    public AnchorPane getSplash(){
        return splash;
    }

    @FXML
    void initialize(){
        this.splash.setStyle("-fx-background-color: transparent;");
    }

}
