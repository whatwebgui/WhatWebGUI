package ehu.isad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

  private double x, y;
  private Parent root;
  private Stage stage;

  @Override
  public void start(Stage primaryStage) throws Exception{

    stage = primaryStage;
    root = FXMLLoader.load(getClass().getResource("/main.fxml"));
    stageSetup();
    mouseDragSetup();
    stage.show();
  }

  private void stageSetup(){
    stage.setScene(new Scene(root));
    stage.setTitle("WhatWebGUI");
    stage.getIcons().add(new Image(Main.class.getResourceAsStream("/wwlogo.png")));
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
