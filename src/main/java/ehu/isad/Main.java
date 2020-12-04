package ehu.isad;

import ehu.isad.controllers.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

  private double x, y;
  private Parent root;
  private Stage stage;

  @Override
  public void start(Stage primaryStage) throws Exception {


    loadUI(primaryStage);
    stageSetup();
    mouseDragSetup();
    stage.show();

    //mainController.showPopUp("Prueba");
  }

  private void loadUI(Stage primaryStage) throws IOException {
    stage = primaryStage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/panes/main.fxml"));
    MainController mainController = MainController.getInstance();
    loader.setController(mainController);
    root = loader.load();
  }

  private void stageSetup(){
    stage.setScene(new Scene(root));
    stage.setTitle("WhatWebGUI");
    stage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/iconsmall.png")));
    stage.setResizable(false);
    stage.initStyle(StageStyle.UNDECORATED);
  }

  private void mouseDragSetup(){
    root.setOnMousePressed(event -> {
      x = event.getSceneX();
      y = event.getSceneY();
    });
    root.setOnMouseDragged(event -> {
      stage.setX(event.getScreenX() - x);
      stage.setY(event.getScreenY() - y);
    });
  }

  public static void main(String[] args) {
    launch(args);
  }
}
