package ehu.isad;

import ehu.isad.controllers.ui.MainController;
import ehu.isad.controllers.ui.SplashController;
import ehu.isad.utils.Utils;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

  private double x, y;
  private Parent mainUI;
  private Stage stageMain;
  private Stage stageSplash;
  private Scene sceneMain;
  private Scene sceneSplash;
  private MainController mainController = MainController.getInstance();
  private SplashController splashController = SplashController.getInstance();

  @Override
  public void start(Stage primaryStage) throws Exception {
    stageMain = primaryStage;
    stageSplashSetup();
    Thread threadInit = new Thread(() -> {
      initialize(); // FIXME only if it is not created!!!!
      try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
      Platform.runLater(() -> {
        try {stageMainSetup();} catch (IOException e) {e.printStackTrace();}
      });
    });
    threadInit.start();
  }

  private void initialize() {
    Properties p = Utils.getProperties();
    File directory = new File(p.getProperty("pathToFolder")); // .whatwebgui/
    if(!directory.exists()) directory.mkdir(); // FIXME user.home !
    Utils.createDirectories();
    Utils.createDB();
  }

  private void stageSplashSetup() throws IOException {
    loadSplashUI();
    stageSplash = new Stage();
    stageSplash.setScene(sceneSplash);
    stageMain.setTitle("Loading WhatWebGUI...");
    stageMain.getIcons().add(new Image(Main.class.getResourceAsStream("/img/iconsmall.png")));
    stageMain.setResizable(false);
    stageSplash.initStyle(StageStyle.TRANSPARENT);
    sceneSplash.setFill(Color.TRANSPARENT);
    stageSplash.show();

    FadeTransition fadeInSplash = new FadeTransition(Duration.millis(300), splashController.getSplash());
    fadeInSplash.setFromValue(0);
    fadeInSplash.setToValue(1);
    fadeInSplash.setCycleCount(1);
    fadeInSplash.play();
  }

  private void loadSplashUI() throws IOException {
    FXMLLoader loaderSplash = new FXMLLoader(getClass().getResource("/panes/splash.fxml"));
    loaderSplash.setController(splashController);
    Parent splashUI = loaderSplash.load();
    sceneSplash = new Scene(splashUI);
  }

  private void stageMainSetup() throws IOException {
    loadMainUI();

    stageMain.setScene(sceneMain);
    stageMain.setTitle("WhatWebGUI");
    stageMain.getIcons().add(new Image(Main.class.getResourceAsStream("/img/iconsmall.png")));
    stageMain.setResizable(false);
    stageMain.initStyle(StageStyle.UNDECORATED);

    mouseDragSetup();
    stageSplash.hide();
    stageMain.show();
    mainController.showPopUp();
  }

  private void loadMainUI() throws IOException {
    FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/panes/main.fxml"));
    loaderMain.setController(mainController);
    mainUI = loaderMain.load();
    sceneMain = new Scene(mainUI);
  }

  private void mouseDragSetup(){
    mainUI.setOnMousePressed(event -> {
      x = event.getSceneX();
      y = event.getSceneY();
    });
    mainUI.setOnMouseDragged(event -> {
      stageMain.setX(event.getScreenX() - x);
      stageMain.setY(event.getScreenY() - y);
    });
  }

  public static void main(String[] args) {
    launch(args);
  }
}
